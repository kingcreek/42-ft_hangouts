package es.kingcreek.ft_hangouts.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.TypedValue;

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.kingcreek.ft_hangouts.R;

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

    public static void changeAppThemeColor(Context context, int color) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        typedValue.data = color;
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true);

        // Crear una nueva configuración con el tema actualizado
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK; // Limpiar el modo noche
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    /*
    public static void changeAppThemeColor2(Application application, int color) {
        Resources.Theme theme = application.getTheme();

        // Crear un nuevo tema basado en el tema actual
        Resources.Theme newTheme = application.getResources().newTheme();
        newTheme.setTo(theme);

        // Aplicar el nuevo color al nuevo tema
        TypedValue typedValue = new TypedValue();
        typedValue.data = color;
        newTheme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true);

        // Guardar el nuevo tema en la aplicación
        application.setTheme(newTheme.getResId());
    }

    public static void changeAppThemeColor(Context context, int color) {
        // Obtener el tema actual de la aplicación
        Resources.Theme appTheme = context.getTheme();

        // Crear un nuevo tema
        Resources.Theme newTheme = context.getResources().newTheme();
        newTheme.setTo(appTheme);

        // Aplicar el nuevo color al tema
        TypedValue typedValue = new TypedValue();
        typedValue.data = color;
        newTheme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true);

        // Cambiar el tema de la aplicación
        context.setTheme(newTheme.getResId());

        // Reiniciar la aplicación
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

     */
}
