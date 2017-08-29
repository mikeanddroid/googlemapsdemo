package com.mike.givemewingzz.bbvamaps.model;

import com.mike.givemewingzz.bbvamaps.model.data.BaseModel;

public interface UIHandler {

    void showProgress();

    void hideProgress();

    void setItems(BaseModel items);

    void onDataComplete();

    void showMessage(String message);

}
