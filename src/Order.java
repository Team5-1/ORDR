import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kylejm on 22/03/15.
 */
public class Order extends SQLObject {
    //DB column names
    private static final String kID_COLUMN_NAME = "order_id";
    private static final String kUSER_ID_COLUMN_NAME = "user_id";
    private static final String kDATE_PLACED_COLUMN_NAME = "date_placed";

    //Constant DB values
    private int ID;
    private User user;
    private Date datePlaced;

    //OrderItems
    private ArrayList<Item.OrderItem> orderItems = new ArrayList<Item.OrderItem>();

    private Order() {}

    public static void placeOrderWithUser(final User user, final OrderPlacementCompletionHandler handler) {
        if (user.getBasket().size() == 0) return;

        SQLQueryTask.SQLQueryCall currentDateQuery = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String stm = "SELECT NOW()";
                return DatabaseManager.getSharedDbConnection().prepareStatement(stm).executeQuery();
            }
        };

        DatabaseManager.SQLExceptionHandler exceptionHandler = new DatabaseManager.SQLExceptionHandler() {
            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }
        };

        DatabaseManager.QuerySuccessHandler callback = new DatabaseManager.QuerySuccessHandler(exceptionHandler) {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                final Date currentDate = results.getDate(1);
                HashMap<String, Object> newOrderDetails = new HashMap<String, Object>(3);
                newOrderDetails.put(kUSER_ID_COLUMN_NAME, user.getID());
                newOrderDetails.put(kDATE_PLACED_COLUMN_NAME, currentDate);
                DatabaseManager.createRecordInBackground(newOrderDetails, getSQLTableName(Order.class), new DatabaseManager.CreateSuccessHandler(exceptionHandler) {
                    @Override
                    public void succeeded(final int ID) {
                        final Order newOrder = new Order();
                        Runnable createOrderInstanceAndItems = new Runnable() {
                            @Override
                            public void run() {
                                newOrder.ID = ID;
                                newOrder.user = user;
                                newOrder.datePlaced = currentDate;
                                try {
                                    for (Item.BasketItem bItem : user.getBasket().values()) {
                                        Item.OrderItem ordItem = Item.OrderItem.makeOrderItemFromBasketItem(bItem, newOrder);
                                        newOrder.orderItems.add(ordItem);
                                    }
                                } catch (SQLException e) {
                                    handler.failed(e);
                                }
                            }
                        };

                        Runnable callback = new Runnable() {
                            @Override
                            public void run() {
                                user.emptyBasket(new DatabaseManager.SaveOrDeleteSuccessHandler(exceptionHandler) {
                                    @Override
                                    public void succeeded() {
                                        handler.succeeded(newOrder);
                                    }
                                });
                            }
                        };
                        BackgroundQueue.addToQueue(new MainRunnableTask(createOrderInstanceAndItems, callback));
                    }
                });
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(currentDateQuery, callback));
    }

    //SQLObject
    @Override
    public HashMap<String, Object> changes() {
        return null;
    }

    @Override
    public Boolean hasChanges() {
        return false;
    }

    //Getters
    @Override
    public String getIDColumnName() {
        return kID_COLUMN_NAME;
    }

    @Override
    public int getID() {
        return ID;
    }

    public interface OrderPlacementCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        abstract public void succeeded(Order order);
    }
}
