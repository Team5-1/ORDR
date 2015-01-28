import java.util.HashMap;

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
    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private User() {}

    @Override
    public void save(DatabaseManager.SQLSaveCompletionHandler handler) {

    }

    @Override
    public HashMap<String, Object> changes() {
        return null;
    }

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
}
