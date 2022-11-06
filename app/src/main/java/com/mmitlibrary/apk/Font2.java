package com.mmitlibrary.apk;

import android.app.Application;

public class Font2 extends Application {
    public void onCreate() {
        TypefaceUtils.overrideFont(getApplicationContext(),"SERIF","unicode.ttf");
        super.onCreate();
    }
}
