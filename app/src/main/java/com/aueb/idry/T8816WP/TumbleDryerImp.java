package com.aueb.idry.T8816WP;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TumbleDryerImp implements TumbleDryer {

    // Singleton
    private static TumbleDryerImp dryer;

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
    private Date endTime;
    private ProgrammeSequence sequence;
    private boolean running;

    // For debugging
    private Random random;

    public static TumbleDryerImp getInstance() {
        if (dryer == null) {
            dryer = new TumbleDryerImp();
        }

        return dryer;
    }

    public static TumbleDryerImp getRandomInstance() {
        if (dryer == null) {
            long now = Calendar.getInstance().getTime().getTime();
            dryer = new TumbleDryerImp(now);
        }

        return dryer;
    }

    private TumbleDryerImp() {
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
    private TumbleDryerImp(long seed) {
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
                long duration = calculateDuration(dryingLevel, programme);

                // Determine the exact end time of the program
                Calendar calendar = Calendar.getInstance();
                endTime = calendar.getTime();
                long now = calendar.getTime().getTime();
                endTime.setTime(now + duration);

                running = true;
                startStop = StartStopButtonState.CONSTANT_LIT;
                break;

            case CONSTANT_LIT:
                running = !running;
                break;
        }
    }

    @Override
    public long calculateDuration(DryingLevel dryingLevel, Programme programme) {
        long MILLIS_IN_HOUR = 3600000L;
        long duration = 0L;

        // Calculate the duration of the selected program
        switch (programme) {
            // Set end time for each program
            case COTTONS:
            case WOOLLENS:
            case SHIRTS:
            case GENTLE_SMOOTHING:
            case PROOFING:
                duration += MILLIS_IN_HOUR;
                break;

            case MINIMUM_IRON:
            case EXPRESS:
            case AUTOMATIC_PLUS:
            case DENIM:
                duration += MILLIS_IN_HOUR / 2;
                break;

            case OUTERWEAR:
            case HYGIENE:
            case WARM_AIR:
                duration += MILLIS_IN_HOUR * 3 / 4;
                break;
        }

        switch (dryingLevel) {
            case EXTRA_DRY:
                duration += MILLIS_IN_HOUR / 4;
                break;

            case HAND_IRON:
                duration += MILLIS_IN_HOUR / 6;
                break;

            case MACHINE_IRON:
                duration += MILLIS_IN_HOUR / 8;
                break;
        }

        return duration;
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
    public long getDuration() {
        if (endTime != null) {
            Date now = Calendar.getInstance().getTime();
            return now.getTime() - endTime.getTime();
        }

        return 0L;
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

    @Override
    public boolean checkProgrammeTime() {
        long now = Calendar.getInstance().getTime().getTime();
        if (running && now >= endTime.getTime()) {
            // Stop running
            running = false;
            endTime = null;
            return true;
        }

        return false;
    }
}
