import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 25/01/15.
 */
public class User extends ORDRSQLObject {
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
    public Boolean hasChanges() {
        return (username != null || firstName != null || lastName != null);
    }

}
