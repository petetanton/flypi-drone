package uk.flypi.drone;

import com.pi4j.io.gpio.GpioFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.flypi.drone.http.HttpServer;
import uk.flypi.drone.instruments.InstrumentWatcher;
import uk.flypi.drone.instruments.Sonar;

import java.util.concurrent.TimeoutException;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) throws TimeoutException, InterruptedException {
        LOG.info("warming up instruments...");
        InstrumentWatcher iw = new InstrumentWatcher(250);
        iw.addInstrumentToWatcher("sonar-down", new Sonar(GpioFactory.getInstance()));
        LOG.info("instruments warmed up");
        LOG.info("Starting up please wait...");
        final HttpServer httpServer = new HttpServer();
        httpServer.start();
        LOG.info("web server started");

    }

}
