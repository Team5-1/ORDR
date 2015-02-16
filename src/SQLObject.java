import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by kylejm on 26/01/15.
 */
public abstract class SQLObject {

    protected static void handleSQLException(SQLException e) {
        //TODO: implement this
    }

    public void save(DatabaseManager.SaveCompletionHandler handler) {
        if (hasChanges()) {
            DatabaseManager.updateFieldsForRecord(getSQLTableName(), getIDColumnName(), getID(), changes(), handler);
        } else {
            handler.succeeded();
        }
    }

    public static String getSQLTableName(Class<? extends  SQLObject> SQLObjectSubclass) { return SQLObjectSubclass.getSimpleName() + "s"; }

    //TODO: Couldn't this be overridden in sublclasses so they pass themselves.class instead of passing in the class in every call
    public String getSQLTableName() { return getSQLTableName(this.getClass()); }

    abstract public HashMap<String, Object> changes();
    abstract public Boolean hasChanges();

    abstract public String getIDColumnName();
    abstract public int getID();

}