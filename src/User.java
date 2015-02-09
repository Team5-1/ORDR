import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kylejm on 25/01/15.
 */
public class User extends SQLObject {
    private static String kID_COLUMN_NAME;

    //Fetched DB fields
    private String fetchedFirstName;
    private String fetchedLastName;
    private String fetchedEmailAddress;

    //Current DB fields
    private int ID;
    private String firstName;
    private String lastName;
    private String emailAddress;

    //Constructors
    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    private User() {}

    @Override
    public void save(DatabaseManager.SQLSaveCompletionHandler handler) {

    }

    @Override
    public HashMap<String, Object> changes() {
        return null;
    }

    public void signUpUser(String password, final UserSignUpCompletionHandler handler) {
        //Validation
        if (ID != 0) {
            handler.succeeded();
            return;
        }
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(emailAddress);
        if (!matcher.find()) {
            handler.emailFormatIncorrect();
            return;
        }
        if (password.length() < 8) {
            handler.passwordTooShort();
            return;
        }

        String stmString = "INSERT INTO users SET first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + emailAddress + "', password = MD5('" + password + "')";
        try {
            PreparedStatement stm = DatabaseManager.getSharedDbConnection().prepareStatement(stmString);
            stm.execute();
            ResultSet idResult = DatabaseManager.getSharedDbConnection().prepareStatement("SELECT user_id FROM users WHERE email = '" + emailAddress + "'").executeQuery();
            idResult.next();
            ID = idResult.getInt("user_id");
            fetchedFirstName = firstName;
            firstName = null;
            fetchedLastName = lastName;
            lastName = null;
            fetchedEmailAddress = emailAddress;
            emailAddress = null;
            handler.succeeded();
            //TODO: do you have to close queries when you're done with them?
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                handler.emailAddressTaken();
            } else {
                handler.failed(e);
            }
        }
    }

    //Accessor methods
    @Override
    public Boolean hasChanges() {
        return (firstName != null || lastName != null || emailAddress != null);
    }

    @Override
    public String getIDColumnName() {
        return kID_COLUMN_NAME;
    }

    @Override
    public int getID() {
        return ID;
    }

    public interface UserSignUpCompletionHandler {
        public void succeeded();
        public void failed(SQLException exception);
        public void passwordTooShort();
        public void emailFormatIncorrect();
        public void emailAddressTaken();
    }
}
