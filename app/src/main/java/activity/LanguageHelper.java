package activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static void onCreate(Context context) {

        String lang;
        if (getLanguage(context).isEmpty()) {
            lang = getPersistedData(context, Locale.getDefault().getLanguage());
        } else {
            lang = getLanguage(context);
        }

        setLocale(context, lang);
    }
    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(SELECTED_LANGUAGE, defaultLanguage);
    }
    public static void onCreate(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        setLocale(context, lang);
    }
    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }
    public static void setLocale(Context cont, String lang) {
        persist(cont, lang);
        updateResources(cont, lang);
    }
    private static void updateResources(Context cont, String lang) {
        Locale loc = new Locale(lang);
        Locale.setDefault(loc);
        Resources resources = cont.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = loc;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
    private static void persist(Context context, String language) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(SELECTED_LANGUAGE, language);
        ed.apply();
    }
}