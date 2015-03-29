import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MaywandA on 12/02/15.
 */


public class ContactForm extends ViewController {
    private JLabel ordrLbl;
    private JPanel mainPanel;
    private JLabel headingLbl;
    private JLabel formLbl;
    private JLabel firstNameLbl;
    private JPanel leftForm_panel;
    private JTextField commentFill;
    private JCheckBox subscribeCheckbox;
    private JButton submitButton;
    private JLabel surnameLbl;
    private JLabel emailLbl;
    private JTextField FirstNameFill;
    private JTextField surnameFill;
    private JTextField emailFill;
    private JLabel errorLbl;
    private JTextField firstNameFill;
    private JPanel infoPanel;
    private JLabel contactDetailLbl;
    private JPanel formPanel;
    private JPanel rightFormPanel;
    private JLabel commentLbl;
    private JLabel contact;

//
//    private void createUIComponents() {
//        TODO: place custom component creation code here
//    }

//    public void initialiseView() {
//
//        ImageIcon icon = new ImageIcon("../ORDR/ORDR_Logo.png");
//        Image img = icon.getImage();
//        Image newImage = img.getScaledInstance(70,70, Image.SCALE_SMOOTH);
//        ImageIcon newIcon = new ImageIcon(newImage);
//        logoLabel.setIcon(newIcon);
//
//        }

    public ContactForm() {

        submitButton.addActionListener(new ActionListener() {
            int error = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameFill.getText();
                String surname = surnameFill.getText();
                String comment = commentFill.getText();
                String emailAddress = emailFill.getText();
                if ((firstName == null) || (firstName.equalsIgnoreCase(""))) {
                    errorLbl.setText("Error: Please enter first name");
                    error++;
                } else if ((surnameFill == null) || (surname.equalsIgnoreCase(""))) {
                    errorLbl.setText("Error: Please enter surname");
                    error++;
                } else if ((emailAddress == null) || (emailAddress.equalsIgnoreCase(""))) {
                    errorLbl.setText("Error: Please enter email address");
                    error++;
                } else if (!User.emailAddressIsValid(emailAddress)) {
                    errorLbl.setText("Error: Invalid email address format");
                    error++;
                } else if ((comment == null) || (comment.equalsIgnoreCase(""))) {
                    errorLbl.setText("Error: Please enter comment");
                    error++;
                } else if ((comment.length() < 10) || (comment.length() > 100)) {
                    errorLbl.setText("Error: Comment needs to be between 10 and 100 characters");
                    error++;
                }

                if (error == 0) {
                    String message = "Form has been submitted successfully";
                    if (subscribeCheckbox.isSelected()) {
                        message += " and you have subscribed to the newsletter";
                    }
                    JOptionPane.showMessageDialog(new JFrame(), message);
                    errorLbl.setText("");
                    firstNameFill.setText("");
                    surnameFill.setText("");
                    commentFill.setText("");
                    emailFill.setText("");
                    subscribeCheckbox.setSelected(false);
                }
                error = 0;
            }
        });
    }

    public JPanel getView() {
        return mainPanel;
    }

    @Override
    public String getButtonLabel() {
        return "Contact";
    }
}
