import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class ORDRSQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    protected static ResultSet fetchAllObjectsOfClass(Class<? extends ORDRSQLObject> classEntity) {
        String className = classEntity.getName();
        try {
            return DatabaseManager.sharedManager.fetchAllRowsForTable(className + "s");
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

}
