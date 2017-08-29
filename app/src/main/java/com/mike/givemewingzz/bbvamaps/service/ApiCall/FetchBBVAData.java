package com.mike.givemewingzz.bbvamaps.service.ApiCall;

import android.util.Log;

import com.mike.givemewingzz.bbvamaps.model.data.BaseModel;
import com.mike.givemewingzz.bbvamaps.model.data.Results;
import com.mike.givemewingzz.bbvamaps.presenter.SearchInteractor;
import com.mike.givemewingzz.bbvamaps.service.BaseClient;
import com.mike.givemewingzz.bbvamaps.service.OttoHelper;
import com.mike.givemewingzz.bbvamaps.service.RetrofitInterface;
import com.mike.givemewingzz.bbvamaps.utils.AppConstants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FetchBBVAData {

    private static final String TAG = FetchBBVAData.class.getSimpleName();

    public void call(final OnResultsComplete onResultsComplete) {

        RetrofitInterface retrofitInterface = BaseClient.getBBSIClient();

        Call<BaseModel> call = retrofitInterface.getBBVALocations(AppConstants.API_KEYS.MAP_API_KEY);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(final Response<BaseModel> response, Retrofit retrofit) {

                Log.d(TAG, " Response : Size : " + response.body().getResults().size() + " : Response Code : " + response.code());

                List<Results> resultsList = response.body().getResults();

                final int resultSize = resultsList.size();

                Log.d(TAG, " Response Results : Size : " + resultSize);

                for (int i = 0; i < resultSize; i++) {
                    Log.d(TAG, "Fetch BBVA Data Results : Formatted Address :" + resultsList.get(i).getFormattedAddress());
                }

                try {

                    Realm realm = Realm.getDefaultInstance();

                    final BaseModel baseModel = response.body();
                    RealmList<Results> results = baseModel.getResults();
                    realm.beginTransaction();
                    realm.deleteAll();

                    if (baseModel != null) {
                        realm.copyToRealm(baseModel);
                        realm.copyToRealmOrUpdate(resultsList);
                    }

                    realm.commitTransaction();

                    onResultsComplete.onResultsFetched(new SearchInteractor.OnSearchFinished() {
                        @Override
                        public void onFinished(BaseModel items) {
                            OttoHelper.post(new SuccessEvent(items, response));
                        }
                    }, baseModel);

                } catch (NullPointerException npe) {
                    Log.e(TAG + ":: Error :: ", "Missing element somewhere in location response", npe);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, " Response Failure : " + t.getLocalizedMessage());
                OttoHelper.post(new FailureEvent(t.getMessage()));
            }
        });

    }

    public interface OnResultsComplete {
        void onResultsFetched(SearchInteractor.OnSearchFinished listener, BaseModel baseModel);
    }

    public static class SuccessEvent {
        private BaseModel baseModel;
        private Response response;

        private SuccessEvent(BaseModel baseModel, Response response) {
            this.baseModel = baseModel;
            this.response = response;
        }

        public BaseModel getBaseModel() {
            return baseModel;
        }

        public Response getResponse() {
            return response;
        }
    }

    public static class FailureEvent {

        private String errorMessage;

        private FailureEvent(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

}
