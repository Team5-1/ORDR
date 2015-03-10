
/**
 * Created by kylejm on 12/02/15.
 */
public class MainRunnableTask implements Runnable{
    private final Runnable task;
    private final Runnable callback;


    public MainRunnableTask(Runnable task, Runnable callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    public void run() {
        task.run();
        Main.addToMainQueue(callback);
    }
}
