import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;


/**
 * Created by danyalaboobakar on 28/01/15.
 */

public class ItemTableViewController {

    public JPanel table;
    private JTable itemsTable;
    private JScrollPane scrollPane;

    public ItemTableViewController() {
        //Prevent movement of columns
        itemsTable.getTableHeader().setReorderingAllowed(false);

        //Prevent cells from being edited
        itemsTable.setEnabled(false);

        //Ability to sort each item according to column.
        itemsTable.setAutoCreateRowSorter(true);

        final DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Price");
        model.addColumn("Stock");

        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
            @Override
            public void succeeded(ArrayList <Item> items) {
                for (Item item : items) {
                    model.addRow(new Object[] {item.getName(), item.getDescription(),item.getPrice(), item.getStockQty()});
                }

                //Setting the height of table rows.
                itemsTable.setRowHeight(30);
                itemsTable.setModel(model);

            }

            @Override
            public void failed(SQLException exception) {

            }
        });
        itemsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                JFrame newItem = null;
//                if(Table == null)
//                    newItem = new JFrame();
//                else {
//                    //remove the previous JFrame
//                    Table.setVisible(false);
//                    newItem.dispose();
//                    //create a new one
//                    newItem = new JFrame();
//
//                }

                JFrame newItem = new JFrame("Item Description");
                newItem.setSize(850,500);
                newItem.setVisible(true);

                itemsTable.setVisible(false);
                table.setVisible(false);

            }

        });
    }
}

