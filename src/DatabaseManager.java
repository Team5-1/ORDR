import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by kylejm on 24/01/15.
 */
public class DatabaseManager {
    static final DatabaseManager sharedManager = new DatabaseManager();

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

    public static  void fetchAllRowsForTableInBackground(final String tableName, final SQLQueryCompletionHandler handler) {
        BackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    java.sql.Statement stm = sharedManager.dbConnection.createStatement();
                    ResultSet results = stm.executeQuery("SELECT * FROM " + tableName);
                    handler.succeeded(results);
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
                    int i = 0;
                    int keyValueQty = columnsAndValues.size();
                    HashMap<Integer, Object> keyValueOrder = new HashMap<Integer, Object>(columnsAndValues.size());
                    for (String key : columnsAndValues.keySet()) {
                        stmString = stmString + key + " = ?";
                        if (i > 0 && i != keyValueQty - 1) stmString = stmString + ",";
                        stmString = stmString + " ";
                        keyValueOrder.put(i, columnsAndValues.get(key));
                        i++;
                    }
                    stmString = stmString + "WHERE " + recordKeyName + " = " + String.format("%d", recordKey);
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    for (i = 1; i == keyValueQty; i++) {
                        stm.setObject(i, keyValueOrder.get(i - 1));
                    }
                    stm.executeUpdate();
                    handler.succeeded();
                } catch (SQLException e) {
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
}

