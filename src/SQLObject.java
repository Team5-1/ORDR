import java.sql.SQLException;

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

    //NEXT TODO: add method that returns hashmap of changes and column names
    abstract public void save(DatabaseManager.SQLSaveCompletionHandler handler);
    abstract public Boolean hasChanges();

}