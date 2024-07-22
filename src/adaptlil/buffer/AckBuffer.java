package adaptlil.buffer;

import adaptlil.gazepoint.api.ack.AckXmlObject;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Similar to the GazeBuffer it holds
 */
public class AckBuffer {
    final LinkedList<AckXmlObject> ackDataQueue = new LinkedList<>();
    private ReentrantLock reentrantLock;

    public AckBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    public void write(AckXmlObject xmlObject) {
        synchronized (ackDataQueue) {
            this.reentrantLock.lock();
            ackDataQueue.add(xmlObject);
            this.reentrantLock.unlock();
            this.ackDataQueue.notify();
        }
    }

    public AckXmlObject read() {
        AckXmlObject ackXmlObject;
        try {
            synchronized (this.ackDataQueue) {
                this.reentrantLock.lock();

                //queue is empty, release lock and wait
                if (ackDataQueue.isEmpty()) {
                    this.reentrantLock.unlock();
                    this.ackDataQueue.wait();
                    this.reentrantLock.lock();
                }
                ackXmlObject = ackDataQueue.removeFirst();
                this.reentrantLock.unlock();
            }
        } catch (InterruptedException e) {
            return null;
        }
        return ackXmlObject;
    }
}
