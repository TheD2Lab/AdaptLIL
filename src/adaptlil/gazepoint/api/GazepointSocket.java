package adaptlil.gazepoint.api;

import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import adaptlil.mediator.AdaptationMediator;
import adaptlil.mediator.Component;
import adaptlil.mediator.Mediator;
import adaptlil.buffer.AckBuffer;
import adaptlil.buffer.GazeBuffer;
import adaptlil.gazepoint.api.ack.AckXmlObject;
import adaptlil.gazepoint.api.get.GetCommand;
import adaptlil.gazepoint.api.recv.RecXmlObject;
import adaptlil.gazepoint.api.set.SetCommand;
import adaptlil.gazepoint.api.set.SetEnableSendCommand;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * Connects to GP3 and reads data.
 * Utilizes a thread safe queue to write/read data recieved from the tracker. API specifies that data is sent in the form of XML
 * so we utilize XML Annotations to construct XML objects after receiving API data. This is to simplify the process of
 * interacting with the tracking data.
 */
@ServerEndpoint("/gp3connection")
public class GazepointSocket implements Component {

    private String hostName;
    private int port;
    private Socket socket;

    private BufferedReader input;
    private PrintStream output;

    private final XmlMapper xmlMapper;

    private Thread gazeBufferThread;


    /**
     * We use a FIFO queue to handle the gaze data being sent to preserve the correct order. if a FILO queue is used
     * the data will be out of order and you'll only be reading the most recent data.
     */
    private final int windowSize = 60;

    private final GazeBuffer gazeDataBuffer;
    private AckBuffer ackBuffer;
    private final LinkedList<RecXmlObject> gazeDataQueue = new LinkedList<>();
    private FileInputStream testFileReader;
    private AdaptationMediator mediator;

    public GazepointSocket(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        xmlMapper = new XmlMapper();
        gazeDataBuffer = new GazeBuffer();
        ackBuffer = new AckBuffer();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.gazeBufferThread = new Thread(new DataStreamRunnable(this));

    }

    /**
     * Opens socket port to the gazepoint tracker and sets the input, output streams
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

        //Gaze is now sending data in form of REC, begin write runnable
        this.gazeBufferThread.start();

    }

    private void writeToGazeBuffer() throws IOException, InterruptedException {

        String msg = input.readLine();
        while (msg != null) {
            //Offer data to queue, block if queue is being used.
            XmlObject command = GazeApiCommands.mapToXmlObject(msg);
            if (GazeApiCommands.mapToXmlObject(msg) != null) {
                if (command instanceof AckXmlObject) {

                    this.ackBuffer.write((AckXmlObject) command);
                }
                else if (command instanceof RecXmlObject) {

                    this.gazeDataBuffer.write((RecXmlObject) command);
                }
            } else
                System.err.println("failed to write to datapacket to buffer");

            msg = input.readLine();

        }

        System.out.println("msg line was null, finished writing to buffer.");
    }

    /**
     * Grabs the head of the gaze data xml object queue (Reference gazepoint API)
     * @return Returns the XML Data Object that details whatever GazeData has been sent from the tracker
     */
    public RecXmlObject readGazeDataFromBuffer() {
        return gazeDataBuffer.read();
    }

    /**
     * Pauses the data stream by sending the ENABLE_SEND_DATA command w/ false flag.
     * @return Returns the ACK Xml from the server.
     * @throws IOException If this is thrown, it may be a logic issue where we are reading the wrong input line.
     */
    public AckXmlObject stopGazeDataStream() throws IOException {
        SetEnableSendCommand enableSendData = new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DATA, false);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        return xmlMapper.readValue(input.readLine(), AckXmlObject.class);
    }

    public GazeBuffer getGazeDataQueue() {
        return this.gazeDataBuffer;
    }

    public void write(String msg) {
        output.println(msg);
    }

    public void writeSetCommand(SetCommand setCommand) throws JsonProcessingException {
        this.write(xmlMapper.writeValueAsString(setCommand));

    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (AdaptationMediator) mediator;
    }

    private class DataStreamRunnable implements Runnable {

        private final GazepointSocket gazepointSocket;
        DataStreamRunnable(GazepointSocket gazepointSocket) {
            this.gazepointSocket = gazepointSocket;
        }
        @Override
        public void run() {
            try {
                this.gazepointSocket.writeToGazeBuffer();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
