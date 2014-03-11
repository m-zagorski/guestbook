package com.appunite.guestbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.appunite.guestbook.dagger.ActivityGraphProvider;
import com.appunite.guestbook.dagger.FragmentModule;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

import static com.google.common.base.Preconditions.checkNotNull;

public class BaseFragment extends Fragment {

    private ObjectGraph mFragmentGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            final ActivityGraphProvider graphProvider = checkNotNull((ActivityGraphProvider) getActivity());
            mFragmentGraph = graphProvider.getActivityGraph().plus(new FragmentModule(this));
        } catch (ClassCastException e) {
            throw new RuntimeException("Activity does not implement ActivityGraphProvider", e);
        }

        mFragmentGraph.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }
}
