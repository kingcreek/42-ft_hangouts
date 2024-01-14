package es.kingcreek.ft_hangouts.helper;

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static void changeToolbarColor(Toolbar toolbar, int selectedColor) {
        toolbar.setBackgroundColor(selectedColor);
    }

    public static String getCurrentDateTimeString() {
        Date currentDate = new Date();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(currentDate);
    }
}
