import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by kylejm on 24/01/15.
 */
public class DatabaseManager {
    static final DatabaseManager sharedManager = new DatabaseManager();
    //TODO handlers should get calledBack on a different thread to the background thread

    private Connection dbConnection;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // initialise the JDBC driver, with a check for it working
            this.dbConnection = DriverManager.getConnection(DatabaseCredentials.localURL, DatabaseCredentials.localUsername, DatabaseCredentials.localPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found; is your CLASSPATH set?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: MySQL Connection Failed!");
            e.printStackTrace();
        }
        
    }

    public static void fetchAllRowsForTableInBackground(final String tableName, final SQLQueryCompletionHandler handler) {
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement("SELECT * FROM " + tableName);
                    ResultSet results = stm.executeQuery();
                    handler.succeeded(results);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        });
    }

    public static void fetchFieldsForTableInBackground(final ArrayList<String> fields, final String tableName, final SQLQueryCompletionHandler handler) {
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    String stmString = "SELECT ";
                    int fieldCount = fields.size();
                    int i = 0;
                    for (String field : fields) {
                        stmString += (i == fieldCount) ? field + " " : field + ", ";
                        i++;
                    }
                    stmString += "FROM " + tableName;
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    ResultSet results = stm.executeQuery();
                    handler.succeeded(results);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        });
    }

    public static void queryTableInBackground(final HashMap<String, Object> fieldsAndValuesToMatch, final ArrayList<String> fieldsToFetch, final String tableName, final SQLQueryCompletionHandler handler) {
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    String stmString = "SELECT ";
                    int fieldCount = fieldsToFetch.size();
                    int i = 0;
                    for (String field : fieldsToFetch) {
                        stmString += (i == fieldCount) ? field + ", " : field + " ";
                        i++;
                    }
                    stmString += "FROM " + tableName + " WHERE ";
                    ArrayList<Object> values = new ArrayList<Object>();
                    fieldCount = fieldsAndValuesToMatch.size();
                    i = 1;
                    for (String key : fieldsAndValuesToMatch.keySet()) {
                        stmString += key + " = ?";
                        if (i != fieldCount) stmString +=  ", ";
                        values.add(fieldsAndValuesToMatch.get(key));
                        i++;
                    }
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    for (i = 0; i < values.size(); i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    ResultSet result = stm.executeQuery();
                    handler.succeeded(result);
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        });
    }


    public static void updateFieldsForRecord(final String tableName, final String recordKeyName, final int recordKey, final HashMap<String, Object> columnsAndValues, final SQLSaveCompletionHandler handler) {
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    String stmString = "UPDATE " + tableName + " SET ";
                    ArrayList<Object> values = new ArrayList<Object>(columnsAndValues.size());
                    for (String key : columnsAndValues.keySet()) {
                        stmString +=  key + " = ?, ";
                        values.add(columnsAndValues.get(key));
                    }
                    stmString += "date_updated = NOW() WHERE " + recordKeyName + " = " + String.format("%d", recordKey);
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    for (int i = 0; i < values.size(); i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    stm.executeUpdate();
                    handler.succeeded();
                } catch (SQLException e) {
                    System.out.println(e.getLocalizedMessage());
                    handler.failed(e);
                }
            }
        });
    }

    private void handleExcetion(SQLException e) {
        System.out.println("ERROR: MySQL Connection Failed!");
        e.printStackTrace();
    }

    public interface SQLQueryCompletionHandler {
        public void succeeded(ResultSet results);
        public void failed(SQLException exception);
    }

    public interface SQLSaveCompletionHandler {
        public void succeeded();
        public void failed(SQLException exception);
    }


    //Accessor method

    public Connection getDbConnection() {
        return dbConnection;
    }
}

