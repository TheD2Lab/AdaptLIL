package adaptovis.buffer;

import adaptovis.gazepoint.api.recv.RecXmlObject;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class GazeBuffer extends AsyncBuffer {
    private final LinkedList<RecXmlObject> gazeDataQueue = new LinkedList<>();

    private ReentrantLock reentrantLock;

    public GazeBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    /**
     * Thread safe, write/pushes the xmlObject param to the back of the queue.
     * @param xmlObject
     */
    public void write(RecXmlObject xmlObject) {
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
    public RecXmlObject read() {
        RecXmlObject recXmlObject;
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
                    recXmlObject = gazeDataQueue.removeFirst();
                } catch (Exception e) {
                    recXmlObject = new RecXmlObject();
                    recXmlObject.BPOGV = false;
                }
                this.reentrantLock.unlock();
            }
        } catch (InterruptedException e) {
            return null;
        }
        return recXmlObject;
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
