package DatabaseClasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
                    Item item = new Item();
                    item.ID = results.getInt(kID_COLUMN_NAME);
                    item.fetchedName = results.getString(kNAME_COLUMN_NAME);
                    item.fetchedDescription = results.getString(kDESCRIPTION_COLUMN_NAME);
                    item.fetchedPrice = results.getDouble(kPRICE_COLUMN_NAME);
                    item.fetchedStockQty = results.getInt(kSTOCK_COLUMN_NAME);
                    handler.success(item);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }

            @Override
            public void failed(SQLException exception) {
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
                        Item item = new Item();
                        item.ID = results.getInt(kID_COLUMN_NAME);
                        item.fetchedName = results.getString(kNAME_COLUMN_NAME);
                        item.fetchedDescription = results.getString(kDESCRIPTION_COLUMN_NAME);
                        item.fetchedPrice = results.getDouble(kPRICE_COLUMN_NAME);
                        item.fetchedStockQty = results.getInt(kSTOCK_COLUMN_NAME);
                        items.add(item);
                    }
                    handler.succeeded(items);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }

            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }

            @Override
            public void noResults() {
                handler.noResults();
            }

        });
    }

    //SQLObject methods
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
            public void failed(SQLException exception) {
                handler.failed(exception);
            }
        });
    }

    @Override
    public HashMap<String, Object> changes() {
        HashMap<String, Object> changes  = new HashMap<String, Object>();
        if (name != null) changes.put(kNAME_COLUMN_NAME, name);
        if (description != null) changes.put(kDESCRIPTION_COLUMN_NAME, description);
        if (price != null) changes.put(kPRICE_COLUMN_NAME, price);
        if (stockQty!= null) changes.put(kSTOCK_COLUMN_NAME, stockQty);
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
}
