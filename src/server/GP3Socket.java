package server;

import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

    private ArrayBlockingQueue<RecXmlObject> gazeDataQueue;
    private final int windowSize = 60;
    public GP3Socket() {
        xmlMapper = new XmlMapper();
        gazeDataQueue = new ArrayBlockingQueue<>(windowSize);
    }
    public void connect() throws IOException {

        socket = new Socket(hostName, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintStream(socket.getOutputStream());

    }

    public void start() throws JsonProcessingException {
        Get_Calibrate_Start calibrateStart = new Get_Calibrate_Start();
        output.println(xmlMapper.writeValueAsString(calibrateStart));
    }

    /**
     * I sugges using this method in an asynchronous fashion to let our analytics run ML while simulatenously pulling data.
     * @throws IOException
     */
    public void startGazeDataStream() throws IOException {
        Set_Enable_Send_Data enableSendData = new Set_Enable_Send_Data(true);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        //Read ACK
        String ack = input.readLine();

        //Gaze is now sending data in form of REC

        while(!isWritingToGazeBuffer) {
            isWritingToGazeBuffer = true;

        }

    }

    public void stopGazeDataStream() throws IOException {
        Set_Enable_Send_Data enableSendData = new Set_Enable_Send_Data(false);
        output.println(xmlMapper.writeValueAsString(enableSendData));
        String ack = input.readLine();
    }

}
