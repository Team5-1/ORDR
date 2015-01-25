import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {
    public static void main(String[] args) {
//        AddEditProductViewController vc = new AddEditProductViewController();
//        vc.setSize(300, 200);
//        vc.setTitle("Add Product");
//        vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        vc.setVisible(true);
        DatabaseManager manager = new DatabaseManager();
        ArrayList<Item> items = new ArrayList();
        try {
            items = manager.fetchAllItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Item item : items) {
            System.out.println(item.getName());
        }
    }
}