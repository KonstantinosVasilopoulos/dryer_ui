package model;

import androidx.annotation.NonNull;

import com.aueb.idry.T8816WP.DryingLevel;
import com.aueb.idry.T8816WP.Programme;

/**
 * The Routine class represents a set of pre-selected dryer options that are necessary to
 * run the dryer. Such options include the routine's name, a drying level, a programme and
 * a delay.
 *
 * @author Konstantinos Vasilopoulos
 */
public class Routine {
    private String name;
    private DryingLevel level;
    private Programme programme;
    private long delay;

    /**
     * Default empty constructor.
     */
    public Routine() {
        level = DryingLevel.NORMAL;
        programme = Programme.COTTONS;
        delay = 0L;
    }

    /**
     * Constructor having the routine's name as the only argument.
     * @param name the routine's name
     */
    public Routine(String name) {
        this();
        this.name = name;
    }

    /**
     * Constructor with the routine's name and its drying level.
     * @param name the routine's name
     * @param level the drying level of this routine
     */
    public Routine(String name, DryingLevel level) {
        this(name);
        this.level = level;
    }

    /**
     * Constructor receiving the routine's name, its drying level and its programme as parameters.
     * @param name the routine's name
     * @param level the drying level of the routine
     * @param programme the programme of the routine
     */
    public Routine(String name, DryingLevel level, Programme programme) {
        this(name, level);
        this.programme = programme;
    }

    /**
     * Constructor receiving values for every variable of the routine.
     * @param name the routine's name
     * @param level the drying level of the routine
     * @param programme the programme of the routine
     * @param delay the amount of time in milliseconds that the routine will have to wait before starting
     */
    public Routine(String name, DryingLevel level, Programme programme, long delay) {
        this(name, level, programme);
        this.delay = delay;
    }

    /**
     * Separate the routine's attributes for storing the routine in a CSV file.
     * New line included at the end.
     * @return the routine's variables separated by commas
     */
    public String toCSV() {
        return name + ',' + level.ordinal() + ',' + programme.ordinal() + ',' + delay + '\n';
    }

    /**
     * Return the routine's name.
     * @return the routine's name
     */
    public String getName() {
        return name;
    }

    /**
     * Give the routine a new name.
     * @param name the routine's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the routine's drying level.
     * @return the routine's drying level
     */
    public DryingLevel getLevel() {
        return level;
    }

    /**
     * Give the routine a new drying level.
     * @param level the routine's drying level
     */
    public void setLevel(DryingLevel level) {
        this.level = level;
    }

    /**
     * Return the routine's programme.
     * @return the routine's programme
     */
    public Programme getProgramme() {
        return programme;
    }

    /**
     * Give the routine a new programme
     * @param programme the routine's programme
     */
    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    /**
     * Return the routine's delay.
     * @return the delay of the routine in milliseconds
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Give the routine a new delay
     * @param delay the delay of the routine in milliseconds
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * @return a string representation of the routine
     */
    @NonNull
    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", dryingLevel=" + level +
                ", programme=" + programme +
                ", delay= " + delay +
                '}';
    }
}
