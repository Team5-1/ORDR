import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by kylejm on 25/01/15.
 */

public class Item extends SQLObject {
    //DB column names
    private static final String kID_COLUMN_NAME = "item_id";
    private static final String kNAME_COLUMN_NAME = "name";
    private static final String kDESCRIPTION_COLUMN_NAME = "description";
    private static final String kPRICE_COLUMN_NAME = "price";
    private static final String kSTOCK_COLUMN_NAME = "stock_qty";

    //Constant DB values
    private int ID;

    //Fetched DB values
    private String fetchedName;
    private String fetchedDescription;
    private Double fetchedPrice;
    private Integer fetchedStockQty;

    //Changed values
    private String name;
    private String description;
    private Double price;
    private Integer stockQty;


    //Constructors
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private Item() {}

    public static void fetchItemWithIDInBackground(int ID, final SingleItemCompletionHandler handler) {
        HashMap<String, Object> query = new HashMap<String, Object>();
        query.put(kID_COLUMN_NAME, ID);
        DatabaseManager.fetchAllFieldsForMatchingRecordsInBackground(query, getSQLTableName(Item.class), new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) {
                try {
                    results.next();
                    Item item = itemFromResultSet(results);
                    handler.success(item);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }

            @Override
            public void sqlException(SQLException exception) {
                handler.failed(exception);
            }

            @Override
            public void noResults() {
                handler.noResult();
            }

        });
    }

    public static void fetchAllItemsInBackground(final MultipleItemCompletionHandler handler) {
        DatabaseManager.fetchAllRecordsForTableInBackground(getSQLTableName(Item.class), new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) {
                ArrayList<Item> items = new ArrayList<Item>();
                try {
                    while (results.next()) {
                        Item item = itemFromResultSet(results);
                        items.add(item);
                    }
                    handler.succeeded(items);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }

            @Override
            public void sqlException(SQLException exception) {
                handler.failed(exception);
            }

            @Override
            public void noResults() {
                handler.noResults();
            }

        });
    }

    //TODO: Perhaps want this method to be declared in SQLObject so all SQLObjects have to implement it
    private static Item itemFromResultSet(ResultSet results) throws SQLException {
        Item item = new Item();
        item.ID = results.getInt(kID_COLUMN_NAME);
        item.fetchedName = results.getString(kNAME_COLUMN_NAME);
        item.fetchedDescription = results.getString(kDESCRIPTION_COLUMN_NAME);
        item.fetchedPrice = results.getDouble(kPRICE_COLUMN_NAME);
        item.fetchedStockQty = results.getInt(kSTOCK_COLUMN_NAME);
        return item;
    }

    //SQLObject methods
    //TODO: change this save method to be in the SQLObject only and make a updateFields private abstract method that will be called by SQLObject on save completion
    @Override
    public void save(final DatabaseManager.SaveCompletionHandler handler) {
        super.save(new DatabaseManager.SaveCompletionHandler() {
            @Override
            public void succeeded() {
                if (name != null) {
                    fetchedName = name;
                    name = null;
                }
                if (description != null) {
                    fetchedDescription = description;
                    description = null;
                }
                if (price != null) {
                    fetchedPrice = price;
                    price = null;
                }
                if (stockQty!= null) {
                    fetchedStockQty = stockQty;
                    stockQty = null;
                }
                handler.succeeded();
            }

            @Override
            public void sqlException(SQLException exception) {
                handler.sqlException(exception);
            }
        });
    }

    @Override
    public HashMap<String, Object> changes() {
        HashMap<String, Object> changes  = new HashMap<String, Object>();
        if (name != null) changes.put(kNAME_COLUMN_NAME, name);
        if (description != null) changes.put(kDESCRIPTION_COLUMN_NAME, description);
        if (price != null) changes.put(kPRICE_COLUMN_NAME, price);
        if (stockQty != null) changes.put(kSTOCK_COLUMN_NAME, stockQty);
        return changes;
    }

    @Override
    public Boolean hasChanges() {
        return ((name != null) || (description != null) || (price != null) || stockQty != null);
    }

    @Override
    public String getIDColumnName() {
        return kID_COLUMN_NAME;
    }

    @Override
    public int getID() {
        return ID;
    }


    //Getter methods
    public String getName() {
        return (name != null) ? name : fetchedName;
    }

    public String getDescription() {
        return (description != null) ? description : fetchedDescription;
    }

    public double getPrice() {
        return (price != null) ? price : fetchedPrice;
    }

    public int getStockQty() {
        return (stockQty != null) ? stockQty : fetchedStockQty;
    }


    //Setter methods
    public void setName(String name) { this.name = (name.equals(this.name)) ? null : name; }

    public void setDescription(String description) { this.description = (description.equals(this.description)) ? null : description; }

    public void setPrice(Double price) {
        this.price = (price.equals(this.price)) ? null : price;
    }

    public void setStockQty(Integer stockQty) { this.stockQty = (stockQty.equals(this.stockQty)) ? null : stockQty; }


    //Item completion handler
    public interface SingleItemCompletionHandler {
        public void success(Item item);
        public void failed(SQLException exception);
        public void noResult();
    }

    public interface MultipleItemCompletionHandler {
        public void succeeded(ArrayList<Item> items);
        public void failed(SQLException exception);
        public void noResults();
    }

    public static class BasketItem extends SQLObject {
        //DB column names
        private static final String kID_COLUMN_NAME = "basket_item_id";
        private static final String kUSER_ID_COLUMN_NAME = "user_id";
        private static final String kITEM_ID_COLUMN_NAME = "item_id";
        private static final String kQUANTITY_COLUMN_NAME = "quantity";

        //Constant DB values
        private int ID;
        private Item item;

        //Fetched DB values
        private int fetchedQuantity;

        //Changed values
        private int quantity;

        private BasketItem() {}

        private BasketItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public static void fetchAllBasketItemsForUser(User user, final MultipleBasketItemCompletionHandler handler) {
            if (user.getID() == 0) return;

            String baskItemTableName = getSQLTableName(BasketItem.class);
            String itemTableName = getSQLTableName(Item.class);
            String select = "SELECT * FROM " + baskItemTableName;
            String join = String.format(" LEFT JOIN %s ON %s.%s = %s.%s ", itemTableName, itemTableName, kITEM_ID_COLUMN_NAME, baskItemTableName, kITEM_ID_COLUMN_NAME);
            String where = String.format("WHERE %s.%s = %d", baskItemTableName, kUSER_ID_COLUMN_NAME, user.getID());
            final String stmString = select + join + where;

            Callable<ResultSet> query = new Callable<ResultSet>() {
                @Override
                public ResultSet call() throws Exception {
                    return DatabaseManager.getSharedDbConnection().prepareStatement(stmString).executeQuery();
                }
            };

            MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
                @Override
                public void complete(ResultSet results) {
                    try {
                        HashMap<Integer, BasketItem> bItems = new HashMap<Integer, BasketItem>();
                        while (results.next()) {
                            BasketItem bItem = new BasketItem();
                            bItem.ID = results.getInt(kID_COLUMN_NAME);
                            bItem.fetchedQuantity = results.getInt(kQUANTITY_COLUMN_NAME);
                            bItem.item = itemFromResultSet(results);
                            bItems.put(bItem.item.getID(), bItem);
                        }
                        handler.succeeded(bItems);
                    } catch (SQLException e) {
                        handler.sqlException(e);
                    }
                }

                @Override
                public void failed(Exception exception) {
                    handler.handleException(exception);
                }
            };

            BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
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

        //SQLObject methods
        @Override
        public Boolean hasChanges() {
            return (quantity != 0);
        }

        @Override
        public HashMap<String, Object> changes() {
            if
            HashMap<String, Object> changes = new HashMap<String, Object>(1);
            if (quantity != 0) changes.put(kQUANTITY_COLUMN_NAME, quantity);
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

        public int getQuantity() {
            return (quantity != 0) ? quantity : fetchedQuantity;
        }

        //Setters
        public void setQuantity(int quantity) { this.quantity = (quantity == fetchedQuantity) ? 0 : quantity; }

        public static abstract class MultipleBasketItemCompletionHandler extends DatabaseManager.SQLCompletionHandler {
            abstract public void succeeded(HashMap<Integer, BasketItem> basketItems);
        }
    }

}
