package com.appunite.guestbook.content;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.appunite.guestbook.dagger.ForActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPreferences {

    public static class AppPreferencesEditor {
        private Editor mEditor;

        AppPreferencesEditor(SharedPreferences preferences) {
            mEditor = preferences.edit();
        }

        public boolean commit() {
            return mEditor.commit();
        }

        public AppPreferencesEditor clear() {
            mEditor.clear();
            return this;
        }

        public AppPreferencesEditor setLoggedIn(boolean isLoggedIn) {
            mEditor.putBoolean(IS_LOGGED_IN, isLoggedIn);
            return this;
        }

        public AppPreferencesEditor setUserGuid(String userGuid) {
            mEditor.putString(USER_GUID, userGuid);
            return this;
        }

        public AppPreferencesEditor setUserEmail(String userEmail) {
            mEditor.putString(USER_EMAIL, userEmail);
            return this;
        }

        public AppPreferencesEditor setUserName(String userName) {
            mEditor.putString(USER_NAME, userName);
            return this;
        }

        public AppPreferencesEditor setUserPhoto(String userPhoto) {
            mEditor.putString(USER_PHOTO, userPhoto);
            return this;
        }

    }

    private static final String PREFERENCES_NAME = "app";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String USER_GUID = "user_guid";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NAME = "user_name";
    private static final String USER_PHOTO = "user_photo";

    private SharedPreferences mPreferences;

    @Inject
    public UserPreferences(@ForActivity Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public AppPreferencesEditor edit() {
        return new AppPreferencesEditor(mPreferences);
    }

    public boolean isLoggedIn() {
        return mPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUserGuid() {
        return mPreferences.getString(USER_GUID, null);
    }

    public String getUserEmail() {
        return mPreferences.getString(USER_EMAIL, null);
    }

    public String getUserName() {
        return mPreferences.getString(USER_NAME, null);
    }

    public String getUserPhoto() {
        return mPreferences.getString(USER_PHOTO, "");
    }

}
