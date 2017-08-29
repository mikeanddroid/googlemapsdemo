package com.mike.givemewingzz.bbvamaps.presenter;

import android.util.Log;

import com.mike.givemewingzz.bbvamaps.base.BBVAApplication;
import com.mike.givemewingzz.bbvamaps.model.data.BaseModel;
import com.mike.givemewingzz.bbvamaps.service.ApiCall.FetchBBVAData;
import com.mike.givemewingzz.bbvamaps.service.RealmController;

import io.realm.Realm;

public class SearchInteractorImplementation implements SearchInteractor, FetchBBVAData.OnResultsComplete {

    protected Realm realm;
    private static final String TAG = SearchInteractorImplementation.class.getSimpleName();

    @Override
    public void findRequest(final OnSearchFinished listener) {

        realm = RealmController.with(BBVAApplication.getInstance()).getRealm();

        FetchBBVAData fetchBBVAData = new FetchBBVAData();
        fetchBBVAData.call(this);

    }

    @Override
    public void onResultsFetched(OnSearchFinished listener, BaseModel model) {
        Log.d(TAG, " SearchInteractorImplementation Results : Size : " + model.getStatus());
        listener.onFinished(model);
    }
}
