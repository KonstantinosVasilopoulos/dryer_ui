package com.aueb.idry.T8816WP;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class TumbleDryerImp implements TumbleDryer {

    private boolean gentleTumble;
    private DryingLevel dryingLevel;
    private float delay;
    private boolean buzzer;
    private boolean perfectDry;
    private boolean filters;
    private boolean container;
    private StartStopButtonState startStop; // The state of the Start/Stop button
    private Programme programme;
    private boolean doorClosed;
    private boolean powerOn;
    private Duration duration;
    private LocalDateTime endTime;
    private ProgrammeSequence sequence;
    private boolean running;

    // For debugging
    private Random random;

    public TumbleDryerImp() {
        // Initialize the tumble with default values
        gentleTumble = false;
        dryingLevel = DryingLevel.NORMAL;
        delay = 0.f;
        buzzer = false;
        perfectDry = false;
        filters = false;
        container = false;
        startStop = StartStopButtonState.OFF;
        programme = Programme.COTTONS;
        doorClosed = true;
        powerOn = false;
        sequence = ProgrammeSequence.NONE;
        running = false;
    }

    // For debugging!
    public TumbleDryerImp(long seed) {
        random = new Random(seed);

        // Assign random values
        gentleTumble = random.nextBoolean();
        dryingLevel = DryingLevel.NORMAL;
        delay = random.nextFloat() * random.nextInt(25);
        buzzer = random.nextBoolean();
        perfectDry = random.nextBoolean();
        filters = random.nextBoolean();
        container = random.nextBoolean();
        startStop = StartStopButtonState.OFF;
        programme = Programme.COTTONS;
        doorClosed = random.nextBoolean();
        powerOn = true; // The power must be on, otherwise many values will reset on startup
        sequence = ProgrammeSequence.NONE;
        running = false;
    }

    // Gentle tumble
    @Override
    public boolean getGentleTumble() {
        return gentleTumble;
    }

    @Override
    public void setGentleTumble(boolean value) {
        gentleTumble = value;
    }

    // Drying level
    @Override
    public DryingLevel getDryingLevel() {
        return dryingLevel;
    }

    @Override
    public void setDryingLevel(DryingLevel level) {
        dryingLevel = level;
    }

    // Delay
    @Override
    public float getDelay() {
        return delay;
    }

    @Override
    public void setDelay(float delay) {
        if (delay <= 24.f) {
            this.delay = delay;
        }
    }

    @Override
    public void incrementDelay() {
        if (delay < 10.f) {
            delay += 0.5f;
        } else  if (delay < 24.f) {
            delay += 1.f;
        }
    }

    // Buzzer
    @Override
    public boolean getBuzzer() {
        return buzzer;
    }

    @Override
    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    // Perfect dry indicator
    @Override
    public boolean getPerfectDry() {
        return perfectDry;
    }

    // Calculate and return the value of the "Perfect Dry" indicator light
    private boolean estimatePerfectDry() {
        return programme != Programme.WOOLLENS && programme != Programme.WARM_AIR &&
                programme != Programme.GENTLE_SMOOTHING;
    }

    // Fault/Check lights
    // Filters
    // A value of true indicates that the light is lit
    @Override
    public boolean checkFilters() {
        return filters;
    }

    // Container
    // Same behavior as the above
    @Override
    public boolean checkContainer() {
        return container;
    }

    // Start/Stop button
    @Override
    public StartStopButtonState getStartStopButtonState() {
        return startStop;
    }

    @Override
    public void pressStartStop() {
        // Determine the actions to be made from the current state
        switch (startStop) {
            case OFF:
                break;

            case FLASHING:
                perfectDry = estimatePerfectDry();

                // Start the selected program
                switch (programme) {
                    // TODO: Insert duration for each program
                    case COTTONS:

                        break;

                    case MINIMUM_IRON:

                        break;

                    case WOOLLENS:

                        break;

                    case OUTERWEAR:

                        break;

                    case PROOFING:

                        break;

                    case EXPRESS:

                        break;

                    case AUTOMATIC_PLUS:

                        break;

                    case SHIRTS:

                        break;

                    case DENIM:

                        break;

                    case HYGIENE:

                        break;

                    case WARM_AIR:

                        break;

                    case GENTLE_SMOOTHING:

                        break;
                }

                running = true;
                break;

            case CONSTANT_LIT:
                running = false;
                break;
        }
    }

    // Programme selector
    @Override
    public Programme getProgramme() {
        return programme;
    }

    @Override
    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    // Door
    @Override
    public boolean isClosed() {
        return doorClosed;
    }

    @Override
    public void openDoor() {
        doorClosed = false;
    }

    // Power
    @Override
    public boolean getPowerStatus() {
        return powerOn;
    }

    @Override
    public void turnOn() {
        // Set the drying level to normal
        dryingLevel = DryingLevel.NORMAL;

        // Set the delay to zero
        setDelay(0.f);

        // Turn on the Start/Stop button's light
        startStop = StartStopButtonState.FLASHING;

        // Set the drying program to "cottons"
        setProgramme(Programme.COTTONS);

        // Finally, turn on the power
        powerOn = true;
    }

    @Override
    public void turnOff() {
        // Turn off the Start/Stop button's light
        startStop = StartStopButtonState.OFF;

        // Turn off the power
        powerOn = false;
    }

    @Override
    public void setPower(boolean value) {
        if (value)
            turnOn();
        else
            turnOff();
    }

    // Programme duration
    @Override
    public Duration getDuration() {
        return duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duration calculateDuration() {
        if (endTime != null) {
            // FIXME: API errors
            LocalDateTime now = LocalDateTime.now();
            return Duration.between(now, endTime);
        }

        return Duration.ZERO;
    }

    // Programme sequence
    @Override
    public ProgrammeSequence getProgrammeSequence() {
        return sequence;
    }

    @Override
    public void setProgrammeSequence(ProgrammeSequence sequence) {
        this.sequence = sequence;
    }
}
