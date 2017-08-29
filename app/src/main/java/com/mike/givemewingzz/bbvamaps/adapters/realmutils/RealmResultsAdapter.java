package com.mike.givemewingzz.bbvamaps.adapters.realmutils;

import android.content.Context;

import com.mike.givemewingzz.bbvamaps.model.data.Results;

import io.realm.RealmResults;

/**
 * Created by GiveMeWingzz on 8/28/2017.
 */

public class RealmResultsAdapter extends RealmModelAdapter<Results> {

    public RealmResultsAdapter(Context context, RealmResults<Results> resultsRealmResults, boolean automaticUpdate) {
        super(context, resultsRealmResults, automaticUpdate);
    }

}
