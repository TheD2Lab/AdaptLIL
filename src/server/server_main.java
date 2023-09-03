package server;
import java.io.IOException;

public class server_main {

    public static void main(String[] args) {

        System.out.println("Beginning GP3 Real-Time Prototype Stream");
        GP3Socket gp3Socket = new GP3Socket();
        try {
            gp3Socket.connect();
            System.out.println("Connected to GP3");
            System.out.println("Starting Data Stream via thread");
            gp3Socket.startGazeDataStream();
            System.out.println("Started gaze data stream.");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
