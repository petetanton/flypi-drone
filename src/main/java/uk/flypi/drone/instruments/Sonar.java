package uk.flypi.drone.instruments;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.flypi.drone.exception.InstrumentException;

import java.util.concurrent.TimeoutException;

public class Sonar {
    //    TODO: Calculate speed of sound from a temperature sensor
    private static final float SOUND_SPEED = 344f;
    private static final Logger LOG = LogManager.getLogger(Sonar.class);

    private final GpioPinDigitalInput echoPin;
    private final GpioPinDigitalOutput trigPin;

    public Sonar(final GpioController gpio) {
        this.trigPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        this.echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
        this.trigPin.low();
    }

    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep(0, 10 * 1000);
            this.trigPin.low();
        } catch (InterruptedException ex) {
            LOG.error("Interrupt during trigger", ex);
        }
    }

    private void waitForSignal() throws TimeoutException, InterruptedException {
        int countdown = 2100;

        while (this.echoPin.isLow() && countdown > 0) {
            countdown--;
        }

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal start");
        }
    }

    public float measureDistance() throws InstrumentException {
        long duration;
        try {
            this.triggerSensor();
            this.waitForSignal();
            duration = this.measureSignal();
        } catch (TimeoutException | InterruptedException e) {
            throw new InstrumentException("An exception occured whilst measuring a distance", e);
        }

        return duration * SOUND_SPEED / (2 * 10000);
    }

    private long measureSignal() throws TimeoutException {
        int countdown = 2100;
        long start = System.nanoTime();
        while (this.echoPin.isHigh() && countdown > 0) {
            countdown--;
        }
        long end = System.nanoTime();

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal end");
        }

        return (long) Math.ceil((end - start) / 1000.0);  // Return micro seconds
    }

}
