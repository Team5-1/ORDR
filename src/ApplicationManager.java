import javax.swing.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Contact Form");
        // ContactForm vc = new ContactForm();
        //frame.setContentPane(vc.getView());
        //frame.setSize(600, 500);
        //frame.setTitle("Add Product");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setVisible(true);

        Item item = new Item("Arsenal Home Jersey", "Arsenal 2014/15 Home red Jersey. PUMA dryCELL. ");
        item.setPrice(42.99);
        ItemView itemView = new ItemView(item);
        frame.setContentPane(itemView.getView());
        frame.setSize(500, 500);
        frame.setTitle("Add Product");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
