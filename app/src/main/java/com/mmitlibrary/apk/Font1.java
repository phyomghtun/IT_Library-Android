package com.mmitlibrary.apk;

import android.app.Application;

public class Font1 extends Application {

    public void onCreate() {
       TypefaceUtils.overrideFont(getApplicationContext(),"SERIF","zawgyi.ttf");
        super.onCreate();
    }
}
