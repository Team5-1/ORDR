import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;


/**
 * Created by danyalaboobakar on 28/01/15.
 */

public class ItemTableViewController extends ViewController {

    public JPanel view;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private ArrayList<Item> items;

    public ItemTableViewController() {
        //Prevent movement of columns
        table.getTableHeader().setReorderingAllowed(false);

        //Prevent cells from being edited
        table.setEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);

        //Ability to sort each item according to column.
        table.setAutoCreateRowSorter(true);

        model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Price");
        model.addColumn("Stock");

        final ItemTableViewController selfPointer = this;
        Item.fetchAllItemsInBackground(new Item.MultipleItemCompletionHandler() {
            @Override
            public void succeeded(ArrayList <Item> items) {
                selfPointer.items = items;
                for (Item item : items) {
                    model.addRow(new Object[] {item.getName(), item.getDescription(),item.getPrice(), item.getStockQty()});
                }

                //Setting the height of table rows.
                table.setRowHeight(30);
                table.setModel(model);

            }

            @Override
            public void failed(SQLException exception) {

            }
        });

        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JFrame newItem = null;
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

//                JFrame newItem = new JFrame("Item Description");
                int selectedRow = table.getSelectedRow();
                ItemDetailViewController detailVC = new ItemDetailViewController(items.get(selectedRow));
                ApplicationManager.setDisplayedViewController(detailVC);
//                newItem.setSize(850,500);
//                newItem.setVisible(true);
//
//                table.setVisible(false);
//                view.setVisible(false);
            }
        });
    }

    //Getters
    @Override
    public JPanel getView() {
        return view;
    }

    @Override
    public String getButtonLabel() { return "View Products"; }
}

