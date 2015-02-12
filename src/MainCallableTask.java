import java.util.concurrent.Callable;

/**
 * Created by kylejm on 11/02/15.
 */
public class MainCallableTask<T> implements Runnable {
    private final Callable<T> task;
    private final ReturnValueCallback<T> callback;


    public MainCallableTask(Callable<T> task, ReturnValueCallback<T> callback) {
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

    public interface ReturnValueCallback<T> {
        public void complete(T object);
        public void failed(Exception exception);
    }
}
