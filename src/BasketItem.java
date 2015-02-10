import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by kylejm on 10/02/15.
 */
public class BasketItem extends SQLObject {
    //DB column names
    private static final String kID_COLUMN_NAME = "basket_item_id";
    private static final String kBASKET_ID_COLUMN_NAME = "basket_id";
    private static final String kITEM_ID_COLUMN_NAME = "item_id";
    private static final String kQUANTITY_COLUMN_NAME = "quantity";

    //Constant DB values
    private int ID;
    private Item item;

    //Fetched DB values
    private int fetchedQuantity;

    //Changed values
    private int quantity;


    public void getItemInBackground(Item.SingleItemCompletionHandler handler) {
        Item.fetchItemWithIDInBackground(item.getID(), handler);
    }

    private BasketItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public static BasketItem makeBasketItem(final int basketID, final Item item, final int quantity) {
        final BasketItem bItem = new BasketItem(item, quantity);
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> fieldsAndValues = new HashMap<String, Object>(4);
                fieldsAndValues.put(kBASKET_ID_COLUMN_NAME, basketID);
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
                    public void failed(SQLException exception) {
                        //TODO: handle this
                    }
                });
            }
        });
        return bItem;
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
    public int getQuantity() {
        return (quantity != 0) ? quantity : fetchedQuantity;
    }

    //Setters
    public void setQuantity(int quantity) {
        this.quantity = (quantity == fetchedQuantity) ? 0 : quantity;
    }
}
