package com.mike.givemewingzz.bbvamaps.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mike.givemewingzz.bbvamaps.R;
import com.squareup.picasso.Picasso;

import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.IMAGE_URL_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_ADDRESS_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_GEO_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_NAME_KEY;
import static com.mike.givemewingzz.bbvamaps.utils.AppConstants.LOCATION_RATING_KEY;

/**
 * Created by GiveMeWingzz on 8/26/2017.
 */

public class MapLocationDetails extends AppCompatActivity {

    private static final String TAG = MapLocationDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_details);

        final String locationImageUrl = getIntent().getStringExtra(IMAGE_URL_KEY);
        final String locationGeo = getIntent().getStringExtra(LOCATION_GEO_KEY);
        final String locationName = getIntent().getStringExtra(LOCATION_NAME_KEY);
        final String locationAddress = getIntent().getStringExtra(LOCATION_ADDRESS_KEY);
        final String locationRating = getIntent().getStringExtra(LOCATION_RATING_KEY);

        Log.i(TAG, " Location Geo  :  " + locationGeo != null ? locationGeo : " locationGeo Null ");
        Log.i(TAG, " Location Name :  " + locationName != null ? locationName : " locationName Null ");
        Log.i(TAG, " Location Add  :  " + locationAddress != null ? locationAddress : " locationAddress Null ");
        Log.i(TAG, " Location ImageUrl  :  " + locationImageUrl != null ? locationImageUrl : " Location Url Null ");

        ImageView locationImageIv = (ImageView) findViewById(R.id.locationDetailsImageView);
        TextView locationNameTv = (TextView) findViewById(R.id.locationDetailsName);
        TextView locationGeoTv = (TextView) findViewById(R.id.locationDetailsGeo);
        TextView locationAddTv = (TextView) findViewById(R.id.locationDetailsAddress);
        TextView locationRatingTv = (TextView) findViewById(R.id.locationDetailsRating);

        Button navigateMaps = (Button) findViewById(R.id.locationDetailsNav);

        Picasso.with(this).load(locationImageUrl).into(locationImageIv);

        locationGeoTv.setText("Location Geo  :  " + locationGeo != null ? locationGeo : " Location Geo Unavailable ");
        locationNameTv.setText("Location Name :  " + locationName != null ? locationName : " Location Name Unavailable ");
        locationAddTv.setText("Location Add  :  " + locationAddress != null ? locationAddress : " Location Address Unavailable ");
        locationRatingTv.setText("Location Rating  :  " + locationRating != null ? locationRating : "Location Rating Unavailable");

        navigateMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + locationGeo));
                startActivity(intent);
            }
        });
    }
}
