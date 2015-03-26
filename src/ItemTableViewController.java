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

public class ItemTableViewController implements ViewController {

    public JPanel view;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private ArrayList<Item> items;

    public ItemTableViewController() {
        //Prevent movement of columns
        table.getTableHeader().setReorderingAllowed(false);

        //Prevent cells from being edited
//        table.setEnabled(true);
        table.setRowSelectionAllowed(true);
//        table.setCellSelectionEnabled(true);
//        table.setColumnSelectionAllowed(true);

        //Ability to sort each item according to column.
        table.setAutoCreateRowSorter(true);

        model = new DefaultTableModel();
        model.addColumn("Image");
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

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow > -1) {
                    ItemDetailViewController detailVC = new ItemDetailViewController(items.get(selectedRow));
                    ApplicationManager.setDisplayedViewController(detailVC);
                }
//                ListSelectionModel cellSelectionModel = table.getSelectionModel();
//                cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//                cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
//                    @Override
//                    public void valueChanged(ListSelectionEvent e) {
//
//
//                    }
//                });
            }
        });
    }

    @Override
    public JPanel getView() {
        return view;
    }


}

