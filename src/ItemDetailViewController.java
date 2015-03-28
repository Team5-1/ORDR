import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by MaywandA on 19/02/15.
 */
public class ItemDetailViewController extends ViewController {
    private JLabel itemName;
    private JLabel price;
    private JLabel itemDescription;
    private JComboBox quantityCmb;
    private JPanel panel;
    private JLabel priceTotalLabel;
    private JButton addToBasketButton;
    private JPanel BottomPanel;
    private JLabel logoLabel;
    final private Item item;

    public ItemDetailViewController(Item item) {
        this.item = item;
        populateField();
        initialiseView();
    }

    public void initialiseView() {
        ImageIcon icon = new ImageIcon("../ORDR/ORDR_Logo.png");
        Image img = icon.getImage();
        Image newImage = img.getScaledInstance(70,70, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImage);
        logoLabel.setIcon(newIcon);
        quantityCmb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String quantity = e.getItem().toString();
                if (!quantity.equalsIgnoreCase("Quantity Needed:")) {
                    priceTotalLabel.setText("£" + (Integer.parseInt(quantity) * item.getPrice()));
                }

            }
        });
        addToBasketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedItem = quantityCmb.getSelectedItem().toString();
                if (User.getCurrentUser() != null && !selectedItem.equalsIgnoreCase("Quantity Needed:")) {
                    User.getCurrentUser().addToBasket(item, Integer.parseInt(selectedItem));
                }
            }
        });
    }

    public void populateField() {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        price.setText("£ " + item.getPrice());
    }


    //Getters
    public JPanel getView() {
        return this.panel;
    }
}