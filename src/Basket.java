import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by kylejm on 10/02/15.
 */
public class Basket extends SQLObject {
    //DB column names
    private static final String kID_COLUMN_NAME = "basket_id";
    private static final String kUSER_COLUMN_NAME = "name";

    //Constant DB values
    private int ID;
    private int userID;

    //Shared basket
    private static final HashMap<Integer, BasketItem> basketItems = new HashMap<Integer, BasketItem>();


    //TODO: Don't think we need handler on this method. Because if it can't save we'll just keep it local
    public static void addToBasket(final Item item, final int quantity) {
        if (User.getCurrentUser() != null) {
            User.getCurrentUser().getBasketID(new User.BasketFetchCompletionHandler() {
                @Override
                public void succeeded(int basketID) {
                    basketItems.put(item.getID(), BasketItem.makeBasketItem(basketID, item, quantity));
                }

                @Override
                public void failed(SQLException exception) {
                }
            });
        }
    }

    //SQLObject methods
    @Override
    public Boolean hasChanges() {
        return null;
    }

    @Override
    public HashMap<String, Object> changes() {
        return null;
    }

    //Getters
    @Override
    public String getIDColumnName() {
        return kID_COLUMN_NAME;
    }

    public static String IDColumnName() {
        return kID_COLUMN_NAME;
    }

    public static String userIDColumnName() {
        return kUSER_COLUMN_NAME;
    }

    @Override
    public int getID() {
        return 0;
    }
}
