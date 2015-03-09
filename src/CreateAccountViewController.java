import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

/**
 * Created by kylejm on 18/02/15.
 */
public class CreateAccountViewController {
    private JTextField firstNameTF;
    private JPanel view;
    private JTextField lastNameTF;
    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JPasswordField confirmPasswordTF;
    private JButton createAccountButton;
    private JLabel messageLabel;

    public CreateAccountViewController() {
        createAccountButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                final User newUser = new User(firstNameTF.getText(), lastNameTF.getText(), emailTF.getText());
                newUser.signUpInBackground(passwordTF.getText(), new User.SignUpCompletionHandler() {
                    @Override
                    public void succeeded() {
                        messageLabel.setText(String.format("Your account has been created! Your user id number is %d", newUser.getID()));
                    }

                    @Override
                    public void passwordTooShort() {
                        messageLabel.setText("Your password is too short");
                    }

                    @Override
                    public void emailFormatIncorrect() {
                        messageLabel.setText("Your email address is not in the correct format");
                    }

                    @Override
                    public void emailAddressTaken() {
                        messageLabel.setText("That email address has already been taken");
                    }

                    @Override
                    public void sqlException(SQLException exception) {
                        System.out.println(exception.getLocalizedMessage());
                    }
                });
            }
        });
    }

    public JPanel getView() {
        return view;
    }
}
