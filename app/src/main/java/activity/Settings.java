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
                setLanguage(true);
            }
            else {
                setLanguage(false);
            }

        });

        /* GREEK SWITCER */
        greekSwitcher.setOnClickListener(view -> {
            if (greekSwitcher.isChecked()) {
                setLanguage(false);
            }
            else {
                setLanguage(true);
            }
        });

        /* Return button */
        returnBtn.setOnClickListener(view -> {
            // go in the previous page
            preference.setNotifications(notificationsSwitcher.isChecked());
            preference.setVoiceInstructions(voiceInstructionsSwitcher.isChecked());
            preference.setVoiceCommands(voiceCommandsSwitcher.isChecked());
            preference.setLanguage(greekSwitcher.isChecked());
            try {
                PreferenceDAO.getInstance(this).savePreference(preference);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), classToIntent);
            startActivity(intent);
            finish();
        });
    }
    private void setSwitchers(Preference preference) {
        if (preference == null) {
            setLanguage(Locale.getDefault().getLanguage().equals("en"));
        }
        else {
            notificationsSwitcher.setChecked(preference.getNotifications());
            voiceInstructionsSwitcher.setChecked(preference.getVoiceInstructions());
            voiceCommandsSwitcher.setChecked(preference.getVoiceCommands());
            setLanguage(!preference.getLanguage());
        }
    }

    private void setLanguage( boolean language) {
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
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
    }

}





