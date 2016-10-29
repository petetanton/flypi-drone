package uk.flypi.drone.instruments;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InstrumentTest {

    private Date date1;
    private Instrument underTest;
    private int measureCount;

    @Before
    public void setUp() throws Exception {
        this.date1 = new Date();
        this.measureCount = 0;
        this.underTest = new Instrument() {
            @Override
            CompletableFuture<Measurement> measure() {
                return CompletableFuture.supplyAsync(() -> {
                    Measurement mes;
                    if (measureCount == 0) {
                        mes = null;
                    } else if (measureCount == 2) {
                        mes = null;
                        try {
                            wait(10000);
                        } catch (InterruptedException e) {
                            fail("There has been a problem running the test");
                        }
                    } else {
                        mes = new Measurement((float) measureCount, date1);
                    }
                    measureCount++;
                    return mes;
                });
            }
        };

    }


    @Test
    public void itReturnsAnOptionalNullIfNothingExists() {
        this.underTest.run();

        final Optional<Measurement> actual = this.underTest.getLastMeasurement();

        assertEquals(1, measureCount);

        assertFalse(actual.isPresent());
    }

    @Test
    public void itUsesTheMeasureMethod() {
        this.underTest.run();
        this.underTest.run();

        final Optional<Measurement> actual = this.underTest.getLastMeasurement();

        assertEquals(2, measureCount);

        assertTrue(actual.isPresent());
        assertEquals(1f, actual.get().getValue(), 0f);
        assertEquals(date1, actual.get().getDate());
    }

    @Test
    public void itUsesThePreviousValueIfTheCFTimesout() {
        this.underTest.run();
        this.underTest.run();
        this.underTest.run();

        final Optional<Measurement> actual = this.underTest.getLastMeasurement();

        assertEquals(2, measureCount);

        assertTrue(actual.isPresent());
        assertEquals(1f, actual.get().getValue(), 0f);
        assertEquals(date1, actual.get().getDate());
    }
}