package model;

public class Preference {
    // User preferences initialization
    private boolean notifications;
    private boolean voiceInstructions;
    private boolean voiceCommands;

    // As Greek is the default language, true corresponds
    // to this and false to English.
    private boolean language;

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

    public String getLanguageName() {
        if (getLanguage()) {
            return "gr";
        }
        return "en";
    }
}
