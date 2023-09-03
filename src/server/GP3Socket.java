package server;

import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.ackcommands.AckXmlObject;
import server.getcommands.*;
import server.recvcommands.RecXmlObject;
import server.setcommands.Set_Enable_Send_Data;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * Connects to GP3 and reads data.
 */
@ServerEndpoint("/gp3connection")
public class GP3Socket {

    private String hostName = "localhost";
    private int port = 4242;
    private Socket socket;

    private BufferedReader input;
    private PrintStream output;

    private final XmlMapper xmlMapper;
    private volatile boolean isWritingToGazeBuffer = false;

    /**
     * We use a FIFO queue to handle the gaze data being sent to preserve the correct order. if a FILO queue is used
     * the data will be out of order and you'll only be reading the most recent data.
     */
    private final int windowSize = 60;
    private final ArrayBlockingQueue<RecXmlObject> gazeDataQueue = new ArrayBlockingQueue<>(windowSize);

    public GP3Socket() {
        xmlMapper = new XmlMapper();

    }

    /**
     * Opens socket port to the gazepoint tracker
     * @throws IOException
     */
    public void connect() throws IOException {

        socket = new Socket(hostName, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintStream(socket.getOutputStream());

    }

    /**
     * Initiates calibration on the tracker.
     * @throws JsonProcessingException
     */
    public void startCalibration() throws JsonProcessingException {
        Get_Calibrate_Start calibrateStart = new Get_Calibrate_Start();
        output.println(xmlMapper.writeValueAsString(calibrateStart));
    }

    /**
     * Initiates ENABLE_SEND_DATA with the tracker to begin gaze data stream.
     * This method creates a new thread so be careful. The thread will write data into the gaze buffer.
     * @throws IOException
     */
    public void startGazeDataStream() throws IOException {
        Set_Enable_Send_Data enableSendData = new Set_Enable_Send_Data(true);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        //Read ACK
        String ack = input.readLine();
        System.out.println("ACK: " + ack);
        //Gaze is now sending data in form of REC, begin write runnable
        System.out.println("Beginning datastream thread");
        DataStreamRunnable dataStreamRunnable = new DataStreamRunnable(this);
        dataStreamRunnable.run();
        System.out.println("Datastream runnable has started");
    }

    private void writeToGazeBuffer() throws IOException {
        while(!isWritingToGazeBuffer) {
            isWritingToGazeBuffer = true;

            //Offer data to queue, block if queue is being used.
            gazeDataQueue.offer(xmlMapper.readValue(input.readLine(), RecXmlObject.class));
        }
    }

    /**
     * Grabs the head of the gaze data xml object queue (Reference gazepoint API)
     * @return Returns the XML Data Object that details whatever GazeData has been sent from the tracker
     */
    public RecXmlObject readGazeDataFromBuffer() {
        return gazeDataQueue.poll();
    }

    /**
     * Pauses the data stream by sending the ENABLE_SEND_DATA command w/ false flag.
     * @return Returns the ACK Xml from the server.
     * @throws IOException If this is thrown, it may be a logic issue where we are reading the wrong input line.
     */
    public AckXmlObject stopGazeDataStream() throws IOException {
        Set_Enable_Send_Data enableSendData = new Set_Enable_Send_Data(false);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        return xmlMapper.readValue(input.readLine(), AckXmlObject.class);
    }

    private class DataStreamRunnable implements Runnable {

        private final GP3Socket gp3Socket;
        DataStreamRunnable(GP3Socket gp3Socket) {
            this.gp3Socket = gp3Socket;
        }
        @Override
        public void run() {
            try {
                this.gp3Socket.writeToGazeBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
