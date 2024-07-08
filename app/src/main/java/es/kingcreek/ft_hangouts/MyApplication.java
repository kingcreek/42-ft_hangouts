package es.kingcreek.ft_hangouts;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.ProcessLifecycleOwner;

import es.kingcreek.ft_hangouts.observer.AppLifecycleObserver;


public class MyApplication extends Application {
    public static boolean comingFromActivity = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
    }
}