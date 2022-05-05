package com.aueb.idry;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.aueb.idry.T8816WP.DryingLevel;
import com.aueb.idry.T8816WP.Programme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import model.Routine;
import model.RoutineDAO;

@RunWith(AndroidJUnit4.class)
public class RoutineDAOTest {
    private Context context;
    private RoutineDAO routines;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File file = new File(context.getFilesDir(), "routines.csv");
        file.delete();
        routines = RoutineDAO.getInstance(context);
    }

    @Test
    public void testRoutineDAOConstructor() {
        File routinesFile = new File(context.getFilesDir(), "routines.csv");
        assertTrue(routinesFile.exists());
    }

    @Test
    public void testSaveRoutine() {
        // Save a new routine
        Routine routine = new Routine(
                "test routine",
                DryingLevel.EXTRA_DRY,
                Programme.SHIRTS
        );
        routines.saveRoutine(routine);

        // Make sure that the routine was saved
        assertTrue(routines.containsName("test routine"));
        for (Routine r : routines.getRoutines()) {
            if (r.getName().equals("test routine")) {
                assertEquals(DryingLevel.EXTRA_DRY, r.getLevel());
                assertEquals(Programme.SHIRTS, r.getProgramme());
            }
        }

        // Try saving a routine with the same name
        routine = new Routine(
                "test routine",
                DryingLevel.NORMAL,
                Programme.AUTOMATIC_PLUS
        );
        routines.saveRoutine(routine);

        // The routine named "test routine" ought not to have changed
        for (Routine r : routines.getRoutines()) {
            if (r.getName().equals("test routine")) {
                assertEquals(DryingLevel.EXTRA_DRY, r.getLevel());
                assertEquals(Programme.SHIRTS, r.getProgramme());
            }
        }
    }

    @Test
    public void testRemoveRoutine() {
        routines.removeRoutine("test routine");
        assertFalse(routines.containsName("test routine"));
    }

    @Test
    public void testUpdateRoutine() {
        // Create a new routine
        Routine routine = new Routine(
                "new routine",
                DryingLevel.NORMAL,
                Programme.EXPRESS
        );
        routines.saveRoutine(routine);

        // Create a new routine with the same name
        Routine newRoutine = new Routine(
                "new routine",
                DryingLevel.HAND_IRON,
                Programme.WOOLLENS
        );
        routines.updateRoutine(newRoutine);

        // Ensure that the routine was amended
        for (Routine r : routines.getRoutines()) {
            if (r.getName().equals("new routine")) {
                assertEquals(DryingLevel.HAND_IRON, r.getLevel());
                assertEquals(Programme.WOOLLENS, r.getProgramme());
                break;
            }
        }
    }

    @Test
    public void testFileWritten() {
        File file = new File(context.getFilesDir(), "routines.csv");
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);

                String line = reader.readLine();
                assertNotEquals(null, line);

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void printCSVFile() {
        File file = new File(context.getFilesDir(), "routines.csv");
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);

                StringBuilder data = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    data.append(line);
                    line = reader.readLine();
                }

                Log.d("CSV file", data.toString());

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        for (Routine routine : routines.getRoutines()) {
//            Log.d("routines", routine.toString());
//        }
    }
}