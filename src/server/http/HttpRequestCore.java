package server.http;

import com.fasterxml.jackson.core.util.JacksonFeature;
import server.http.request.RequestModelHttp;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class HttpRequestCore {
    public static <T> Response POST(String url, Entity<T> body) {

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Create a WebTarget to send a request at
        System.out.println("Creating webTarget");
        WebTarget webTarget = client.target(url);

        // Create an InvocationBuilder to create the HTTP request
        System.out.println("building invocationbuilder");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Send the request and save it to a Response
        System.out.println("Sending request...");
        Response response = invocationBuilder.post(body);
        System.out.println("Sent!");

        return response;
    }

    /**
     * TODO - Build GET request using webTarget and serialization of response models.
     *
     * @param url
     * @param request
     * @return
     */
    public static Response GET(String url, RequestModelHttp request) {

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Create a WebTarget to send a request at
        System.out.println("Creating webTarget");
        WebTarget webTarget = client.target(url);

        // Create an InvocationBuilder to create the HTTP request
        System.out.println("building invocationbuilder");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Send the request and save it to a Response
        System.out.println("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
        System.out.println("Sent!");

        return response;
    }
}
