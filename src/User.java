import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.Callable;
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
    private HashMap<Integer, BasketItem> basketItems = new HashMap<Integer, BasketItem>();

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



        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
                String select = String.format("SELECT Users.%s, Users.%s, Users.%s", kID_COLUMN_NAME, kFIRST_NAME_COLUMN_NAME, kLAST_NAME_COLUMN_NAME);
                String from = String.format(" FROM %s ", getSQLTableName(User.class));
                String where = String.format("WHERE Users.%s = '%s' AND Users.%s = MD5('%s')", kEMAIL_ADDRESS_COLUMN_NAME, emailAddress, kPASSWORD_COLUMN_NAME, password);
                String stmString = select + from + where;
                ResultSet userDetails = null;
                try {
                    userDetails = DatabaseManager.getSharedDbConnection().prepareStatement(stmString).executeQuery();
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
                return userDetails;
            }
        };


        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet userDetails) {
                try {
                    if (userDetails.next()) {
                        final User user = new User(userDetails.getInt("user_id"), userDetails.getString("first_name"), userDetails.getString("last_name"), emailAddress);
                        DatabaseManager.getSharedDbConnection().prepareStatement(String.format("UPDATE users SET %s = NOW() WHERE %s = %d", kLAST_LOGGED_IN_FIELD, kID_COLUMN_NAME, user.ID)).execute();
                        user.refreshBasketInBackground(new BasketRefreshCompletionHandler() {
                            @Override
                            public void succeeded() {
                                handler.succeeded(user);
                            }

                            @Override
                            public void sqlException(SQLException exception) {
                                handler.sqlException(exception);
                            }
                        });
                    } else {
                        handler.emailAddressOrPasswordIncorrect();
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };

        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));

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

        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {

                String columns = String.format("(%s, %s, %s, %s)", kFIRST_NAME_COLUMN_NAME, kLAST_NAME_COLUMN_NAME, kEMAIL_ADDRESS_COLUMN_NAME, kPASSWORD_COLUMN_NAME);
                String values = String.format("VALUES ('%s', '%s', '%s', MD5('%s'))", firstName, lastName, emailAddress, password);
                String stmString = "INSERT INTO " + getSQLTableName(User.class) + columns + values;
                PreparedStatement stm = DatabaseManager.getSharedDbConnection().prepareStatement(stmString);
                stm.execute(); //Insert user
                return DatabaseManager.getSharedDbConnection().prepareStatement("SELECT user_id FROM users WHERE email = '" + emailAddress + "'").executeQuery();
            }
        };

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet idResult) {
                try {
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
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                if (exception.getClass() == SQLException.class) {
                    SQLException sqlException = (SQLException) exception;
                    if (sqlException.getErrorCode() == 1062) {
                        handler.emailAddressTaken();
                    } else {
                        handler.sqlException(sqlException);
                    }
                } else {
                    handler.threadException(exception);
                }
            }
        };

        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    }

    public void refreshBasketInBackground(final BasketRefreshCompletionHandler handler) {
        final User selfPointer = this;
        BasketItem.fetchAllBasketItemsForUser(this, new BasketItem.MultipleBasketItemCompletionHandler() {
            @Override
            public void succeeded(HashMap<Integer, BasketItem> basketItems) {
                selfPointer.basketItems = basketItems;
                handler.succeeded();
            }

            @Override
            public void sqlException(SQLException exception) {
                handler.sqlException(exception);
            }
        });
    }

    public void addToBasket(final Item item, final int quantity) {
        if (item.getID() == 0) return;
        if (basketItems.containsKey(item.getID())) {
            BasketItem bItem = basketItems.get(item.getID());
            bItem.setQuantity(bItem.getQuantity() + quantity);
        } else {
            basketItems.put(item.getID(), BasketItem.makeBasketItem(getID(), item, quantity));
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

    public static abstract class LogInCompletionHandler extends DatabaseManager.SQLCompletionHandler {
        abstract public void succeeded(User user);
        abstract public void emailAddressOrPasswordIncorrect();
        abstract public void passwordTooShort();
        abstract public void emailFormatIncorrect();
    }

    public static abstract class SignUpCompletionHandler extends DatabaseManager.SQLCompletionHandler {
        abstract public void succeeded();
        abstract public void passwordTooShort();
        abstract public void emailFormatIncorrect();
        abstract public void emailAddressTaken();
    }

    public static abstract class BasketRefreshCompletionHandler extends DatabaseManager.SQLCompletionHandler {
        abstract public void succeeded();
    }
}
