package com.aueb.idry.T8816WP;

import java.time.Duration;

// Interface emulating the T8816WP Edition 111 tumble dryer's operations
public interface TumbleDryer {
    // Gentle tumble
    public boolean getGentleTumble();
    public void setGentleTumble(boolean value);

    // Drying level
    public DryingLevel getDryingLevel();
    public void setDryingLevel(DryingLevel level);

    // Delay
    public float getDelay();
    public void setDelay(float delay);
    public void incrementDelay();

    // Buzzer
    public boolean getBuzzer();
    public void setBuzzer(boolean buzzer);

    // Perfect dry indicator
    public boolean getPerfectDry();

    // Fault/Check lights
    // Filters
    // A value of true indicates that the light is lit
    public boolean checkFilters();

    // Container
    // Same behavior as the above
    public boolean checkContainer();

    // Start/Stop button
    public StartStopButtonState getStartStopButtonState();
    public void pressStartStop();

    // Programme selector
    public Programme getProgramme();
    public void setProgramme(Programme programme);

    // Door
    public boolean isClosed();
    public void openDoor(); // The tumble dryer can only open the door on its own!

    // Power
    public boolean getPowerStatus(); // true means that the dryer is on
    public void turnOn();
    public void turnOff();
    public void setPower(boolean value);

    // Programme duration
    public Duration getDuration();

    // Programme sequence
    public ProgrammeSequence getProgrammeSequence();
    public void setProgrammeSequence(ProgrammeSequence sequence);
}
