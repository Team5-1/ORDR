import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import static java.awt.Color.black;
import static java.awt.Color.white;


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
            public void succeeded(ArrayList<Item> items) {
                selfPointer.items = items;
                for (Item item : items) {
                    model.addRow(new Object[]{
                            item.getImage(),
                            item.getName(),
                            item.getDescription(),
                            item.getPrice(),
                            item.getStockQty()
                    });
                }


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
        //Setting the height of table rows.
        table.setRowHeight(150);
        table.setRowSelectionAllowed(true);
        //Ability to sort by row.
        table.setAutoCreateRowSorter(true);
        //Prevent reordering of columns.
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumn("Image").setMinWidth(150);
        scrollPane.setViewportView(table);
//        table.setSelectionForeground(black);
//        table.setSelectionBackground(white);
//        table.setShowHorizontalLines(false);
//        table.setShowVerticalLines(false);


        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
//                table.setSelectionForeground(black);
//                table.setSelectionBackground(white);
//                table.setShowHorizontalLines(false);
//                table.setShowVerticalLines(false);
                int selectedRow = table.getSelectedRow();
                if (selectedRow > -1) {
                    ItemDetailViewController detailVC = new ItemDetailViewController(items.get(selectedRow));
                    ApplicationManager.setDisplayedViewController(detailVC);
                }
                //Get the selected row and clear it when user clicks on product.
                table.getSelectedRows();
                table.clearSelection();
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

