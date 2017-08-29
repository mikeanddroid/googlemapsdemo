package com.mike.givemewingzz.bbvamaps.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mike.givemewingzz.bbvamaps.R;
import com.mike.givemewingzz.bbvamaps.adapters.LocationListAdapter;
import com.mike.givemewingzz.bbvamaps.adapters.realmutils.RealmResultsAdapter;
import com.mike.givemewingzz.bbvamaps.adapters.realmutils.ResultsAdapter;
import com.mike.givemewingzz.bbvamaps.model.UIHandler;
import com.mike.givemewingzz.bbvamaps.model.data.BaseModel;
import com.mike.givemewingzz.bbvamaps.model.data.Results;
import com.mike.givemewingzz.bbvamaps.presenter.ActionPresenter;
import com.mike.givemewingzz.bbvamaps.presenter.ActionPresenterImplementation;
import com.mike.givemewingzz.bbvamaps.presenter.SearchInteractorImplementation;
import com.mike.givemewingzz.bbvamaps.service.ApiCall.FetchBBVAData;
import com.mike.givemewingzz.bbvamaps.service.OttoHelper;
import com.mike.givemewingzz.bbvamaps.service.RealmController;
import com.mike.givemewingzz.bbvamaps.utils.Prefs;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.IMAGE_URL_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_ADDRESS_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_GEO_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_NAME_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_RATING_KEY;

/**
 * Created by GiveMeWingzz on 8/25/2017.
 */
public class MapListFragment extends Fragment implements LocationListAdapter.EventListener, UIHandler, ResultsAdapter.EventListener {

    public static final String TAG = MapListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private LocationListAdapter locationListAdapter;
    protected Realm realm;

    private ProgressDialog progressDialog;
    private ActionPresenter presenter;

    private ResultsAdapter resultsAdapter;

    public static MapListFragment newInstance() {
        return new MapListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = RealmController.with(getActivity()).getRealm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data");
        progressDialog.setTitle("Please wait");
        presenter = new ActionPresenterImplementation(this, new SearchInteractorImplementation());

        setupRecyclerView();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // refresh the realm instance
                RealmController.with(getActivity()).refresh();

                // get all persisted objects
                // create the helper adapter and notify data set changes
                // changes will be reflected automatically
                setRealmAdapter(RealmController.with(getActivity()).getResults());
            }
        }, 400);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        // Register Otto
        OttoHelper.register(this);

        presenter.onResume();

        BaseModel model = null;
        if (!Prefs.with(getActivity()).getPreLoad()) {
            model = RealmController.with(getActivity()).getBaseModel();

            if (model != null) {
                Log.d(TAG, "onResume : Map List : Status : Pre-loaded CACHED " + model.getStatus());
            } else {
                Log.d(TAG, "onResume : Map List : Status : Pre-loaded NOT CACHED : Network Call");
                setResultsData();
            }

        } else {
            Log.d(TAG, "onResume : Map List : Status : NETWORK CALL");
            // Create default request for fetching bbva details
            setResultsData();
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        // UnRegister Otto
        OttoHelper.unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Subscribe
    public void onResultSuccess(FetchBBVAData.SuccessEvent successEvent) {
        Log.d(TAG, "onResultSuccess : MapList : Status : " + successEvent.getBaseModel().getStatus());
        setItems(successEvent.getBaseModel());
    }

    @Subscribe
    public void onResultFailure(FetchBBVAData.FailureEvent failureEvent) {
        Log.d(TAG, " onResultFailure : Base Model : Status : " + failureEvent.getErrorMessage());
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        resultsAdapter = new ResultsAdapter(getActivity());
        recyclerView.setAdapter(resultsAdapter);
    }

    private void setResultsData() {
        presenter.onResultsFetch();
        Prefs.with(getActivity()).setPreLoad(true);
    }

    @Override
    public void onItemClick(final int position, Results results) {

        // Navigate to details view
        Intent detailsIntent = new Intent(getActivity(), MapLocationDetails.class);
        detailsIntent.putExtra(IMAGE_URL_KEY, results.getIcon());
        detailsIntent.putExtra(LOCATION_GEO_KEY, results.getGeometry().getPlaceLocation().toString());
        detailsIntent.putExtra(LOCATION_NAME_KEY, results.getLocationName());
        detailsIntent.putExtra(LOCATION_ADDRESS_KEY, results.getFormattedAddress());
        detailsIntent.putExtra(LOCATION_RATING_KEY, results.getRating());
        getActivity().startActivity(detailsIntent);

        presenter.onItemClicked(position);

    }

    @Override
    public void showProgress() {
        if (isAdded()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onDataComplete() {
        progressDialog.dismiss();
    }

    @Override
    public void setItems(final BaseModel items) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                RealmList<Results> resultsRealmList = null;
                if (items != null) {
                    Log.d(TAG, " Fragment : Base Model : MVP : Status : " + items.getStatus());
                    resultsRealmList = items.getResults();
                }

                RealmResults<Results> realmResults = realm.where(Results.class).findAll();

                if (resultsRealmList != null) {
                    for (Results results : resultsRealmList) {
                        Log.d(TAG, " Fragment : Results Model : MVP : address : " + results.getFormattedAddress());

                        final String locationTitle = results.getLocationName();
                        final String rating = results.getRating();
                        final String formattedAddress = results.getFormattedAddress();

                        final String imageUrl = results.getIcon();

                        Log.d(TAG, " Fragment : Results Model : Location Icon : " + imageUrl);
                        Log.d(TAG, " Fragment : Results Model : Location Title : " + locationTitle);
                        Log.d(TAG, " Fragment : Results Model : Location Rating : " + rating);
                        Log.d(TAG, " Fragment : Results Model : Location Address : " + formattedAddress);

                    }
                }

                setRealmAdapter(realmResults);
                progressDialog.hide();
            }
        }, 500);

    }

    public void setRealmAdapter(RealmResults<Results> realmResults) {

        RealmResultsAdapter realmResultsAdapter = new RealmResultsAdapter(getActivity(), realmResults, true);
        resultsAdapter.setEventListener(this);
        resultsAdapter.setRealmAdapter(realmResultsAdapter);
        resultsAdapter.notifyDataSetChanged();

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
