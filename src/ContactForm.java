import javax.swing.*;

/**
 * Created by MaywandA on 12/02/15.
 */


public class ContactForm {
    private JLabel ordr_label;
    private JPanel main_panel;
    private JLabel contact_label;
    private JPanel info_panel;
    private JLabel form_label;
    private JLabel name_label;
    private JLabel email_label;
    private JLabel comment_label;
    private JPanel form_panel;
    private JPanel leftForm_panel;
    private JLabel account_label;
    private JPanel rightForm_panel;
    private JTextField nameFill;
    private JTextField emailFill;
    private JTextField accountFill;
    private JTextField commentFill;
    private JCheckBox subscribe_checkbox;
    private JButton submit_button;


//    private void createUIComponents() {
//        TODO: place custom component creation code here
//    }

    public JPanel getView() {
        return main_panel;
    }



}
