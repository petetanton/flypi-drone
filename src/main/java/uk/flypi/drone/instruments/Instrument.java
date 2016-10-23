package uk.flypi.drone.instruments;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class Instrument implements Runnable {

    private Optional<Measurement> lastMeasurement = Optional.empty();
    abstract CompletableFuture<Measurement> measure();


    @Override
    public void run() {
        try {
            this.lastMeasurement = Optional.ofNullable(measure().get(2000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public Optional<Measurement> getLastMeasurement() {
        return lastMeasurement;
    }
}
