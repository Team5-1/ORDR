import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by kylejm on 24/01/15.
 */
public class DatabaseManager {
    static final DatabaseManager sharedManager = new DatabaseManager();
    //TODO handlers should get calledBack on a different thread to the background thread

    private Connection dbConnection;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // initialise the JDBC driver, with a check for it working
            this.dbConnection = DriverManager.getConnection(DatabaseCredentials.localURL, DatabaseCredentials.localUsername, DatabaseCredentials.localPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found; is your CLASSPATH set?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    //Inserting
    public static void createRecordInBackground(final HashMap<String, Object> columnsAndValues, final String tableName, final CreateCompletionHandler handler) {
        //Create statement callable
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String insert = "INSERT INTO " + tableName;
                String cols = " (";
                String values = " VALUES (";
                int columnCount = columnsAndValues.size();
                int i = 1;
                ArrayList<Object> orderedValues = new ArrayList<Object>(columnCount);
                for (String key : columnsAndValues.keySet()) {
                    cols += key;
                    if (i == columnCount) {
                        cols += ") ";
                        values += "?)";
                    } else {
                        cols += ", ";
                        values += "?, ";
                    }
                    orderedValues.add(columnsAndValues.get(key));
                    i++;
                }
                String stmString =  insert + cols + values;
                ResultSet insertedRecordID = null;
                PreparedStatement stm = getSharedDbConnection().prepareStatement(stmString);
                for (i = 0; i < orderedValues.size(); i++) {
                    stm.setObject(i + 1, orderedValues.get(i));
                }
                stm.execute(); //Execute insert. Throws exception and stops here if issue
                insertedRecordID =  getSharedDbConnection().prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
                return insertedRecordID; //If return null, the callback won't get called
            }
        };

