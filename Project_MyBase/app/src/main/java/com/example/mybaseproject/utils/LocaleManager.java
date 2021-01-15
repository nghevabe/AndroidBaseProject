package com.example.mybaseproject.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context c) {
        return setNewLocale(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        Locale locale = null;
        String languageToLoad = "";
        return "en";
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

}
