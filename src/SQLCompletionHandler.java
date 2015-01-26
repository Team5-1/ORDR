import java.nio.channels.CompletionHandler;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kylejm on 26/01/15.
 */
public interface SQLCompletionHandler {
    public void success(ResultSet results);
    public void failed(SQLException exception);
}
