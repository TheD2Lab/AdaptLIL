package adaptlil.buffer;

import adaptlil.gazepoint.api.ack.AckXml;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Similar to the GazeBuffer but it holds the acknowledgement packets/xml
 */
public class AckBuffer {
    final LinkedList<AckXml> ackDataQueue = new LinkedList<>();
    private ReentrantLock reentrantLock;

    public AckBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    public void write(AckXml xmlObject) {
        synchronized (ackDataQueue) {
            this.reentrantLock.lock();
            ackDataQueue.add(xmlObject);
            this.reentrantLock.unlock();
            this.ackDataQueue.notify();
        }
    }

    public AckXml read() {
        AckXml ackXml;
        try {
            synchronized (this.ackDataQueue) {
                this.reentrantLock.lock();

                //queue is empty, release lock and wait
                if (ackDataQueue.isEmpty()) {
                    this.reentrantLock.unlock();
                    this.ackDataQueue.wait();
                    this.reentrantLock.lock();
                }
                ackXml = ackDataQueue.removeFirst();
                this.reentrantLock.unlock();
            }
        } catch (InterruptedException e) {
            return null;
        }
        return ackXml;
    }
}
