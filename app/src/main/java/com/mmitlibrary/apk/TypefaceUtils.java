package com.mmitlibrary.apk;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Asus on 10/3/2018.
 */
public class TypefaceUtils {
    public static void overrideFont(Context context,String defaultFontNameToOverride,String customFontFileInAssets){
       try {
           final Typeface customFontTypeface=Typeface.createFromAsset(context.getAssets(),customFontFileInAssets);
           final Field defaultFontTypefaceField=Typeface.class.getDeclaredField(defaultFontNameToOverride);
           defaultFontTypefaceField.setAccessible(true);
           defaultFontTypefaceField.set(null,customFontTypeface);
       } catch (Exception e) {
           e.printStackTrace();
       }

    }
    }

