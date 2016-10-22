package uk.flypi.drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.flypi.drone.http.HttpServer;

import java.util.concurrent.TimeoutException;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) throws TimeoutException, InterruptedException {
        LOG.info("Starting up please wait...");
        final HttpServer httpServer = new HttpServer();
        httpServer.start();
        LOG.info("web server started");
    }

}
