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
        if (user.getBasket().size() == 0) handler.succeeded(null);

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
                System.out.println(exception.getLocalizedMessage());
                handler.failed(exception);
            }
        };

        DatabaseManager.QuerySuccessHandler callback = new DatabaseManager.QuerySuccessHandler(exceptionHandler) {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                results.next();
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
                                    System.out.println(e.getLocalizedMessage());
                                    handler.failed(e);
                                }
                            }
                        };

                        Runnable callback = new Runnable() {
                            @Override
                            public void run() {
                                user.emptyBasket();
                                handler.succeeded(newOrder);
                            }
                        };

                        BackgroundQueue.addToQueue(new MainRunnableTask(createOrderInstanceAndItems, callback));
                    }
                });
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(currentDateQuery, callback));
    }

    public static void fetchAllOrdersForUser(final User user, final MultipleOrderCompletionHandler handler) {
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String orderTableName = getSQLTableName(Order.class);
                String orderItemTableName = getSQLTableName(Item.OrderItem.class);
                String select = "SELECT * FROM " + orderTableName;
                String joinOrderItems = String.format(" LEFT JOIN %s ON %s.%s = %s.%s ", orderItemTableName, orderTableName, kID_COLUMN_NAME, orderItemTableName, kID_COLUMN_NAME);
                String itemTableName = getSQLTableName(Item.class);
                String joinItems = String.format("LEFT JOIN %s ON %s.%s = %s.%s", itemTableName, itemTableName, "item_id", orderItemTableName, "item_id");
                String where = String.format("WHERE %s.%s = %s", orderTableName, kUSER_ID_COLUMN_NAME, user.getID());
                String stmString = select + joinOrderItems + joinItems + where;
                return DatabaseManager.getSharedDbConnection().prepareStatement(stmString).executeQuery();
            }
        };

        DatabaseManager.QueryCompletionHandler callback = new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                ArrayList<Order> orders = new ArrayList<Order>();
                Order lastInsertedOrder = null;
                while (results.next()) {
                    int orderID = results.getInt(kID_COLUMN_NAME);
                    if (lastInsertedOrder == null || lastInsertedOrder.getID() != orderID) {
                        lastInsertedOrder = new Order();
                        lastInsertedOrder.ID = orderID;
                        lastInsertedOrder.user = user;
                        lastInsertedOrder.datePlaced = results.getDate(kDATE_PLACED_COLUMN_NAME);
                        orders.add(lastInsertedOrder);
                    }
                    lastInsertedOrder.orderItems.add(Item.OrderItem.makeOrderItemWithResultSet(results));
                }
                handler.succeeded(orders);
            }

            @Override
            public void failed(SQLException exception) {
                System.out.println(exception.getLocalizedMessage());
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, callback));
    }

    public interface MultipleOrderCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        public void succeeded(ArrayList<Order> orders);
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

    public Date getDatePlaced() {
        return datePlaced;
    }

    public ArrayList<Item.OrderItem> getItems() {
        return orderItems;
    }

    public interface OrderPlacementCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        abstract public void succeeded(Order order);
    }
}
