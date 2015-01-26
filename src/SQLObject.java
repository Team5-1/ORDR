import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class SQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    protected static ResultSet fetchAllObjectsOfClass(Class<? extends SQLObject> classEntity) {
        String className = classEntity.getName();
        try {
            return DatabaseManager.sharedManager.fetchAllRowsForTable(className + "s");
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    abstract public Boolean hasChanges();

}