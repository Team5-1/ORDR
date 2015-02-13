import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by kylejm on 10/02/15.
 */
public class BasketItem extends SQLObject {
    //DB column names
    private static final String kID_COLUMN_NAME = "basket_item_id";
    private static final String kUSER_ID_COLUMN_NAME = "user_id";
    private static final String kITEM_ID_COLUMN_NAME = "item_id";
    private static final String kQUANTITY_COLUMN_NAME = "quantity";

    //Constant DB values
    private int ID;
    private int itemID;
    private Item item;

    //Fetched DB values
    private int fetchedQuantity;

    //Changed values
    private int quantity;

    private BasketItem() {

    }

    private BasketItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public static void fetchAllBasketItemsForUser(User user, final MultipleBasketItemCompletionHandler handler) {
        if (user.getID() == 0) return;

        ArrayList<String> fieldsToFetch = new ArrayList<String>(Arrays.asList(kID_COLUMN_NAME, kITEM_ID_COLUMN_NAME, kQUANTITY_COLUMN_NAME));
        HashMap<String, Object> query = new HashMap<String, Object>();
        query.put(kUSER_ID_COLUMN_NAME, user.getID());
        DatabaseManager.fetchSpecifiedFieldsForMatchingRecordsInBackground(fieldsToFetch, query, getSQLTableName(BasketItem.class), new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) {
                try {
                    while (results.next()) {
                        HashMap<Integer, BasketItem> bItems = new HashMap<Integer, BasketItem>();
                        BasketItem bItem = new BasketItem();
                        bItem.ID = results.getInt(kID_COLUMN_NAME);
                        bItem.itemID = results.getInt(kITEM_ID_COLUMN_NAME);
                        bItem.fetchedQuantity = results.getInt(kQUANTITY_COLUMN_NAME);
                        bItems.put(bItem.getItemID(), bItem);
                        handler.succeeded(bItems);
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void noResults() {
                handler.succeeded(new HashMap<Integer, BasketItem>());
            }

            @Override
            public void sqlException(SQLException exception) {

            }
        });
    }

    public static BasketItem makeBasketItem(final int userID, final Item item, final int quantity) {
        //TODO: Handle duplicate here
        if (userID == 0 || item == null || item.getID() == 0 || quantity == 0) return null;
        final BasketItem bItem = new BasketItem(item, quantity);

        HashMap<String, Object> fieldsAndValues = new HashMap<String, Object>(4);
        fieldsAndValues.put(kUSER_ID_COLUMN_NAME, userID);
        fieldsAndValues.put(kITEM_ID_COLUMN_NAME, item.getID());
        fieldsAndValues.put(kQUANTITY_COLUMN_NAME, quantity);

        DatabaseManager.createRecordInBackground(fieldsAndValues, getSQLTableName(BasketItem.class), new DatabaseManager.CreateCompletionHandler() {
            @Override
            public void succeeded(int ID) {
                bItem.ID = ID;
                bItem.fetchedQuantity = quantity;
                bItem.quantity = 0;
            }

            @Override
            public void sqlException(SQLException exception) {
                //TODO: handle this
            }
        });

        return bItem;
    }

    public void fetchItem(final Item.SingleItemCompletionHandler handler) {
        if (item != null) {
            handler.success(item);
            return;
        }
        if (itemID == 0) return;
        final BasketItem selfPointer = this;
        Item.fetchItemWithIDInBackground(itemID, new Item.SingleItemCompletionHandler() {
            @Override
            public void success(Item item) {
                selfPointer.item = item;
                selfPointer.itemID = 0;
                handler.success(item);
            }

            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }

            @Override
            public void noResult() {
                //Should never end up here since a BasketItem can't be created with out an Item
                handler.noResult();
            }
        });
    }

    //SQLObject methods
    @Override
    public Boolean hasChanges() {
        return (quantity != 0);
    }

    @Override
    public HashMap<String, Object> changes() {
        HashMap<String, Object> changes = new HashMap<String, Object>(1);
        changes.put(kQUANTITY_COLUMN_NAME, quantity);
        return changes;
    }

    @Override
    public String getIDColumnName() {
        return kID_COLUMN_NAME;
    }

    @Override
    public int getID() {
        return ID;
    }


    //Getters
    public Item getItem() { return item; }

    public int getItemID() {
            if (itemID > 0) {
                return itemID;
            } else if (item != null) {
                return item.getID();
            } else return 0;
    }

    public int getQuantity() {
        return (quantity != 0) ? quantity : fetchedQuantity;
    }

    //Setters
    public void setQuantity(int quantity) { this.quantity = (quantity == fetchedQuantity) ? 0 : quantity; }

    public static abstract class MultipleBasketItemCompletionHandler extends DatabaseManager.SQLCompletionHandler {
        abstract public void succeeded(HashMap<Integer, BasketItem> basketItems);
    }
}
