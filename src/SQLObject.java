import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class SQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    protected static void fetchAllObjectsOfClassInBackground(Class<? extends SQLObject> SQLObjectSubclass, DatabaseManager.QueryCompletionHandler handler) {
        DatabaseManager.fetchAllRowsForTableInBackground(getSQLTableName(SQLObjectSubclass), handler);
    }

    public void save(DatabaseManager.SaveCompletionHandler handler) {
        if (hasChanges()) {
            DatabaseManager.updateFieldsForRecord(getSQLTableName(), getIDColumnName(), getID(), changes(), handler);
        } else {
            handler.succeeded();
        }
    }

    public static String getSQLTableName(Class<? extends  SQLObject> SQLObjectSubclass) { return SQLObjectSubclass.getName() + "s"; }

    public String getSQLTableName() { return getSQLTableName(this.getClass()); }

    abstract public HashMap<String, Object> changes();
    abstract public Boolean hasChanges();

    abstract public String getIDColumnName();
    abstract public int getID();

}