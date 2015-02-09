import java.sql.PreparedStatement;
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
    private String fetchedUsername;
    private String fetchedFirstName;
    private String fetchedLastName;
    private String fetchedEmailAddress;

    //Current DB fields
    private int ID;
    private String username;
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
        if ID != null {
            handler.succeeded(this);
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

        String stmString = "INSERT INTO " + getSQLTableName(User.class) + " SET first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + emailAddress + "', password = MD5('" + password + "')";
        try {
            PreparedStatement stm = DatabaseManager.sharedManager.getDbConnection().prepareStatement(stmString);
            stm.execute();
            handler.succeeded();
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
        return (username != null || firstName != null || lastName != null);
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
