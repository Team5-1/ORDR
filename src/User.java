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

    public User(int ID, String fetchedFirstName, String fetchedLastName, String fetchedEmailAddress) {
        this.ID = ID;
        this.fetchedFirstName = fetchedFirstName;
        this.fetchedLastName = fetchedLastName;
        this.fetchedEmailAddress = fetchedEmailAddress;
    }

    private User() {}

    @Override
    public void save(DatabaseManager.SQLSaveCompletionHandler handler) {

    }

    @Override
    public HashMap<String, Object> changes() {
        return null;
    }

    public static void logInInBackground(String emailAddress, String password, LogInCompletionHandler handler) {
        //Validation
        if (!emailAddressIsValid(emailAddress)) {
            handler.emailFormatIncorrect();
            return;
        }
        if (!passwordIsValid(password)) {
            handler.passwordTooShort();
            return;
        }

        String stmString = "SELECT user_id, first_name, last_name FROM users WHERE email = '" + emailAddress + "'AND password = MD5('" + password + "')";
        try {
            ResultSet userDetails = DatabaseManager.getSharedDbConnection().prepareStatement(stmString).executeQuery();
            if (userDetails.next()) {
                User user = new User(userDetails.getInt("user_id"), userDetails.getString("first_name"), userDetails.getString("last_name"), emailAddress);
                handler.succeeded(user);
            } else {
                handler.emailAddressOrPasswordIncorrect();
            }
        } catch (SQLException e) {
            handler.failed(e);
        }
    }

    public void signUpUser(String password, final SignUpCompletionHandler handler) {
        //Validation
        if (ID != 0) {
            handler.succeeded();
            return;
        }
        if (!emailAddressIsValid(emailAddress)) {
            handler.emailFormatIncorrect();
            return;
        }
        if (!passwordIsValid(password)) {
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

    private static boolean emailAddressIsValid(String emailAddress) {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(emailAddress);
        return matcher.find();
    }

    private static boolean passwordIsValid(String password) {
        return (password.length() > 7);
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

    public String getFirstName() {
        return (firstName != null) ? firstName : fetchedFirstName;
    }

    public String getLastName() {
        return (lastName != null) ? lastName : fetchedLastName;
    }

    public String getEmailAddress() {
        return (emailAddress != null) ? emailAddress : fetchedEmailAddress;
    }

    public interface LogInCompletionHandler {
        public void succeeded(User user);
        public void emailAddressOrPasswordIncorrect();
        public void failed(SQLException exception);
        public void passwordTooShort();
        public void emailFormatIncorrect();
    }

    public interface SignUpCompletionHandler {
        public void succeeded();
        public void failed(SQLException exception);
        public void passwordTooShort();
        public void emailFormatIncorrect();
        public void emailAddressTaken();
    }

}
