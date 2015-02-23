import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by MaywandA on 19/02/15.
 */
public class ItemView {
    private JLabel itemName;
    private JLabel price;
    private JLabel itemDescription;
    private JComboBox quantityCmb;
    private JPanel panel;
    private JLabel priceTotalLabel;
    private JButton BasketButton;
    private JLabel logoLabel;
    private Item item;

    public ItemView(final Item item) {

        this.item = item;
        populateField();
        quantityCmb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String quantity = e.getItem().toString();
                if (!quantity.equalsIgnoreCase("Quantity Needed:")) {
                    priceTotalLabel.setText("£" + (Integer.parseInt(quantity) * item.getPrice()));
                }

            }
        });
    }

    public void populateField() {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        price.setText("£ " + item.getPrice());
    }

    public JPanel getView() {
        return this.panel;
    }

}