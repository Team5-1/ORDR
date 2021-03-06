import javax.swing.*;
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
    private JTextArea itemDescription ;
    private JComboBox quantityCmb;
    private JPanel panel;
    private JLabel priceTotalLabel;
    private JButton BasketButton;
    private JPanel BottomPanel;
    private JLabel itemImgDisplay;
    private JLabel ordrLbl;
    final private Item item;


    public ItemDetailViewController(Item item) {
        this.item = item;
        populateField();
        initialiseView();
    }

    public void initialiseView() {
        quantityCmb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String quantity = e.getItem().toString();
                if (!quantity.equalsIgnoreCase("")) {
                    priceTotalLabel.setText("£" + (Integer.parseInt(quantity) * item.getPrice()));
                }
            }
        });
        BasketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (User.getCurrentUser() != null) {
                    int quantity = Integer.parseInt(quantityCmb.getSelectedItem().toString());
                    User.getCurrentUser().addToBasket(item, quantity);
                    ApplicationManager.displayViewControllerAtIndex(1);
                }
            }
        });
    }

    public void populateField() {
        itemName.setText(item.getName());
        itemImgDisplay.setIcon(item.getImage());
        itemDescription.setText(item.getDescription());
        price.setText("£ " + item.getPrice());
        itemDescription.setText(item.getDescription());
        itemDescription.setEditable(false);
        itemDescription.setLineWrap(true);
    }


    //Getters
    public JPanel getView() {
        return this.panel;
    }
}