package adaptlil.buffer;

import adaptlil.gazepoint.api.recv.RecXml;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Asynchronous Buffer that takes in gaze data packets
 */
public class GazeBuffer extends AsyncBuffer {
    private final LinkedList<RecXml> gazeDataQueue = new LinkedList<>();

    private ReentrantLock reentrantLock;

    public GazeBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    /**
     * Thread safe, write/pushes the xmlObject param to the back of the queue.
     * @param xmlObject
     */
    public void write(RecXml xmlObject) {
        synchronized (gazeDataQueue) {
            this.reentrantLock.lock();
            gazeDataQueue.add(xmlObject);
            this.reentrantLock.unlock();
            this.gazeDataQueue.notify();
        }
    }

    /**
     * Thread safe read, removes the top of queue
     * @return
     */
    public RecXml read() {
        RecXml recXml;
        try {
            synchronized (this.gazeDataQueue) {
                this.reentrantLock.lock();

                //queue is empty, release lock and wait for next gaze data packet
                if (gazeDataQueue.isEmpty()) {
                    this.reentrantLock.unlock();
                    this.gazeDataQueue.wait();
                    this.reentrantLock.lock();
                }
                try {
                    recXml = gazeDataQueue.removeFirst();
                } catch (Exception e) {
                    recXml = new RecXml();
                    recXml.BPOGV = false;
                }
                this.reentrantLock.unlock();
            }
        } catch (InterruptedException e) {
            return null;
        }
        return recXml;
    }

    /**
     * Flush the queue
     */
    public void flush() {
        synchronized(this.gazeDataQueue) {
            this.reentrantLock.lock();
            gazeDataQueue.clear();
            this.reentrantLock.unlock();
        }
    }

    public int size() {
        synchronized (this.gazeDataQueue) {
            this.reentrantLock.lock();
            int size = this.gazeDataQueue.size();
            this.reentrantLock.unlock();
            return size;
        }
    }
}
