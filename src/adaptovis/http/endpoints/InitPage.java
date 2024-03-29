package adaptovis.http.endpoints;

import adaptovis.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import adaptovis.http.request.AckServerStartRequestModel;
import adaptovis.http.response.ResponseModelHttp;

@Path("init")
public class InitPage {

    /**
     * Used to init load model, websocket, adaptation mediator, gaze window, and gp3 socket after the keras server has finished starting.
     * @param headers
     * @param jsonText
     * @return
     */
    @Path("ackKerasServer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ack(@Context HttpHeaders headers, String jsonText)
    {
        int resultCode;
        String message;
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("ack started here");
        try {
            AckServerStartRequestModel request = mapper.readValue(jsonText, AckServerStartRequestModel.class);

            if (request.getResultCode() == 1000) {
                System.out.println(request.getMessage());
                System.out.println("sync mainthreadlock");
                synchronized (Main.mainThreadLock) {
                    System.out.println("notifying");
                    Main.hasKerasServerAckd = true;
                    Main.mainThreadLock.notifyAll();
                }
                System.out.println("returning");
                return Response.status(200).entity(new ResponseModelHttp(1000, "received your ACK and have successfully started remaining components of IAV")).build();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Response.status(500).entity(new ResponseModelHttp(1500)).build();



    }
}
