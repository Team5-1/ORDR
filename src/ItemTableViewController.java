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
                    model.addRow(new Object[] {
                            item.getImage(),
                            item.getName(),
                            item.getDescription(),
                            item.getPrice(),
                            item.getStockQty()
                    });
                }

                //Setting the height of table rows.
            }

            @Override
            public void failed(SQLException exception) {

            }
        });

        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                 return (column == 0) ? Icon.class : Object.class;
            }
        };
        table.setRowHeight(30);
        table.setRowSelectionAllowed(true);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane.setViewportView(table);

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

    @Override
    public String getButtonLabel() {
        return "Products";
    }
}

