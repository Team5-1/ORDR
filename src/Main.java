import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by kylejm on 11/02/15.
 */
public class Main {
    private static final LinkedBlockingQueue<Runnable> mainThreadQueue = new LinkedBlockingQueue<Runnable>();

    public static void main(String[] args) {
        ApplicationManager.applicationDidLaunch();

        while (true) {
            try {
                mainThreadQueue.take().run();
            } catch (InterruptedException e) {
                //TODO: handler exception
            }
        }
    }

    public static void addToMainQueue(Runnable r) {
        mainThreadQueue.add(r);
    }
}
