import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MaywandA on 12/02/15.
 */


public class ContactForm {
    private JLabel ordr_label;
    private JPanel main_panel;
    private JLabel contact_label;
    private JPanel info_panel;
    private JLabel form_label;
    private JLabel firstname_label;
    private JLabel comment_label;
    private JPanel form_panel;
    private JPanel leftForm_panel;
    private JPanel rightForm_panel;
    private JTextField commentFill;
    private JCheckBox subscribe_checkbox;
    private JButton submit_button;
    private JLabel surname_label;
    private JLabel email_label;
    private JTextField firstnameFill;
    private JTextField surnameFill;
    private JTextField emailFill;
    private JLabel errorLbl;
    private JLabel ContactDetail_Label;


//    private void createUIComponents() {
//        TODO: place custom component creation code here
//    }



    public ContactForm() {
        submit_button.addActionListener(new ActionListener() {
            int error = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstnameFill.getText();
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
                    if (subscribe_checkbox.isSelected()) {
                        message += " and you have subscribed to the newsletter";
                    }
                    JOptionPane.showMessageDialog(new JFrame(), message);
                    errorLbl.setText("");
                    firstnameFill.setText("");
                    surnameFill.setText("");
                    commentFill.setText("");
                    emailFill.setText("");
                    subscribe_checkbox.setSelected(false);
                }
                error = 0;
            }
        });
    }

    public JPanel getView() {
        return main_panel;
    }


}
