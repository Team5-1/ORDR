import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by kylejm on 18/02/15.
 */
public class CreateAccountViewController {
    private JTextField textField1;
    private JPanel panel1;
    private JTextField textField2;
    private JTextField textField3;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton createAccountButton;

    public CreateAccountViewController() {
        createAccountButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //Add signup logic here
            }
        });
    }

    public JPanel getView() {
        return panel1;
    }
}
