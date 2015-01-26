import java.sql.SQLException;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class SQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    protected static void fetchAllObjectsOfClassInBackground(Class<? extends SQLObject> classEntity, DatabaseManager.SQLCompletionHandler handler) {
        String className = classEntity.getName();
        DatabaseManager.sharedManager.fetchAllRowsForTableInBackground(className + "s", handler);
    }

    abstract public Boolean hasChanges();

}