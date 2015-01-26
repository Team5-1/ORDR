import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 25/01/15.
 */

public class Item {
    private int ID;
    private String name;
    private String description;

    public Item(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
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
        try {
            ResultSet results = DatabaseManager.sharedManager.fetchAllRowsForTable("Items");
            ArrayList<Item> items = new ArrayList<Item>(results.getFetchSize());
            while (results.next()) {
                items.add(new Item(results.getInt("item_id"), results.getString("name"), results.getString("description")));
            }
            return items;
        } catch (SQLException e) {

        }
        return null;
    }
}
