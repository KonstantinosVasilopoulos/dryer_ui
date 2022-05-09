package model;

import android.content.Context;
import android.util.Log;

import androidx.collection.ArraySet;

import com.aueb.idry.T8816WP.DryingLevel;
import com.aueb.idry.T8816WP.Programme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class RoutineDAO {

    // Singleton
    private static RoutineDAO instance;

    // Set for storing routines
    private final Set<Routine> routines;
    private String storagePath;
    private final String STORAGE_FILENAME = "routines.csv";

    // Warning: instance might be null!
    public static RoutineDAO getInstance() {
        return instance;
    }

    public static RoutineDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RoutineDAO(context);
        }

        return instance;
    }

    private RoutineDAO(Context context) {
        // Initialize the routines set by loading the routines from storage
        routines = new ArraySet<>();
        try {
            // Create an empty file if needed
            File file = new File(context.getFilesDir(), STORAGE_FILENAME);
            storagePath = file.getPath();
            file.delete(); // -0

            // Create file if needed
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read data from file
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split(",");

                // Create and save a new routine
                if (!containsName(values[0])) {
                    Routine routine = new Routine(
                            values[0],
                            DryingLevel.values()[Integer.parseInt(values[1])],
                            Programme.values()[Integer.parseInt(values[2])]
                    );
                    routines.add(routine);
                }

                // Next line
                line = reader.readLine();
            }

            reader.close();

            // Add preset routines
            Routine presetRoutine = new Routine("Everyday cottons");
            saveRoutine(presetRoutine);
            presetRoutine = new Routine(
                    "Express",
                    DryingLevel.NORMAL,
                    Programme.EXPRESS
            );
            saveRoutine(presetRoutine);
            presetRoutine = new Routine(
                    "Hand iron",
                    DryingLevel.HAND_IRON,
                    Programme.SHIRTS
            );
            saveRoutine(presetRoutine);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Finds whether a routine named with the provided string exists
    public boolean containsName(String name) {
        for (Routine routine : routines) {
            if (routine.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void saveRoutine(Routine routine) {
        if (!containsName(routine.getName())) {
            try {
                // Write to file
                FileWriter fileWriter = new FileWriter(storagePath , true);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                writer.write(routine.toCSV());

                writer.close();

                // Append to routines' set
                routines.add(routine);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeRoutine(String name) {
        if (containsName(name)) {
            try {
                // Remove from routines' set
                Routine toRemove = null;
                for (Routine routine : routines) {
                    if (name.equals(routine.getName())) {
                        toRemove = routine;
                        break;
                    }
                }

                routines.remove(toRemove);

                // Rewrite all routines to the file
                FileWriter fileWriter = new FileWriter(storagePath, false);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                for (Routine routine : routines) {
                    writer.write(routine.toCSV());
                }

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Routines are updated based on their name
    public void updateRoutine(Routine updatedRoutine) {
        if (containsName(updatedRoutine.getName())) {
            // Search and replace the routine having the same name
            Routine toRemove = null;
            for (Routine routine : routines) {
                if (updatedRoutine.getName().equals(routine.getName())) {
                    toRemove = routine;
                    break;
                }
            }

            routines.remove(toRemove);
            routines.add(updatedRoutine);

            // Rewrite all routines
            try {
                FileWriter fileWriter = new FileWriter(storagePath, false);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                for (Routine routine : routines) {
                    writer.write(routine.toCSV());
                }

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Search for a routine using its name
    public Routine getRoutine(String name) {
        if (containsName(name)) {
            for (Routine routine : routines) {
                if (name.equals(routine.getName())) {
                    return routine;
                }
            }
        }

        return null;
    }

    public Set<Routine> getRoutines() {
        return routines;
    }
}
