package com.mike.givemewingzz.bbvamaps.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Location extends RealmObject {

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("lng")
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
