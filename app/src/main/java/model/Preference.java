package model;

/**
 * Class containing the user's settings options. It stores the chosen language, notification settings,
 * text-to-speech instructions and voice commands.
 */
public class Preference {
    private boolean notifications;
    private boolean voiceInstructions;
    private boolean voiceCommands;

    // As Greek is the default language, true corresponds
    // to this and false to English.
    private boolean language;

    /**
     * Constructor with all private variables as arguments.
     *
     * @param notifications boolean for activating notifications
     * @param voiceInstructions boolean for activating text-to-speech instructions
     * @param voiceCommands boolean for activating voice commands
     * @param language true indicates Greek, false indicates English
     */
    public Preference(boolean notifications, boolean voiceInstructions,
                      boolean voiceCommands, boolean language) {
        this.notifications = notifications;
        this.voiceInstructions = voiceInstructions;
        this.voiceCommands = voiceCommands;
        this.language = language;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public void setVoiceInstructions(boolean voiceInstructions) {
        this.voiceInstructions = voiceInstructions;
    }

    public void setVoiceCommands(boolean voiceCommands) {
        this.voiceCommands = voiceCommands;
    }

    public void setLanguage(boolean language) {
        this.language = language;
    }

    public boolean getNotifications() {
        return this.notifications;
    }

    public boolean getVoiceInstructions() {
        return this.voiceInstructions;
    }

    public boolean getVoiceCommands() {
        return this.voiceCommands;
    }

    public boolean getLanguage() {
        return this.language;
    }

    /**
     * Return the selected language's code.
     *
     * @return the selected language's code
     */
    public String getLanguageName() {
        if (getLanguage()) {
            return "el";
        }
        return "en";
    }
}
