package uk.flypi.drone.instruments;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SonarTest {

    private Sonar underTest;

    @Mock private GpioController gpio;
    @Mock private GpioPinDigitalInput echoPin;
    @Mock private GpioPinDigitalOutput trigPin;

    @Before
    public void setUp() throws Exception {
        when(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04)).thenReturn(trigPin);
        when(gpio.provisionDigitalInputPin(RaspiPin.GPIO_05)).thenReturn(echoPin);
        when(echoPin.isLow()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(echoPin.isHigh()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        this.underTest = new Sonar(this.gpio);
    }

    @Test
    public void itMeasuresADistance() throws ExecutionException, InterruptedException {
        final Float actual = this.underTest.measure().get().getValue();
        verify(trigPin, times(2)).low();
        verify(gpio).provisionDigitalOutputPin(RaspiPin.GPIO_04);
        verify(gpio).provisionDigitalInputPin(RaspiPin.GPIO_05);
        verify(echoPin, times(3)).isLow();
        verify(echoPin, times(4)).isHigh();
        verify(trigPin).high();

        verifyNoMoreInteractions(gpio, echoPin, trigPin);
    }
}