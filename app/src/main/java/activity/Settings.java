package activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.aueb.idry.R;

public class Settings extends AppCompatActivity {

    Switch NotifSwitcher = findViewById(R.id.switchsettnot);
    Switch VoiceInstrSwitcher = findViewById(R.id.switchsettVI);
    Switch VoiceCommandsSwitcher =findViewById(R.id.switchSettVC);
    Switch EnglishSwitcher = findViewById(R.id.switchsetEN);
    Switch GreekSwitcher = findViewById(R.id.switchSettGr);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        NotifSwitcher.setChecked(true);
        VoiceInstrSwitcher.setChecked(false);
        VoiceCommandsSwitcher.setChecked(false);
        GreekSwitcher.setChecked(true);
        EnglishSwitcher.setChecked(false);
    }

//    private void LanguageButtons(){
//        if (GreekSwitcher.isChecked()){
//            EnglishSwitcher.setChecked(false);
//        }
//        else if(EnglishSwitcher.isChecked()) {
//            GreekSwitcher.setChecked(false);
//        }
//    }
//    public boolean NotificationsOn(){
//        return NotifSwitcher.isChecked();
//    }
//
//    public boolean VoiceInstructionOn(){
//        return VoiceInstrSwitcher.isChecked();
//    }
//
//    public boolean VoiceCommnadsOn(){
//        return VoiceCommandsSwitcher.isChecked();
//    }

}

