package uk.flypi.drone;

import uk.flypi.drone.http.HttpServer;

import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(final String[] args) throws TimeoutException, InterruptedException {
        System.out.println("starting up.\nPlease wait...");
        final HttpServer httpServer = new HttpServer();
        httpServer.start();
        System.out.println("web server started");
    }

}
