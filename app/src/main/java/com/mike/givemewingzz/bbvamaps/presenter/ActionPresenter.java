package com.mike.givemewingzz.bbvamaps.presenter;

public interface ActionPresenter {

    void onItemClicked(final int position);

    void onResultsFetch();

    void onResume();

    void onDestroy();

}
