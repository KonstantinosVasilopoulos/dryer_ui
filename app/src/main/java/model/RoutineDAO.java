package model;

import android.content.Context;

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

/**
 * The RoutineDAO class provides basic CRUD operations for the Routine model. This class is also a
 * Singleton, which means that only one instance of the class exists and can be accessed everywhere
 * in the project.
 *
 * @see Routine
 */
public class RoutineDAO {

    // Singleton
    private static RoutineDAO instance;

    // Set for storing routines
    private final Set<Routine> routines;
    private String storagePath;
    private final String STORAGE_FILENAME = "routines.csv";

    /**
     * Method for accessing the singleton without providing any parameters. Please ensure that the
     * singleton exists before calling, otherwise this method will return null.
     * @return the single instance of the RoutineDAO or null
     */
    public static RoutineDAO getInstance() {
        return instance;
    }

    /**
     * Return the singleton instance. Create the instance if this is the first time this method
     * called.
     * @param context the Android application's context
     * @return the single instance of the RoutineDAO
     */
    public static RoutineDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RoutineDAO(context);
        }

        return instance;
    }

    /**
     * Private constructor for this class. Accesses the application's storage and retrieves the
     * routines.
     * @param context the Android application's context
     */
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
                            Programme.values()[Integer.parseInt(values[2])],
                            Long.parseLong(values[3])
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

    /**
     * Finds whether a routine's name matches the provided string. A routine's name serves as a
     * unique ID; two or more routines cannot have the same name.
     * @param name the name to be queried.
     * @return true if a routine named 'name' exists or false if it doesn't
     */
    public boolean containsName(String name) {
        for (Routine routine : routines) {
            if (routine.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Save a new routine to permanent storage.
     * @param routine the new routine to be saved
     */
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

    /**
     * Remove a saved routine. Only the routine's name is required to find the routine and delete it,
     * since two or more routines cannot have the same name.
     * @param name the routine's name
     */
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

    /**
     * Update an already existing routine. The provided routine's name has to match an already
     * existing routine, which will be updated. In case the routine's name has to be updated, first
     * delete the old routine and add the updated one(don't use this method in such a case!).
     * @param updatedRoutine the updated routine which has the same name as the old one
     */
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

    /**
     * Search for a saved routine using its name.
     * @param name the routine's name
     * @return the routine or null
     */
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

    /**
     * Return a copy of the set holding all saved routines.
     * @return the set containing all routines saved
     */
    public Set<Routine> getRoutines() {
        return new ArraySet<>(routines);
    }
}
