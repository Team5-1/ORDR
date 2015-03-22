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
    //DB column names
    private static final String kID_COLUMN_NAME = "user_id";
    private static final String kFIRST_NAME_COLUMN_NAME = "first_name";
    private static final String kLAST_NAME_COLUMN_NAME = "last_name";
    private static final String kEMAIL_ADDRESS_COLUMN_NAME = "email";
    private static final String kPASSWORD_COLUMN_NAME = "password";
    private static final String kLAST_LOGGED_IN_FIELD = "date_last_logged_in";

    //Constant DB values
    private int ID;

    //Fetched DB values
    private String fetchedFirstName;
    private String fetchedLastName;
    private String fetchedEmailAddress;

    //Changed values
    private String firstName;
    private String lastName;
    private String emailAddress;

    //Current logged in user
    private static User currentUser;

    //Users basket
    private HashMap<Integer, Item.BasketItem> basketItems = new HashMap<Integer, Item.BasketItem>();

    //Constructors
    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    private User(int ID, String fetchedFirstName, String fetchedLastName, String fetchedEmailAddress) {
        this.ID = ID;
        this.fetchedFirstName = fetchedFirstName;
        this.fetchedLastName = fetchedLastName;
        this.fetchedEmailAddress = fetchedEmailAddress;
    }

    private User() {}

    public static void logInInBackground(final String emailAddress, final String password, final LogInCompletionHandler handler) {
        //Validation
        if (!emailAddressIsValid(emailAddress)) {
            handler.emailFormatIncorrect();
            return;
        }
        if (!passwordIsValid(password)) {
            handler.passwordTooShort();
            return;
        }

        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String select = String.format("SELECT Users.%s, Users.%s, Users.%s", kID_COLUMN_NAME, kFIRST_NAME_COLUMN_NAME, kLAST_NAME_COLUMN_NAME);
                String from = String.format(" FROM %s ", getSQLTableName(User.class));
                String where = String.format("WHERE Users.%s = '%s' AND Users.%s = MD5('%s')", kEMAIL_ADDRESS_COLUMN_NAME, emailAddress, kPASSWORD_COLUMN_NAME, password);
                String stmString = select + from + where;
                ResultSet userDetails = DatabaseManager.getSharedDbConnection().prepareStatement(stmString).executeQuery();
                return userDetails;
            }
        };

        DatabaseManager.QueryCompletionHandler callback = new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                if (results.next()) {
                    final User user = new User(results.getInt("user_id"), results.getString("first_name"), results.getString("last_name"), emailAddress);
                    DatabaseManager.getSharedDbConnection().prepareStatement(String.format("UPDATE users SET %s = NOW() WHERE %s = %d", kLAST_LOGGED_IN_FIELD, kID_COLUMN_NAME, user.ID)).execute();
                    user.refreshBasketInBackground(new BasketRefreshCompletionHandler() {
                        @Override
                        public void succeeded() {
                            currentUser = user;
                            handler.succeeded(user);
                        }

                        @Override
                            public void failed(SQLException exception) {
                                handler.failed(exception);
                            }
                        });
                    } else {
                        handler.emailAddressOrPasswordIncorrect();
                    }
            }

            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, callback));

    }

    public void signUpInBackground(final String password, final SignUpCompletionHandler handler) {
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

        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String columns = String.format("(%s, %s, %s, %s)", kFIRST_NAME_COLUMN_NAME, kLAST_NAME_COLUMN_NAME, kEMAIL_ADDRESS_COLUMN_NAME, kPASSWORD_COLUMN_NAME);
                String values = String.format("VALUES ('%s', '%s', '%s', MD5('%s'))", firstName, lastName, emailAddress, password);
                String stmString = "INSERT INTO " + getSQLTableName(User.class) + columns + values;
                PreparedStatement stm = DatabaseManager.getSharedDbConnection().prepareStatement(stmString);
                stm.execute(); //Insert user. If email address already exists an exception will be thrown at this point
                return DatabaseManager.getSharedDbConnection().prepareStatement("SELECT user_id FROM users WHERE email = '" + emailAddress + "'").executeQuery();
            }
        };

        final User selfPointer = this;
        DatabaseManager.QueryCompletionHandler callback = new DatabaseManager.QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                results.next();
                ID = results.getInt(kID_COLUMN_NAME);
                fetchedFirstName = firstName;
                firstName = null;
                fetchedLastName = lastName;
                lastName = null;
                fetchedEmailAddress = emailAddress;
                emailAddress = null;
                currentUser = selfPointer;
                handler.succeeded();
            }

            @Override
            public void failed(SQLException exception) {
                if (exception.getErrorCode() == 1062) {
                    handler.emailAddressTaken(); //duplicate user
                } else {
                    handler.failed(exception); //error with insert or resulting user query
                }
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, callback));
    }

    public void refreshBasketInBackground(final BasketRefreshCompletionHandler handler) {
        final User selfPointer = this;
        Item.BasketItem.fetchAllBasketItemsForUser(this, new Item.BasketItem.MultipleBasketItemCompletionHandler() {
            @Override
            public void succeeded(HashMap<Integer, Item.BasketItem> basketItems) {
                selfPointer.basketItems = basketItems;
                handler.succeeded();
            }

            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }
        });
    }

    public void addToBasket(final Item item, final int quantity) {
        if (item.getID() == 0) return;
        if (basketItems.containsKey(item.getID())) {
            Item.BasketItem bItem = basketItems.get(item.getID());
            bItem.setQuantity(bItem.getQuantity() + quantity);
        } else {
            basketItems.put(item.getID(), Item.BasketItem.makeBasketItem(getID(), item, quantity));
        }
    }

    public static boolean emailAddressIsValid(String emailAddress) {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(emailAddress);
        return matcher.find();
    }

    private static boolean passwordIsValid(String password) {
        return (password.length() > 7);
    }

    //SQLObject methods
    @Override
    public void save(DatabaseManager.SaveCompletionHandler handler) {

    }

    @Override
    public HashMap<String, Object> changes() {
        HashMap<String, Object> changes  = new HashMap<String, Object>();
        if (firstName != null) changes.put(kFIRST_NAME_COLUMN_NAME, firstName);
        if (lastName != null) changes.put(kLAST_NAME_COLUMN_NAME, lastName);
        if (emailAddress != null) changes.put(kEMAIL_ADDRESS_COLUMN_NAME, emailAddress);
        return changes;
    }

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

    //Getters
    public static User getCurrentUser() {
        return currentUser;
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

    public HashMap<Integer, Item.BasketItem> getBasket() {
        return basketItems;
    }

    public interface LogInCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        abstract public void succeeded(User user);
        abstract public void emailAddressOrPasswordIncorrect();
        abstract public void passwordTooShort();
        abstract public void emailFormatIncorrect();
    }

    public interface SignUpCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        abstract public void succeeded();
        abstract public void passwordTooShort();
        abstract public void emailFormatIncorrect();
        abstract public void emailAddressTaken();
    }

    public interface BasketRefreshCompletionHandler extends DatabaseManager.SQLExceptionHandler {
        abstract public void succeeded();
    }
}
