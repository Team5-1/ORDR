import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kylejm on 12/03/15.
 */
public class SQLQueryTask implements Runnable {
    private final SQLQueryCall callable;
    private final DatabaseManager.QueryCompletionHandler callback;


    public SQLQueryTask(SQLQueryCall callable, DatabaseManager.QueryCompletionHandler callback) {
        this.callable = callable;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final ResultSet returnedObject = callable.call();
            if (returnedObject != null) {
                Main.addToMainQueue(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.succeeded(returnedObject);
                        } catch (SQLException e) {
                            callback.failed(e);
                        }
                    }
                });
            }
        } catch (final SQLException e) {
            Main.addToMainQueue(new Runnable() {
                @Override
                public void run() {
                    callback.failed(e);
                }
            });
        }
    }

    public interface SQLQueryCall {
        public ResultSet call() throws SQLException;
    }
}