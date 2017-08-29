package com.mike.givemewingzz.bbvamaps.presenter;

import com.mike.givemewingzz.bbvamaps.model.data.BaseModel;

public interface SearchInteractor {

    interface OnSearchFinished {
        void onFinished(BaseModel items);
    }

    void findRequest(OnSearchFinished listener);

}
