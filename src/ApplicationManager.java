import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {
    public static void main(String[] args) {

        JFrame frame = new JFrame("ItemListView");
        frame.setContentPane(new ItemListView().Table);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//        AddEditProductViewController vc = new AddEditProductViewController();
//        vc.setSize(300, 200);
//        vc.setTitle("Add Product");
//        vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        vc.setVisible(true);
//        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
//            @Override
//            public void succeeded(ArrayList<Item> items) {
//                for (Item item : items) {
//                    System.out.printf("%d\n", item.getID());
//                    System.out.println(item.getName());
//                    System.out.println(item.getDescription());
//                }
//            }
//
//            @Override
//            public void failed(SQLException exception) {
//
//            }
//        });
    }
}