        QueryCompletionHandler callback = new QueryCompletionHandler() {
            @Override
            public void succeeded(ResultSet results) throws SQLException {
                handler.succeeded(results.getInt(1));
            }

            @Override
            public void failed(SQLException exception) {
                handler.failed(exception);
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, callback));
    }


    //Querying
    public static void fetchAllRecordsForTableInBackground(final String tableName, final QueryCompletionHandler handler) {
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                return  getSharedDbConnection().prepareStatement("SELECT * FROM " + tableName).executeQuery();
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, handler));
    }

    public static void fetchAllFieldsForMatchingRecordsInBackground(final HashMap<String, Object> fieldsAndValuesToMatch, final String tableName, final QueryCompletionHandler handler) {
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String stmString = String.format("SELECT * FROM %s WHERE ", tableName);
                int fieldCount = fieldsAndValuesToMatch.size();
                int i = 1;
                ArrayList<Object> values = new ArrayList<Object>(fieldCount);
                for (String key : fieldsAndValuesToMatch.keySet()) {
                    stmString += key + " = ?";
                    if (i != fieldCount) stmString +=  " AND ";
                    values.add(fieldsAndValuesToMatch.get(key));
                    i++;
                }
                PreparedStatement stm = getSharedDbConnection().prepareStatement(stmString);
                for (i = 0; i < values.size(); i++) {
                    stm.setObject(i + 1, values.get(i));
                }
                return stm.executeQuery();
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, handler));
    }

    //TODO: this below method might not be needed. Just always fetch all... there aren't that many columns in records
    public static void fetchSpecifiedFieldsForAllRecordsInBackground(final ArrayList<String> fields, final String tableName, final QueryCompletionHandler handler) {
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String stmString = "SELECT ";
                int fieldCount = fields.size();
                int i = 1;
                for (String field : fields) {
                    stmString += (i == fieldCount) ? field + " " : field + ", ";
                    i++;
                }
                stmString += "FROM " + tableName;
                return sharedManager.dbConnection.prepareStatement(stmString).executeQuery();
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, handler));
    };

    //TODO: This method might not be needed. SELECT LAST_INSERT_ID() is used instead to get the ID of a newly created record
    public static void fetchSpecifiedFieldsForMatchingRecordsInBackground(final ArrayList<String> fieldsToFetch, final HashMap<String, Object> fieldsAndValuesToMatch, final String tableName, final QueryCompletionHandler handler) {
        SQLQueryTask.SQLQueryCall query = new SQLQueryTask.SQLQueryCall() {
            @Override
            public ResultSet call() throws SQLException {
                String stmString = "SELECT ";
                int fieldCount = fieldsToFetch.size();
                int i = 1;
                for (String field : fieldsToFetch) {
                    stmString += (i == fieldCount) ? field + " " : field + ", ";
                    i++;
                }
                stmString += "FROM " + tableName + " WHERE ";
                fieldCount = fieldsAndValuesToMatch.size();
                i = 1;
                ArrayList<Object> values = new ArrayList<Object>(fieldCount);
                for (String key : fieldsAndValuesToMatch.keySet()) {
                    stmString += key + " = ?";
                    if (i != fieldCount) stmString +=  " AND ";
                    values.add(fieldsAndValuesToMatch.get(key));
                    i++;
                }
                PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                for (i = 0; i < values.size(); i++) {
                    stm.setObject(i + 1, values.get(i));
                }
                return stm.executeQuery();
            }
        };

        BackgroundQueue.addToQueue(new SQLQueryTask(query, handler));
    }


    public static void updateFieldsForRecord(final String tableName, final String recordIDColumnName, final int recordID, final HashMap<String, Object> columnsAndValues, final SaveOrDeleteCompletionHandler handler) {
        Runnable query = new Runnable() {
            @Override
            public void run() {
                String stmString = "UPDATE " + tableName + " SET ";
                ArrayList<Object> values = new ArrayList<Object>(columnsAndValues.size());
                for (String key : columnsAndValues.keySet()) {
                    stmString +=  key + " = ?, ";
                    values.add(columnsAndValues.get(key));
                }
                stmString += "date_updated = NOW() WHERE " + recordIDColumnName + " = " + String.format("%d", recordID);
                try {
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    for (int i = 0; i < values.size(); i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    stm.executeUpdate();
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        };

        Runnable callback = new Runnable() {
            @Override
            public void run() {
                handler.succeeded();
            }
        };

        BackgroundQueue.addToQueue(new MainRunnableTask(query, callback));
    }


    //Deleting
    public static void deleteSQLObject(final SQLObject object, final SaveOrDeleteCompletionHandler handler) {
        Runnable delete = new Runnable() {
            @Override
            public void run() {
                try {
                    String stmString = String.format("DELETE FROM %s WHERE %s = ?", object.getSQLTableName(), object.getIDColumnName());
                    PreparedStatement stm = getSharedDbConnection().prepareStatement(stmString);
                    stm.setObject(1, object.getID());
                    stm.executeUpdate();
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        };

        Runnable callback = new Runnable() {
            @Override
            public void run() {
                handler.succeeded();
            }
        };

        BackgroundQueue.addToQueue(new MainRunnableTask(delete, callback));
    }

    public static void deleteSQLObjects(final Collection<? extends  SQLObject> objects, final SaveOrDeleteCompletionHandler handler) {
        Runnable delete = new Runnable() {
            @Override
            public void run() {
                try {
                    for (SQLObject object : objects)  {
                        String stmString = String.format("DELETE FROM %s WHERE %s = ?", object.getSQLTableName(), object.getIDColumnName());
                        PreparedStatement stm = getSharedDbConnection().prepareStatement(stmString);
                        stm.setObject(1, object.getID());
                        stm.executeUpdate();
                    }
                } catch (SQLException e) {
                    handler.failed(e);
                }
            }
        };

        Runnable callback = new Runnable() {
            @Override
            public void run() {
                handler.succeeded();
            }
        };

        BackgroundQueue.addToQueue(new MainRunnableTask(delete, callback));
    }

    // All callback completion handlers will be executed on the main thread
    // TODO: make it so that the handlers get called back on the thread the call was made on
    public  interface SQLExceptionHandler {
        public void failed(SQLException exception);
    }

    public static abstract class SQLExceptionHandlerWrapper implements SQLExceptionHandler {
        public final SQLExceptionHandler exceptionHandler;

        public SQLExceptionHandlerWrapper(SQLExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
        }

        @Override
        public void failed(SQLException exception) {
            exceptionHandler.failed(exception);
        }
    }

    //Create handler
    public interface CreateCompletionHandler extends SQLExceptionHandler {
        abstract public void succeeded(int ID);
    }

    public static abstract class CreateSuccessHandler extends SQLExceptionHandlerWrapper implements CreateCompletionHandler {
        public CreateSuccessHandler(SQLExceptionHandler exceptionHandler) {
            super(exceptionHandler);
        }
    }

    //Query handlers
    public interface QueryCompletionHandler extends SQLExceptionHandler {
        abstract public void succeeded(ResultSet results) throws SQLException;
    }

    public static abstract class QuerySuccessHandler extends SQLExceptionHandlerWrapper implements QueryCompletionHandler {
        public QuerySuccessHandler(SQLExceptionHandler exceptionHandler) {
            super(exceptionHandler);
        }
    }

    //Saving handlers
    public interface SaveOrDeleteCompletionHandler extends SQLExceptionHandler {
        public void succeeded();
    }

    public static abstract class SaveOrDeleteSuccessHandler extends SQLExceptionHandlerWrapper implements SaveOrDeleteCompletionHandler {
        public SaveOrDeleteSuccessHandler(SQLExceptionHandler exceptionHandler) {
            super(exceptionHandler);
        }
    }


    //Accessor method

    public static Connection getSharedDbConnection() {
        return sharedManager.dbConnection;
    }
}

