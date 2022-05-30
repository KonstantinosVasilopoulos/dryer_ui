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

public class Settings extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch notificationsSwitcher, voiceInstructionsSwitcher,
            voiceCommandsSwitcher, englishSwitcher, greekSwitcher;

    private Class classToIntent;

    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Bundle param = getIntent().getExtras();

        if (param != null) {
            classToIntent = (Class) param.getSerializable("className");
        }

        preference = PreferenceDAO.getInstance(this).retrievePreference();

        notificationsSwitcher = findViewById(R.id.switchsettnot);

        voiceInstructionsSwitcher = findViewById(R.id.switchsettVI);

        voiceCommandsSwitcher = findViewById(R.id.switchSettVC);

        englishSwitcher = findViewById(R.id.switchsetEN);

        greekSwitcher = findViewById(R.id.switchSettGr);

        setSwitchers(preference);

        Button returnBtn = findViewById(R.id.returnfromsettings);

        //notificationSwitcher.setChecked(true);
        //voiceInstructionsSwitcher.setChecked(false);
        //voiceCommandsSwitcher.setChecked(false);
        if(Locale.getDefault().getLanguage().equals("en")) {
            greekSwitcher.setChecked(false);
            englishSwitcher.setChecked(true);
            
        }else{
            greekSwitcher.setChecked(true);
            englishSwitcher.setChecked(false);
        }

        /* ENGLISH SWITCER */
        englishSwitcher.setOnClickListener(view -> {
            if (englishSwitcher.isChecked()) {
                setLanguage(true, true);
            }
            else {
                setLanguage(false, true);
            }

        });

        /* GREEK SWITCER */
        greekSwitcher.setOnClickListener(view -> {
            if (greekSwitcher.isChecked()) {
                setLanguage(false, true);
            }
            else {
                setLanguage(true, true);
            }
        });

        /* Return button */
        returnBtn.setOnClickListener(view -> {
            intentClass();
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
            LanguageHelper.setLocale(this , "gr");
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

    private void intentClass() {
        savePreference();

        Intent intent = new Intent(getApplicationContext(), classToIntent);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentClass();
    }
}





