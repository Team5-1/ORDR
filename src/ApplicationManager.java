import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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
        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
            @Override
            public void succeeded(ArrayList<Item> items) {
                for (Item item : items) {
                    System.out.printf("%d\n", item.getID());
                    System.out.println(item.getName());
                    System.out.println(item.getDescription());
                }
                Scanner scanner = new Scanner(System.in);
                String newName = scanner.nextLine();
                Item item = items.get(0);
                item.setName(newName);
                item.save(new DatabaseManager.SQLSaveCompletionHandler() {
                    @Override
                    public void succeeded() {
                        System.out.print("YAY!");
                    }

                    @Override
                    public void failed(SQLException exception) {
                        System.out.print("BOO!");
                    }
                });
            }

            @Override
            public void failed(SQLException exception) {

            }
        });
    }
}