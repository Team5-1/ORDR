import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by kylejm on 26/01/15.
 */
public class BackgroundQueue implements Runnable {
    static final BackgroundQueue sharedBackgroundQueue = new BackgroundQueue();

    private final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<Runnable>();

    public BackgroundQueue() {
        Thread thread = new Thread(this);
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

    public void addToQueue(Runnable runnable) {
        queue.offer(runnable);
        synchronized (queue) {
            queue.notifyAll();
        }
    }
}
