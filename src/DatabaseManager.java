import javax.swing.plaf.nimbus.State;
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

    public ArrayList<Item> fetchAllItems() throws SQLException {
        java.sql.Statement stm = dbConnection.createStatement();
        ResultSet results = stm.executeQuery("SELECT * FROM Items");
        ArrayList<Item> items = new ArrayList<Item>(results.getFetchSize());
        while (results.next()) {
            items.add(new Item((String) results.getObject("name")));
        }
        return items;
    }

    private void handleExcetion(SQLException e) {
        System.out.println("ERROR: MySQL Connection Failed!");
        e.printStackTrace();
    }
}

