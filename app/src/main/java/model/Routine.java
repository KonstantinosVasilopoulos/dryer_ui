package model;

import androidx.annotation.NonNull;

import com.aueb.idry.T8816WP.DryingLevel;
import com.aueb.idry.T8816WP.Programme;

public class Routine {
    private String name;
    private DryingLevel level;
    private Programme programme;

    public Routine() {
        level = DryingLevel.NORMAL;
        programme = Programme.COTTONS;
    }

    public Routine(String name) {
        this();
        this.name = name;
    }

    public Routine(String name, DryingLevel level, Programme programme) {
        this.name = name;
        this.level = level;
        this.programme = programme;
    }

    // Separate the routine's attributes
    // New line included
    public String toCSV() {
        return name + ',' + level.ordinal() + ',' + programme.ordinal() + '\n';
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

    @NonNull
    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", dryingLevel=" + level +
                ", programme=" + programme +
                '}';
    }
}
