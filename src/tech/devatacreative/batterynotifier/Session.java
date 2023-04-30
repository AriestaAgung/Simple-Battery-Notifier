package tech.devatacreative.batterynotifier;

import java.util.prefs.Preferences;

public class Session {
    Preferences prefs = Preferences.userRoot().node("tech/devatacreative/batterynotifier/prefs");

    public void setPrefs(){
        prefs.putBoolean("runbg", false);
        if (new MainForm().bgCheckbox.isSelected()) {
            prefs.putBoolean("runbg", true);
        } else if (! new MainForm().bgCheckbox.isSelected()){
            prefs.putBoolean("runbg", false);
        }
    }
}
