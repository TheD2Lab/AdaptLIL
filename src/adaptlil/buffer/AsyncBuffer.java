package adaptlil.buffer;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncBuffer<T> {
    final LinkedList<T> dataQueue = new LinkedList<>();
    private ReentrantLock reentrantLock;

    public AsyncBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    /**
     * Uses and entrant lock and blocks the main thread from writing until the lock is released
     * @param data
     */
    public void write(T data) {
        synchronized (dataQueue) {
            this.reentrantLock.lock();
            dataQueue.add(data);
            this.reentrantLock.unlock();
            this.dataQueue.notify();
        }
    }

    /**
     * Blocks the main thread from reading until the lock is released.
     * @return
     */
    public T read() {
        T dataFromQueue;
        try {
            synchronized (this.dataQueue) {
                this.reentrantLock.lock();

                //queue is empty, release lock and wait
                if (dataQueue.isEmpty()) {
                    this.reentrantLock.unlock();
                    this.dataQueue.wait();
                    this.reentrantLock.lock();
                }
                dataFromQueue = dataQueue.removeFirst();
                this.reentrantLock.unlock();
            }
        } catch (InterruptedException e) {
            return null;
        }
        return dataFromQueue;
    }

    public boolean isEmpty() {
        return this.dataQueue.isEmpty();
    }
}
