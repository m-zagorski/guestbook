package com.appunite.guestbook;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ApiLoaderFragment<R> extends BaseFragment
implements LoaderManager.LoaderCallbacks<R>, ErrorHelper.OnForceReloadListener {

    public static final int LOADER_MAIN = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onCreateChildView(inflater, container, savedInstanceState);
        setupErrorHelper(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCreatedChildView(view, savedInstanceState);

        if(initLoaderAtStart()){
            showProgress();
            getLoaderManager().initLoader(LOADER_MAIN, null, this);
        }
    }

    @Override
    public Loader<R> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case LOADER_MAIN:
                return onCreateMainLoader(bundle);
            default:
                throw new RuntimeException("Unknown loader id: " +id);
        }
    }

    @Override
    public void onLoadFinished(Loader<R> loader, R result) {
        final int id = loader.getId();
        switch(id){
            case LOADER_MAIN:
                onLoadMainFinished(result);
                updateLoadMore();
                return;
            default:
                throw new RuntimeException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoaderReset(Loader<R> loader) {
    }

    @Override
    public void onForceReload() {
        forceReload();
    }

    protected void forceReload(){
        showProgress();
        getLoaderManager().restartLoader(LOADER_MAIN, null, this);
    }

    protected boolean hasMoreData() {
        return false;
    }

    protected boolean isScrolledToTheLast(){
        return false;
    }

    protected void updateLoadMore() {
        if(!isResumed()) {
            return;
        }
        final boolean hasMoreData = hasMoreData();
        loadMoreIfNeeded();
    }

    protected void showLoadMoreStatus(boolean hasMoreData) {

    }

    private void loadMoreIfNeeded() {
        final Loader<Object> loader = getLoaderManager().getLoader(LOADER_MAIN);
        if(!loader.isStarted()) return;
        if(!hasMoreData()) return;
        if(!isScrolledToTheLast()) return;
        showLoadMoreStatus(hasMoreData());
        getLoaderManager().restartLoader(LOADER_MAIN, null, this);
    }

    protected boolean initLoaderAtStart(){
        return true;
    }

    protected abstract void showProgress();

    protected abstract View onCreateChildView(LayoutInflater inflater,
                                              ViewGroup container,
                                              Bundle savedInstanceState);

    protected abstract void setupErrorHelper(View view);

    protected void onCreatedChildView(View view, Bundle savedInstanceState){};

    protected abstract Loader<R> onCreateMainLoader(Bundle bundle);

    protected abstract void onLoadMainFinished(R result);

    protected abstract void onLoadMainReset();
}
