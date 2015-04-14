import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 30/03/15.
 */
public class OrderTableViewController extends ViewController {
    private JTable table;
    private JPanel view;

    public OrderTableViewController(User user) {
        Order.fetchAllOrdersForUser(user, new Order.MultipleOrderCompletionHandler() {
            @Override
            public void succeeded(ArrayList<Order> orders) {
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Date");
                tableModel.addColumn("Total");
                for (Order order : orders) {
                    float total = 0;
                    for (Item.OrderItem oItem : order.getItems()) {
                        total += oItem.getItem().getPrice() * oItem.getQuantity();
                    }
                    tableModel.addRow(new Object[]{
                            order.getDatePlaced(),
                            total
                    });
                }
                table.setModel(tableModel);
            }

            @Override
            public void failed(SQLException exception) {

            }
        });
    }

    //View Controller
    @Override
    public JPanel getView() {
        return view;
    }

    @Override
    public String getButtonLabel() {
        return "My Orders";
    }
}
