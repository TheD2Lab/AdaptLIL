package server;

import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Link;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.gazepoint.api.XmlObject;
import server.gazepoint.api.ack.AckXmlObject;
import server.gazepoint.api.get.GetCommand;
import server.gazepoint.api.recv.RecXmlObject;
import server.gazepoint.api.set.SetCommand;
import server.gazepoint.api.set.SetEnableSendCommand;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * Connects to GP3 and reads data.
 * Utilizes a thread safe queue to write/read data recieved from the tracker. API specifies that data is sent in the form of XML
 * so we utilize XML Annotations to construct XML objects after receiving API data. This is to simplify the process of
 * interacting with the tracking data.
 */
@ServerEndpoint("/gp3connection")
public class GP3Socket {

    private String hostName = "localhost";
    private int port = 4242;
    private Socket socket;

    private BufferedReader input;
    private PrintStream output;

    private final XmlMapper xmlMapper;
    private boolean isWritingToGazeBuffer = false;
    private boolean isReading = false;
    private ReentrantLock recQueueLock = new ReentrantLock();
    private ReentrantLock ackQueueLock = new ReentrantLock();


    /**
     * We use a FIFO queue to handle the gaze data being sent to preserve the correct order. if a FILO queue is used
     * the data will be out of order and you'll only be reading the most recent data.
     */
    private final int windowSize = 60;
    private final LinkedList<RecXmlObject> gazeDataQueue = new LinkedList<>();
    private final LinkedList<AckXmlObject> ackDataQueue = new LinkedList<>();
    private FileInputStream testFileReader;

    public GP3Socket() {
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    /**
     * Opens socket port to the gazepoint tracker
     * @throws IOException
     */
    public void connect() throws IOException {
        //socket = new Socket(hostName, port);
        output = new PrintStream(new FileOutputStream("output_stream_test.txt"));
        testFileReader =  new FileInputStream("rec_command_test_3.txt");
        input = new BufferedReader(new InputStreamReader(testFileReader));
        //input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //output = new PrintStream(socket.getOutputStream());

    }

    /**
     * Initiates calibration on the tracker.
     * @throws JsonProcessingException
     */
    public void startCalibration() throws JsonProcessingException {
        GetCommand calibrateStart = new GetCommand(GazeApiCommands.CALIBRATE_START);
        output.println(xmlMapper.writeValueAsString(calibrateStart));
    }

    /**
     * Initiates ENABLE_SEND_DATA with the tracker to begin gaze data stream.
     * This method creates a new thread so be careful. The thread will write data into the gaze buffer.
     * @throws IOException
     */
    public void startGazeDataStream() throws IOException {
        SetEnableSendCommand setEnableSendCommand = new SetEnableSendCommand("ENABLE_SEND_DATA", true);

        output.println(xmlMapper.writeValueAsString(setEnableSendCommand));
        //Read ACK
        String ack = input.readLine();
        System.out.println("ACK: " + ack);
        //Gaze is now sending data in form of REC, begin write runnable
        Thread gazeBufferThread = new Thread(new DataStreamRunnable(this));
        gazeBufferThread.start();

    }

    private void writeToGazeBuffer() throws IOException, InterruptedException {

        String msg = input.readLine();
//        input.mark(8193);
        while (msg != null) {

            //Only listen for REC
            //Offer data to queue, block if queue is being used.

            //Test to see if the command can map and assign it to the proper buffer.
            XmlObject command = GazeApiCommands.mapToXmlObject(msg);
            if (GazeApiCommands.mapToXmlObject(msg) != null) {
                if (command instanceof AckXmlObject) {
                    synchronized (ackDataQueue) {
                        System.out.println("acquiring ackQLock writeToGazeBuffer");
                        this.ackQueueLock.lock();
                        ackDataQueue.add((AckXmlObject) command);
                        this.ackQueueLock.unlock();
                        System.out.println("releasing ackQLock writeToGazeBuffer");
                    }
                }
                else if (command instanceof RecXmlObject) {
                    synchronized (gazeDataQueue) {
//                        System.out.println("acquiring recQLock writeToGazeBuffer");
                        this.recQueueLock.lock();
                        gazeDataQueue.add((RecXmlObject) command);
                        this.recQueueLock.unlock();

                        this.gazeDataQueue.notify();
//                        System.out.println("releasing recQLock writeToGazeBuffer");
                    }
                }

            } else
                System.out.println("failed to write to datapacket to buffer");

            msg = input.readLine();

            //FILE HANDLING ONLY
            if (msg == null) {
                //reset file position
                testFileReader.getChannel().position(0);
                System.out.println("reseting, input line should point to next now.");
                input = new BufferedReader(new InputStreamReader(testFileReader));
                msg = input.readLine();
//                input.mark(8193);

            }

//            System.out.println(msg);
        }
    }

    /**
     * Grabs the head of the gaze data xml object queue (Reference gazepoint API)
     * @return Returns the XML Data Object that details whatever GazeData has been sent from the tracker
     */
    public RecXmlObject readGazeDataFromBuffer() throws InterruptedException {
        RecXmlObject recXmlObject = null;
        synchronized (this.gazeDataQueue) {
            System.out.println("Acquiring lock readGzeDataFromBuffer");
            this.recQueueLock.lock();

            //queue is empty, release locka dn wait
            if (gazeDataQueue.isEmpty()) {
                this.recQueueLock.unlock();
                this.gazeDataQueue.wait();
                this.recQueueLock.lock();
            }
            recXmlObject = gazeDataQueue.removeFirst();
            this.recQueueLock.unlock();
            System.out.println("releasing lock... readGazeDataFromBuffer");
        }
        return recXmlObject;
    }

    /**
     * Pauses the data stream by sending the ENABLE_SEND_DATA command w/ false flag.
     * @return Returns the ACK Xml from the server.
     * @throws IOException If this is thrown, it may be a logic issue where we are reading the wrong input line.
     */
    public AckXmlObject stopGazeDataStream() throws IOException {
        SetEnableSendCommand enableSendData = new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DATA, false);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        this.isWritingToGazeBuffer = false;
        return xmlMapper.readValue(input.readLine(), AckXmlObject.class);
    }

    public LinkedList<RecXmlObject> getGazeDataQueue() {
        return gazeDataQueue;
    }

    public void write(String msg) {
        output.println(msg);
    }

    public void writeSetCommand(SetCommand setCommand) throws JsonProcessingException {
        this.write(xmlMapper.writeValueAsString(setCommand));

    }

    private class DataStreamRunnable implements Runnable {

        private final GP3Socket gp3Socket;
        DataStreamRunnable(GP3Socket gp3Socket) {
            this.gp3Socket = gp3Socket;
        }
        @Override
        public void run() {
            System.out.println("run method invoked");
            try {
//                MapWorld.debugFile.write("writing to gaze buffer");

                this.gp3Socket.writeToGazeBuffer();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
