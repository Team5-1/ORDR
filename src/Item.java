import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 25/01/15.
 */

public class Item extends SQLObject {
    //Fetched DB fields
    private String fetchedName;
    private String fetchedDescription;
    private Double fetchedPrice;
    private Integer fetchedStockQty;

    //Current DB fields
    private int ID;
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

    public static void fetchAllItemsInBackground(final MultipleItemCompletionHandler handler) {
        fetchAllObjectsOfClassInBackground(Item.class, new DatabaseManager.SQLCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) {
                ArrayList<Item> items = new ArrayList<Item>();
                try {
                    while (results.next()) {
                        Item item = new Item();
                        item.ID = results.getInt("item_id");
                        item.fetchedName = results.getString("name");
                        item.fetchedDescription = results.getString("description");
                        item.fetchedPrice = results.getDouble("price");
                        item.fetchedStockQty = results.getInt("stock_qty");
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
        });
    }

    @Override
    public Boolean hasChanges() {
        return ((name != null) || (description != null) || (price != null) || stockQty != null);
    }


    //Getter methods
    public int getID() {
        return ID;
    }

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
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }


    //Item completion handler
    public interface SingleItemCompletionHandler {
        public void success(Item item);
        public void failed(SQLException exception);
    }

    public interface MultipleItemCompletionHandler {
        public void succeeded(ArrayList<Item> items);
        public void failed(SQLException exception);
    }
}
