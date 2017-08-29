package com.mike.givemewingzz.bbvamaps.service;

import android.util.Log;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by GiveMeWingzz on 8/24/2017.
 */

public class SearchInterceptor {

    public static final String TAG = SearchInterceptor.class.getSimpleName();

    public static class BaseUrlInterceptor extends BaseRequestInterceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // Take each request and chain query params
            Log.d(TAG, " Request : " + chain.request().url());

            return null;
        }
    }

}
