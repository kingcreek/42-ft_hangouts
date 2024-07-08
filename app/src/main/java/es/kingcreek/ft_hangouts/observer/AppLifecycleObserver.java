package es.kingcreek.ft_hangouts.observer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.app.Application;
import android.widget.Toast;

import es.kingcreek.ft_hangouts.MyApplication;
import es.kingcreek.ft_hangouts.helper.Helper;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;


public class AppLifecycleObserver implements LifecycleObserver {
    private boolean isReturningFromBackground = false;
    private Application application;

    public AppLifecycleObserver(Application application) {
        this.application = application;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        if (isReturningFromBackground && !MyApplication.comingFromActivity) {
            PreferenceHelper preferences = PreferenceHelper.getInstance(application.getApplicationContext());
            Toast.makeText(application, preferences.getLastTime(), Toast.LENGTH_SHORT).show();
        }
        isReturningFromBackground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        PreferenceHelper.getInstance(application.getApplicationContext()).saveLastTime(Helper.getCurrentDateTimeString());
        isReturningFromBackground = true;
    }
}
