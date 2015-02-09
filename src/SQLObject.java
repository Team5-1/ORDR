import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class SQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    protected static void fetchAllObjectsOfClassInBackground(Class<? extends SQLObject> classEntity, DatabaseManager.SQLQueryCompletionHandler handler) {
        String tableName = classEntity.getName() + "s";
        DatabaseManager.fetchAllRowsForTableInBackground(tableName, handler);
    }

    public void save(DatabaseManager.SQLSaveCompletionHandler handler) {
        if (hasChanges()) {
            String tableName = getClass().getName() + "s";
            DatabaseManager.updateFieldsForRecord(tableName, getIDColumnName(), getID(), changes(), handler);
        } else {
            handler.succeeded();
        }
    }

    abstract public HashMap<String, Object> changes();
    abstract public Boolean hasChanges();

    abstract public String getIDColumnName();
    abstract public int getID();

}