package server;

import server.gazepoint.api.recv.RecXmlObject;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class GazeBuffer {
    final LinkedList<RecXmlObject> gazeDataQueue = new LinkedList<>();
    private ReentrantLock reentrantLock;

    public GazeBuffer() {
        this.reentrantLock = new ReentrantLock();
    }

    public void write(RecXmlObject xmlObject) {
        synchronized (gazeDataQueue) {
            this.reentrantLock.lock();
            gazeDataQueue.add(xmlObject);
            this.reentrantLock.unlock();
            this.gazeDataQueue.notify();
        }
    }

    public RecXmlObject read() {
        RecXmlObject recXmlObject = null;
        try {
            synchronized (this.gazeDataQueue) {
                System.out.println("Acquiring lock readGzeDataFromBuffer");
                this.reentrantLock.lock();

                //queue is empty, release locka dn wait
                if (gazeDataQueue.isEmpty()) {
                    this.reentrantLock.unlock();
                    this.gazeDataQueue.wait();
                    this.reentrantLock.lock();
                }
                recXmlObject = gazeDataQueue.removeFirst();
                this.reentrantLock.unlock();
                System.out.println("releasing lock... readGazeDataFromBuffer");
            }
        } catch (InterruptedException e) {
            return null;
        }
        return recXmlObject;
    }

    public int size() {
        return this.gazeDataQueue.size();
    }
}
