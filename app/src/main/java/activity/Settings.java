package activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.aueb.idry.R;

import java.util.Locale;

public class Settings extends AppCompatActivity {
    boolean notification;
    boolean VOICE_INSTRUCTION;
    boolean VOICE_COMMANDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.settings);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch NotifSwitcher = findViewById(R.id.switchsettnot);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch VoiceInstrSwitcher = findViewById(R.id.switchsettVI);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch VoiceCommandsSwitcher = findViewById(R.id.switchSettVC);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch EnglishSwitcher = findViewById(R.id.switchsetEN);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch GreekSwitcher = findViewById(R.id.switchSettGr);
        Button returnn = findViewById(R.id.returnfromsettings);
        super.onCreate(savedInstanceState);
        NotifSwitcher.setChecked(true);
        VoiceInstrSwitcher.setChecked(false);
        VoiceCommandsSwitcher.setChecked(false);
        if(Locale.getDefault().getLanguage().equals("en")) {
            GreekSwitcher.setChecked(false);
            EnglishSwitcher.setChecked(true);
            
        }else{
            GreekSwitcher.setChecked(true);
            EnglishSwitcher.setChecked(false);
        }


        /* ENGLISH SWITCER */
        EnglishSwitcher.setOnClickListener(view -> {
            if (EnglishSwitcher.isChecked()) {
                GreekSwitcher.setChecked(false);
                lan( true );
            }else{
                GreekSwitcher.setChecked(true);
                lan( false);
            }
        });

        /* GREEK SWITCER */
        GreekSwitcher.setOnClickListener(view -> {
            if (GreekSwitcher.isChecked()){
                EnglishSwitcher.setChecked(false);
                lan( false);
            }
            else{
                EnglishSwitcher.setChecked(true);
                lan( true);
            }
        });

        /* NOTIFICATIONS SWITCER */
        NotifSwitcher.setOnClickListener(view -> setNotification(NotifSwitcher.isChecked()));

        /* VOICE INSTRUCTIONS SWITCER */
        VoiceInstrSwitcher.setOnClickListener(view -> setVOICE_INSTRUCTION(VoiceInstrSwitcher.isChecked()));

        /* VOICE COMMANDS SWITCER */
        VoiceCommandsSwitcher.setOnClickListener(view -> setVOICE_COMMANDS(VoiceCommandsSwitcher.isChecked()));


        /* returnn button */
        returnn.setOnClickListener(view -> {
            // go in the previous page
            finish();
        });
    }

    private void lan( boolean b ) {
        /* change language? */
        if (b) {
            LanguageHelper.setLocale(this, "en");
        } else {
            LanguageHelper.setLocale(this , "gr");
        }
    }

    public boolean NotificationOn(){
        return notification;
    }

    private void setNotification(boolean a){
        notification=a;
    }

    private void setVOICE_INSTRUCTION(boolean a) {
        VOICE_INSTRUCTION = a;
    }

    public boolean VOICE_INSTRUCTIONOn(){
        return VOICE_INSTRUCTION;
    }

    public boolean VOICE_COMMANDSOn(){
        return VOICE_COMMANDS;
    }

    private void setVOICE_COMMANDS(boolean a) {
        VOICE_COMMANDS = a;
    }

}





