package uk.flypi.drone.instruments;

import java.util.Date;

public class Measurement {
    private final Float value;
    private final Date date;

    public Measurement(final Float value, final Date date) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public Float getValue() {
        return value;
    }
}
