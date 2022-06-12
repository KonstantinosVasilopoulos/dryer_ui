package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aueb.idry.R;

import org.json.JSONException;

import java.util.Locale;

import model.Preference;
import model.PreferenceDAO;
import utils.LanguageHelper;

public class Settings extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch notificationsSwitcher, voiceInstructionsSwitcher,
            voiceCommandsSwitcher, englishSwitcher, greekSwitcher;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        preference = PreferenceDAO.getInstance(this).retrievePreference();

        notificationsSwitcher = findViewById(R.id.switchsettnot);

        voiceInstructionsSwitcher = findViewById(R.id.switchsettVI);

        voiceCommandsSwitcher = findViewById(R.id.switchSettVC);

        englishSwitcher = findViewById(R.id.switchsetEN);

        greekSwitcher = findViewById(R.id.switchSettGr);

        setSwitchers(preference);

        Button returnBtn = findViewById(R.id.returnfromsettings);

        if (greekSwitcher.isChecked()){
            if (voiceCommandsSwitcher.isChecked()){
               voiceCommandsSwitcher.setChecked(false);
                Toast.makeText(this, "Οι φωνητικές εντολές δεν υποστηρίζονται στα ελληνικά", Toast.LENGTH_SHORT).show();
            }
        }

        if(Locale.getDefault().getLanguage().equals("en")) {
            greekSwitcher.setChecked(false);
            englishSwitcher.setChecked(true);

        } else{
            greekSwitcher.setChecked(true);
            englishSwitcher.setChecked(false);
            voiceCommandsSwitcher.setChecked(false);
        }

        /* ENGLISH SWITCER */
        englishSwitcher.setOnClickListener(view -> setLanguage(englishSwitcher.isChecked(), true));

        /* GREEK SWITCER */
        greekSwitcher.setOnClickListener(view -> setLanguage(!greekSwitcher.isChecked(), true));

        /* Return button */
        returnBtn.setOnClickListener(view -> finishSettings());

        /* voice commands only available in english*/
        voiceCommandsSwitcher.setOnClickListener(view -> {
               if (greekSwitcher.isChecked()) {
                   voiceCommandsSwitcher.setChecked(false);
                   Toast.makeText(this, "Οι φωνητικές εντολές δεν υποστηρίζονται στα ελληνικά", Toast.LENGTH_SHORT).show();
               }
        });
    }

    private void setSwitchers(Preference preference) {
        if (preference == null) {
            setLanguage(Locale.getDefault().getLanguage().equals("en"), false);
        }
        else {
            notificationsSwitcher.setChecked(preference.getNotifications());
            voiceInstructionsSwitcher.setChecked(preference.getVoiceInstructions());
            voiceCommandsSwitcher.setChecked(preference.getVoiceCommands());
            setLanguage(!preference.getLanguage(), false);
        }
    }

    private void setLanguage( boolean language, boolean restartActivity) {
        /* change language? */
        if (language) {
            LanguageHelper.setLocale(this, "en");
            englishSwitcher.setChecked(true);
            greekSwitcher.setChecked(false);
        } else {
            LanguageHelper.setLocale(this , "el");
            englishSwitcher.setChecked(false);
            greekSwitcher.setChecked(true);
        }

        if (restartActivity) {
            savePreference();
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        }
    }

    private void savePreference() {
        preference.setNotifications(notificationsSwitcher.isChecked());
        preference.setVoiceInstructions(voiceInstructionsSwitcher.isChecked());
        preference.setVoiceCommands(voiceCommandsSwitcher.isChecked());
        preference.setLanguage(greekSwitcher.isChecked());

        try {
            PreferenceDAO.getInstance(this).savePreference(preference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Save preferences and head back to the previous activity in the activity stack
    private void finishSettings() {
        savePreference();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishSettings();
    }
}