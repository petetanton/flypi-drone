package uk.flypi.drone.instruments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class DummyInstrument extends Instrument {
    private static final Logger LOG = LogManager.getLogger(DummyInstrument.class);
    private final long waitTime;
    private final float value;

    public DummyInstrument(final long waitTime, final float value) {
        this.waitTime = waitTime;
        this.value = value;
    }

    @Override
    public CompletableFuture<Measurement> measure() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ignored) {
            }
            LOG.info("Dummy instrument has completed: " + value);
            return new Measurement(value, new Date());
        });
    }
}
