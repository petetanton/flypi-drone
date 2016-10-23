package uk.flypi.drone.instruments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentWatcherTest {

    private InstrumentWatcher underTest;

    @Mock private Instrument mockInstrument1;
    @Mock private Instrument mockInstrument2;
    @Mock private Instrument mockInstrument3;

    @Before
    public void setUp() throws Exception {
        this.underTest = new InstrumentWatcher(250);
    }

    @Test
    public void itShouldScheduleInstruments() throws InterruptedException {
        this.underTest.addInstrumentToWatcher("1", mockInstrument1);
        this.underTest.addInstrumentToWatcher("2", mockInstrument2);
        this.underTest.addInstrumentToWatcher("3", mockInstrument3);

        Thread.sleep(600);
        verify(mockInstrument1, times(2)).run();
        verify(mockInstrument2, times(2)).run();
        verify(mockInstrument3, times(2)).run();

        verifyNoMoreInteractions(mockInstrument1, mockInstrument2, mockInstrument3);
    }

    @Test
    public void itDelegatesToTheInstrument() throws InterruptedException {
        final Date date = new Date();
        when(mockInstrument1.getLastMeasurement()).thenReturn(Optional.of(new Measurement(1f, date)));
        this.underTest.addInstrumentToWatcher("1", mockInstrument1);

        final Optional<Measurement> actual = this.underTest.getValue("1");

        verify(mockInstrument1).getLastMeasurement();
        verifyNoMoreInteractions(mockInstrument1, mockInstrument2, mockInstrument3);

        assertTrue(actual.isPresent());
        assertEquals(1f, actual.get().getValue(), 0f);
        assertEquals(date, actual.get().getDate());
    }

}