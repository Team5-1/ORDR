import java.util.concurrent.Callable;

/**
 * Created by kylejm on 11/02/15.
 */
public class MainCallback<T> implements Runnable {
    private final Callable<T> task;
    private final BackgroundCallback<T> callback;


    public MainCallback(Callable<T> task, BackgroundCallback<T> callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final T returnedObject = task.call();
            if (returnedObject != null) {
                Main.addToMainQueue(new Runnable() {
                    @Override
                    public void run() {
                        callback.complete(returnedObject);
                    }
                });
            }
        } catch (final Exception e) {
            Main.addToMainQueue(new Runnable() {
                @Override
                public void run() {
                    callback.failed(e);
                }
            });
        }
    }

    public interface BackgroundCallback<T> {
        public void complete(T object);
        public void failed(Exception exception);
    }
}
