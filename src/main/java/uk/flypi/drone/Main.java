package uk.flypi.drone;

import uk.flypi.drone.http.HttpServer;

public class Main {

    public static void main(final String[] args) {
        System.out.println("starting up.\nPlease wait...");
        final HttpServer httpServer = new HttpServer();
        httpServer.start();
        System.out.println("web server started");
    }
}
