package com.cypherics.palnak.cipher.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by palnak on 23-1-18.
 */

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }


    public void saveLockedApps(Context context, List<String> lockedApps) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(lockedApps);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void addApp(Context context, String appName) {
        List<String> lockedApp = getApp(context);
        if (lockedApp == null)
            lockedApp = new ArrayList<String>();
        lockedApp.add(appName);
        saveLockedApps(context, lockedApp);
    }


    public ArrayList<String> getApp(Context context) {
        SharedPreferences settings;
        List<String> lockedApp;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,
                    String[].class);

            lockedApp = Arrays.asList(favoriteItems);
            lockedApp = new ArrayList<String>(lockedApp);
        } else
            return null;

        return (ArrayList<String>) lockedApp;
    }

}
