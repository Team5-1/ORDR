import javax.swing.plaf.nimbus.State;
import javax.xml.crypto.Data;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ResultSet fetchAllRowsForTable(String tableName) throws SQLException {
        java.sql.Statement stm = dbConnection.createStatement();
        ResultSet results = stm.executeQuery("SELECT * FROM " + tableName);
        return results;
    }

    private void handleExcetion(SQLException e) {
        System.out.println("ERROR: MySQL Connection Failed!");
        e.printStackTrace();
    }
}

