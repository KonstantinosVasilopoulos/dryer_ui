package model;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PreferenceDAO {
    private File file;

    // Singleton object
    private static PreferenceDAO instance = null;

    private final String FILENAME = "preferences.txt";

    public static PreferenceDAO getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceDAO(context);
        }

        return instance;
    }

    private PreferenceDAO(Context context) {

        try {
            file = new File(context.getFilesDir(), FILENAME);

            if (!file.exists()) {
                file.createNewFile();

                // Initialize default preferences
                Preference defaultPreference = new Preference(
                        true,
                        false,
                        false,
                        true
                );

                try {
                    savePreference(defaultPreference);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePreference(Preference preference) throws JSONException {
        JSONObject preferenceJsonObject = new JSONObject();

        preferenceJsonObject.put("notifications", preference.getNotifications());
        preferenceJsonObject.put("voiceInstructions", preference.getVoiceInstructions());
        preferenceJsonObject.put("voiceCommands", preference.getVoiceCommands());
        preferenceJsonObject.put("language", preference.getLanguage());

        String json = preferenceJsonObject.toString();

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Preference retrievePreference() {
        Preference preference = null;

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(fileReader);
            String json = reader.readLine();
            reader.close();
            JSONObject preferenceJsonObject = new JSONObject(json);

            boolean notifications = preferenceJsonObject.getBoolean("notifications");
            boolean voiceInstructions = preferenceJsonObject.getBoolean("voiceInstructions");
            boolean voiceCommands = preferenceJsonObject.getBoolean("voiceCommands");
            boolean language = preferenceJsonObject.getBoolean("language");
            preference = new Preference(notifications, voiceInstructions, voiceCommands, language);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return preference;
    }
}
