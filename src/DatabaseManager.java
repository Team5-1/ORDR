import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

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

    public static void createRecordInBackground(final HashMap<String, Object> columnsAndValues, final String tableName, final CreateCompletionHandler handler) {
        //Create statement callable
        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
                String insert = "INSERT INTO " + tableName;
                String cols = " (";
                String values = " VALUES (";
                int columnCount = columnsAndValues.size();
                int i = 1;
                ArrayList<Object> orderedValues = new ArrayList<Object>(columnCount);
                for (String key : columnsAndValues.keySet()) {
                    cols += key;
                    if (i == columnCount) {
                        cols += " ";
                        values += "?)";
                    } else {
                        cols += ", ";
                        values += "?, ";
                    }
                    orderedValues.add(columnsAndValues.get(key));
                    i++;
                }
                String stmString =  insert + cols + values;
                ResultSet result = null;
                try {
                    PreparedStatement stm = getSharedDbConnection().prepareStatement(stmString);
                    stm.execute();
                    result =  getSharedDbConnection().prepareStatement("SELECT LAST_INSERT_ID()").executeQuery(); //Return results
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
                return result; //If we got here shit hit the fan and an e
            }
        };

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet results) {
                try {
                    handler.succeeded(results.getInt(1));
                } catch (SQLException e) {
                    handler.sqlException(e);
                }

            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };
        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    }

    public static void fetchAllRecordsForTableInBackground(final String tableName, final QueryCompletionHandler handler) {
        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
                return  getSharedDbConnection().prepareStatement("SELECT * FROM " + tableName).executeQuery();
            }
        };

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet results) {
                try {
                    if (results.next()) {
                        results.beforeFirst();
                        handler.succeeded(results);
                    } else {
                        handler.noResults();
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };
        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    }

    public static void fetchAllFieldsForMatchingRecordsInBackground(final HashMap<String, Object> fieldsAndValuesToMatch, final String tableName, final QueryCompletionHandler handler) {
        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
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

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet results) {
                try {
                    if (results.next()) {
                        results.beforeFirst();
                        handler.succeeded(results);
                    } else {
                        handler.noResults();
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };
        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    }

    //TODO: this below method might not be needed. Just always fetch all... there aren't that many columns in records
    public static void fetchSpecifiedFieldsForAllRecordsInBackground(final ArrayList<String> fields, final String tableName, final QueryCompletionHandler handler) {
        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
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

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet results) {
                try {
                    if (results.next()) {
                        results.beforeFirst();
                        handler.succeeded(results);
                    } else {
                        handler.noResults();
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };

        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    };

    //TODO: This method might not be needed. SELECT LAST_INSERT_ID() is used instead to get the ID of a newly created record
    public static void fetchSpecifiedFieldsForMatchingRecordsInBackground(final ArrayList<String> fieldsToFetch, final HashMap<String, Object> fieldsAndValuesToMatch, final String tableName, final QueryCompletionHandler handler) {
        Callable<ResultSet> query = new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
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

        MainCallableTask.ReturnValueCallback<ResultSet> callback = new MainCallableTask.ReturnValueCallback<ResultSet>() {
            @Override
            public void complete(ResultSet results) {
                try {
                    if (results.next()) {
                        results.beforeFirst();
                        handler.succeeded(results);
                    } else {
                        handler.noResults();;
                    }
                } catch (SQLException e) {
                    handler.sqlException(e);
                }
            }

            @Override
            public void failed(Exception exception) {
                handler.handleException(exception);
            }
        };

        BackgroundQueue.addToQueue(new MainCallableTask<ResultSet>(query, callback));
    }


    public static void updateFieldsForRecord(final String tableName, final String recordKeyName, final int recordKey, final HashMap<String, Object> columnsAndValues, final SaveCompletionHandler handler) {
        Runnable query = new Runnable() {
            @Override
            public void run() {
                String stmString = "UPDATE " + tableName + " SET ";
                ArrayList<Object> values = new ArrayList<Object>(columnsAndValues.size());
                for (String key : columnsAndValues.keySet()) {
                    stmString +=  key + " = ?, ";
                    values.add(columnsAndValues.get(key));
                }
                stmString += "date_updated = NOW() WHERE " + recordKeyName + " = " + String.format("%d", recordKey);
                try {
                    PreparedStatement stm = sharedManager.dbConnection.prepareStatement(stmString);
                    for (int i = 0; i < values.size(); i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    stm.executeUpdate();
                } catch (SQLException e) {
                    handler.sqlException(e);
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

    // All callback completion handlers will be executed on the main thread
    // TODO: make it so that the handlers get called back on the thread the call was made on
    public static abstract class SQLCompletionHandler {
        public void handleException(Exception e) {
            if (e.getClass() == SQLException.class) {
                sqlException((SQLException) e);
            } else {
                threadException(e);
            }
        }
        abstract public void sqlException(SQLException exception);
        public void threadException(Exception exception) {
            exception.printStackTrace();
            System.out.println(exception.getLocalizedMessage());
        }
    }

    public static abstract class CreateCompletionHandler extends SQLCompletionHandler {
        abstract public void succeeded(int ID);
    }

    public static abstract class QueryCompletionHandler extends SQLCompletionHandler {
        abstract public void succeeded(ResultSet results);
        abstract public void noResults();
    }

    public static abstract class SaveCompletionHandler extends SQLCompletionHandler {
        abstract public void succeeded();
    }


    //Accessor method

    public static Connection getSharedDbConnection() {
        return sharedManager.dbConnection;
    }
}

