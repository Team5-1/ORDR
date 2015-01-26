import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 25/01/15.
 */

public class Item extends ORDRSQLObject {
    private int ID;
    private String name;
    private String description;
    private double price;
    private int stockQty;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private Item() {

    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<Item> fetchAllItems() {
        ResultSet results = fetchAllObjectsOfClass(Item.class);
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            while (results.next()) {
                Item item = new Item();
                item.ID = results.getInt("item_id");
                item.name = results.getString("name");
                item.description = results.getString("description");
                item.stockQty = results.getInt("stock_qty");
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }
}
