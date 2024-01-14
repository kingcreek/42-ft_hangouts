package es.kingcreek.ft_hangouts.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class PreferenceHelper {

    private final SharedPreferences preferences;
    private static PreferenceHelper instance;

    public PreferenceHelper(Context context) {
        this.preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void saveToolbarColor(int color) {
        preferences.edit().putInt(Constants.KEY_TOOLBAR_COLOR, color).apply();
    }

    public int getToolbarColor() {
        return preferences.getInt(Constants.KEY_TOOLBAR_COLOR, /*Default color*/ -29132);
    }

    public void setDarkMode(boolean isDarkMode) {
        preferences.edit().putBoolean(Constants.KEY_IS_DARK_MODE, isDarkMode).apply();
    }

    public boolean isDarkMode() {
        return preferences.getBoolean(Constants.KEY_IS_DARK_MODE, true);
    }

    public void setBonusActive(boolean isBonusActive) {
        preferences.edit().putBoolean(Constants.KEY_IS_BONUS_ACTIVE, isBonusActive).apply();
    }

    public boolean isBonusActive() {
        return preferences.getBoolean(Constants.KEY_IS_BONUS_ACTIVE, false);
    }

    public void saveLastTime(String lastTime) {
        preferences.edit().putString(Constants.KEY_LAST_TIME, lastTime).apply();
    }

    public String getLastTime() {
        return preferences.getString(Constants.KEY_LAST_TIME, "");
    }
}