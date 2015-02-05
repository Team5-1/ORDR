import javax.swing.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by danyalaboobakar on 28/01/15.
 */
public class ItemListView {

    public JPanel Table;
    private JTable List_Items;
    private JScrollPane ScrollPane;

    public ItemListView() {
        final DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Price");
        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
            @Override
            public void succeeded(ArrayList<Item> items) {
                for (Item item : items) {
                    model.addRow(new Object[] {item.getName(), item.getDescription(),item.getPrice()});
                }
                List_Items.setModel(model);
            }

            @Override
            public void failed(SQLException exception) {

            }
        });
    }
}

