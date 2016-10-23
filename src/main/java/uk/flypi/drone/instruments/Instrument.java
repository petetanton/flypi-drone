package uk.flypi.drone.instruments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class Instrument implements Runnable {
    private static final Logger LOG = LogManager.getLogger(Instrument.class);

    private Optional<Measurement> lastMeasurement = Optional.empty();
    abstract CompletableFuture<Measurement> measure();


    @Override
    public void run() {
        try {
            this.lastMeasurement = Optional.ofNullable(measure().get(2000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.error("Could not update data for Instrument", e);
        }
    }

    public Optional<Measurement> getLastMeasurement() {
        return lastMeasurement;
    }
}
