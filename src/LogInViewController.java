import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

/**
 * Created by kylejm on 10/02/15.
 */
public class LogInViewController {
    private JPanel view;
    private JButton signInButton;
    private JTextField emailTF;
    private JTextField passwordTF;
    private JLabel ordrLabel;

    public LogInViewController() {
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                User.logInInBackground(emailTF.getText(), passwordTF.getText(), new User.LogInCompletionHandler() {
                    @Override
                    public void succeeded(User user) {
                        ordrLabel.setText("Welcome " + user.getFirstName());
                    }

                    @Override
                    public void emailAddressOrPasswordIncorrect() {
                        ordrLabel.setText("Email or password incorrect");
                    }

                    @Override
                    public void sqlException(SQLException exception) {
                        ordrLabel.setText("failed");
                        System.out.println(exception.getLocalizedMessage());
                    }

                    @Override
                    public void passwordTooShort() {
                        ordrLabel.setText("Password too short");
                    }

                    @Override
                    public void emailFormatIncorrect() {
                        ordrLabel.setText("Email format incorrect");
                    }

                });
            }
        });
    }

    public JPanel getView() {
        return view;
    }
}
