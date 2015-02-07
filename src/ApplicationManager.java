import javax.swing.*;

/**
 * Created by kylejm on 28/11/14.
 */
public class ApplicationManager {
    public static void main(String[] args) {
        AddEditProductViewController vc = new AddEditProductViewController();
        vc.setSize(300, 200);
        vc.setTitle("Add Product");
        vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vc.setVisible(true);
        /*BasketViewController vc = new BasketViewController();
        vc.createUIComponents();*/
    }
}
