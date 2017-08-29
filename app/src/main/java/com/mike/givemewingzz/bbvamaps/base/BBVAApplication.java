package com.mike.givemewingzz.bbvamaps.base;

import android.app.Application;

import com.mike.givemewingzz.bbvamaps.utils.DBHelper;

import io.realm.Realm;

/**
 * Created by GiveMeWingzz on 8/24/2017.
 */

public class BBVAApplication extends Application {

    public static BBVAApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Init Realm DB
        instance = this;
        Realm.init(this);
        Realm.setDefaultConfiguration(DBHelper.getRealmConfig(this));

    }

    public static BBVAApplication getInstance() {
        return instance;
    }

}
