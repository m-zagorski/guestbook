package com.appunite.guestbook.dagger;

import com.appunite.guestbook.BaseFragment;
import com.appunite.guestbook.EditProfileFragment;
import com.appunite.guestbook.EmailLoginFragment;
import com.appunite.guestbook.EntriesFragment;
import com.appunite.guestbook.LoginFragment;
import com.appunite.guestbook.SignupFragment;

import dagger.Module;

@Module(
        injects = {
                EntriesFragment.class,
                LoginFragment.class,
                EmailLoginFragment.class,
                SignupFragment.class,
                EditProfileFragment.class
        },
        addsTo = ActivityModule.class,
        library = true
)
public class FragmentModule {

    private final BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }
}
