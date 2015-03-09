import java.util.concurrent.ConcurrentLinkedDeque;

//TODO: should perhaps move this in to DatabaseManager as it's own queue?

/**
 * Created by kylejm on 26/01/15.
 */
public class BackgroundQueue implements Runnable {
    static final BackgroundQueue sharedBackgroundQueue = new BackgroundQueue();

    private final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<Runnable>();

    public BackgroundQueue() {
        Thread thread = new Thread(this, "SQL Serial Background Thread");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    //TODO: handle this error
                }
            }
        }
    }

    public static void addToQueue(Runnable runnable) {
        sharedBackgroundQueue.queue.offer(runnable);
        synchronized (sharedBackgroundQueue.queue) {
            sharedBackgroundQueue.queue.notifyAll(); //Next TODO: Is this right thread being paused here? This will be easier to test with GUI because we can call from the events loop
        }
    }
}
