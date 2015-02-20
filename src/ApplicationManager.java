import javax.swing.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Contact Form");
        ContactForm vc = new ContactForm();
        frame.setContentPane(vc.getView());
        frame.setSize(500, 400);
        frame.setTitle("Add Product");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
