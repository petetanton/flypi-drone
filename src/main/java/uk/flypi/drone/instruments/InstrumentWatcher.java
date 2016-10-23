package uk.flypi.drone.instruments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InstrumentWatcher {
    private static final Logger LOG = LogManager.getLogger(InstrumentWatcher.class);

    private final Map<String, Instrument> instruments;
    private final Map<String, Measurement> values;
    private final ScheduledExecutorService executor;
    private final int refresh;

    public InstrumentWatcher(final int refresh) {
        this.instruments = new HashMap<>();
        this.values = new HashMap<>();
        this.executor = Executors.newScheduledThreadPool(10);
        this.refresh = refresh;
    }

    public void addInstrumentToWatcher(final String key, final Instrument instrument) {
        this.executor.scheduleWithFixedDelay(instrument, refresh, refresh, TimeUnit.MILLISECONDS);
        this.instruments.put(key, instrument);
    }

    public Optional<Measurement> getValue(final String key) {
        return this.instruments.get(key).getLastMeasurement();
    }
}
