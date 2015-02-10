import java.sql.SQLException;
import java.util.ArrayList;
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

    private class BasketItem extends SQLObject {
        //DB column names
        private static final String kID_COLUMN_NAME = "basket_item_id";
        private static final String kBASKET_ID_COLUMN_NAME = "basket_id";
        private static final String kITEM_ID_COLUMN_NAME = "item_id";
        private static final String kQUANTITY_COLUMN_NAME = "quantity";

        //Constant DB values
        private int ID;
        private int itemID;
        private int basketID;

        //Fetched DB values
        private int fetchedQuantity;

        //Changed values
        private int quantity;

        public Basket basket() {

        }

        public Item item() {

        }

        private BasketItem(int ID, int basketID, int itemID) {
            this.ID = ID;
            this.basketID = basketID;
            this.itemID = itemID;
        }

        public BasketItem makeBasketItem(Item item, Basket basket, final int quantity) {
            final BasketItem bItem  = new BasketItem(item.getID(), basket.getID(), quantity);
            BackgroundQueue.addToQueue(new Runnable() {
                @Override
                public void run() {
                    DatabaseManager.createRecordInBackground(bItem, new DatabaseManager.CreateCompletionHandler() {
                        @Override
                        public void succeeded(int ID) {
                            bItem.ID = ID;
                            bItem.fetchedQuantity = bItem.quantity;
                            bItem.quantity = 0;
                        }

                        @Override
                        public void failed(SQLException exception) {

                        }
                    });
                }
            });
            return bItem;
        }

        //SQLObject methods
        @Override
        public String getSQLTableName() {
            return super.getSQLTableName();
        }

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

    private ArrayList<BasketItem> items;

    //Fetched DB values

    //Changed values

    public static void addToBasket(Item item, int quantity) {

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
        return null;
    }

    @Override
    public int getID() {
        return 0;
    }
}
