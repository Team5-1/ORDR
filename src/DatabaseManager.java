import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kylejm on 24/01/15.
 */
public class DatabaseManager {
    static final DatabaseManager sharedManager = new DatabaseManager();

    private Connection dbConnection;

    public DatabaseManager() {
        try            // initialise the JDBC driver, with a check for it working
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e)
        {
            System.out.println("ERROR: MySQL JDBC Driver not found; is your CLASSPATH set?");
            e.printStackTrace();
            return;
        }
        try {
            this.dbConnection = DriverManager.getConnection(DatabaseCredentials.localURL, DatabaseCredentials.localUsername, DatabaseCredentials.localPassword);
        } catch (SQLException e) {
            System.out.println("ERROR: MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    public void fetchAllRowsForTableInBackground(final String tableName, final SQLCompletionHandler handler) {
        BackgroundQueue.sharedBackgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    java.sql.Statement stm = dbConnection.createStatement();
                    ResultSet results = stm.executeQuery("SELECT * FROM " + tableName);
                    handler.succeeded(results);
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

    public interface SQLCompletionHandler {
        public void succeeded(ResultSet results);
        public void failed(SQLException exception);
    }
}

