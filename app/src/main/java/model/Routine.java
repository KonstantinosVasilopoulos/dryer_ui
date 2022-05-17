package model;

import androidx.annotation.NonNull;

import com.aueb.idry.T8816WP.DryingLevel;
import com.aueb.idry.T8816WP.Programme;

public class Routine {
    private String name;
    private DryingLevel level;
    private Programme programme;
    private long delay;

    public Routine() {
        level = DryingLevel.NORMAL;
        programme = Programme.COTTONS;
        delay = 0L;
    }

    public Routine(String name) {
        this();
        this.name = name;
    }

    public Routine(String name, DryingLevel level) {
        this(name);
        this.level = level;
    }

    public Routine(String name, DryingLevel level, Programme programme) {
        this(name, level);
        this.programme = programme;
    }

    public Routine(String name, DryingLevel level, Programme programme, long delay) {
        this(name, level, programme);
        this.delay = delay;
    }

    // Separate the routine's attributes
    // New line included
    public String toCSV() {
        return name + ',' + level.ordinal() + ',' + programme.ordinal() + ',' + String.valueOf(delay) + '\n';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DryingLevel getLevel() {
        return level;
    }

    public void setLevel(DryingLevel level) {
        this.level = level;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @NonNull
    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", dryingLevel=" + level +
                ", programme=" + programme +
                ", delay= " + String.valueOf(delay) +
                '}';
    }
}
