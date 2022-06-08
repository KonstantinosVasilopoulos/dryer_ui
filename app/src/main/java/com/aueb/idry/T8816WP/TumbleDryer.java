package com.aueb.idry.T8816WP;

/**
 * Interface emulating the Miele T8816WP Edition 111 tumble dryer's operations.
 *
 * @see TumbleDryerImp
 */
public interface TumbleDryer {
    /**
     * Return the "gentle tumble" dryer status. When this option is selected, the drum rotates less
     * often. Used for delicate clothing.
     *
     * @return the "gentle tumble" dryer status.
     */
    public boolean getGentleTumble();

    /**
     * When this option is activated, the drum rotates less often. Used for delicate clothing.
     *
     * @param value boolean value to activate or deactivate the "gentle tumble" option.
     */
    public void setGentleTumble(boolean value);

    /**
     * The selected drying level of the dryer.
     *
     * @return the selected drying level
     * @see DryingLevel
     */
    public DryingLevel getDryingLevel();

    /**
     * Change the dryer's drying level.
     *
     * @param level the new drying level
     * @see DryingLevel
     */
    public void setDryingLevel(DryingLevel level);

    /**
     * Return the dryer's selected delay in minutes.
     *
     * @return the dryer's delay in minutes
     */
    public float getDelay();

    /**
     * Set a new dryer delay in minutes
     *
     * @param delay the dryer's new delay in minutes
     */
    public void setDelay(float delay);

    /**
     * Increment the dryer's set delay in a manner identical to the one of the machine. Please refer
     * to the manual for this complex behavior.
     */
    public void incrementDelay();

    /**
     * Return the buzzer option of the dryer.
     *
     * @return the buzzer's status
     */
    public boolean getBuzzer();

    /**
     * Change the buzzer's status to either ON or OFF(true or false respectively).
     *
     * @param buzzer the buzzer's new status
     */
    public void setBuzzer(boolean buzzer);

    /**
     * Return the "perfect dry" option. This option measures the residual moisture in the drum.
     * After the routine starts and the desired drying level has been reached, the "perfect dry"
     * indicator will remain on until the end of the routine.
     *
     * @return the "perfect dry" status as a boolean
     */
    public boolean getPerfectDry();

    /**
     * The filters' light.
     * A value of true indicates that the light is lit.
     *
     * @return the filters' light status
     */
    public boolean checkFilters();

    /**
     * The container's light.
     * A value of true indicates that the light is lit.
     *
     * @return the container's light status
     */
    public boolean checkContainer();

    /**
     * Return the state the Start/Stop button is currently in.
     *
     * @return the Start/Stop button's state
     * @see StartStopButtonState
     */
    public StartStopButtonState getStartStopButtonState();

    /**
     * A press of the Start/Stop button, alters the button's state.
     */
    public void pressStartStop();

    /**
     * Calculate routine's duration based on its settings. The duration is returned in milliseconds!
     *
     * @param dryingLevel the selected drying level of the dryer
     * @param programme the selected programme of the dryer
     * @return the duration in milliseconds
     */
    public long calculateDuration(DryingLevel dryingLevel, Programme programme);

    /**
     * Return the selected programme.
     *
     * @return the selected programme
     * @see Programme
     */
    public Programme getProgramme();

    /**
     * Set the new programme.
     *
     * @param programme the new programme
     * @see Programme
     */
    public void setProgramme(Programme programme);

    /**
     * Return the door's status. A value of true means that the door is closed.
     *
     * @return boolean value indicating whether the door is closed
     */
    public boolean isClosed();

    /**
     * Open the dryer's door. The tumble dryer can only open the door on its own!
     */
    public void openDoor();

    /**
     * Return the dryer's power status. A value of true indicates that the dryer is on.
     *
     * @return a boolean value representing the dryer's power status
     */
    public boolean getPowerStatus();

    /**
     * Turn the tumble dryer on.
     */
    public void turnOn();

    /**
     * Turn the tumble dryer off.
     */
    public void turnOff();

    /**
     * Change the dryer's power status. Effectively, an alternate way of turning on and off the dryer.
     *
     * @param value the new power status of the dryer
     */
    public void setPower(boolean value);

    /**
     * Return the duration of the selected programme and drying level in milliseconds.
     *
     * @return the time needed for the routine to complete in milliseconds
     */
    public long getDuration();

    /**
     * Return the dryer's operation status.
     *
     * @return the dryer's operation status
     * @see ProgrammeSequence
     */
    public ProgrammeSequence getProgrammeSequence();

    /**
     * Change the dryer's operational status.
     *
     * @param sequence the dryer's new operation status
     * @see ProgrammeSequence
     */
    public void setProgrammeSequence(ProgrammeSequence sequence);

    /**
     * Return true if the current routine ought to end now.
     *
     * @return a value of true in case the running routine ends now, otherwise false
     */
    public boolean checkProgrammeTime();
}
