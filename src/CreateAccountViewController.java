import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

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
                User newUser = new User(textField1.getText(), textField2.getText(), textField3.getText());
                newUser.signUpInBackground(passwordField1.getText(), new User.SignUpCompletionHandler() {
                    @Override
                    public void succeeded() {
                        
                    }

                    @Override
                    public void passwordTooShort() {

                    }

                    @Override
                    public void emailFormatIncorrect() {

                    }

                    @Override
                    public void emailAddressTaken() {

                    }

                    @Override
                    public void sqlException(SQLException exception) {

                    }
                });
            }
        });
    }

    public JPanel getView() {
        return panel1;
    }
}
