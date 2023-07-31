package com.lmkr.prmscemployeeapp;

import static cat.ereza.customactivityoncrash.config.CaocConfig.BACKGROUND_MODE_SILENT;

import android.app.Application;
import android.database.CursorWindow;

import java.lang.reflect.Field;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class App extends Application {
    private static App INSTANCE = null;

    public static synchronized App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initCustomActivityOnCrashLibrary();
        increaseDBCursorSize();
    }

    private void increaseDBCursorSize() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCustomActivityOnCrashLibrary() {
        CaocConfig.Builder.create().backgroundMode(BACKGROUND_MODE_SILENT).apply();

        /*
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(false) //default: true
                .showErrorDetails(false) //default: true
                .showRestartButton(false) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
                .restartActivity(null) //default: null (your app's launch activity)
                .errorActivity(null) //default: null (default error activity)
                .eventListener(null) //default: null
                .apply();
*/
    }

}
