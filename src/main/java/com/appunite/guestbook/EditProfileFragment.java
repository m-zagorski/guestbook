package com.appunite.guestbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.appunite.guestbook.content.UserPreferences;
import com.appunite.guestbook.helpers.FormValidator;
import com.appunite.guestbook.helpers.PhotoChooserHelper;
import com.appunite.guestbook.helpers.data.Result;
import com.google.common.base.Optional;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditProfileFragment extends ErrorHelperApiLoaderFragment<Result<String>> {

    private static final int REQ_CODE_PICK_IMAGE = 0;

    @InjectView(R.id.profile_photo)
    ImageView mProfilePhoto;
    @InjectView(R.id.email)
    EditText mEmail;
    @InjectView(R.id.username)
    EditText mUsername;

    @Inject
    PhotoChooserHelper mPhotoChooserHelper;
    @Inject
    Picasso mPicasso;
    @Inject
    UserPreferences mAppPreferences;

    private Uri mImageUri;
    private FormValidator mFormValidator;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    protected boolean isScreenEmpty(Result<String> result) {
        return false;
    }

    @Override
    protected View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.profile_change_fragment, container, false));
    }

    @Override
    protected void onCreatedChildView(View view, Bundle savedInstanceState) {
        super.onCreatedChildView(view, savedInstanceState);
        loadUserInformation();
        mFormValidator = new FormValidator(getActivity(), view);
    }

    @Override
    protected Loader<Result<String>> onCreateMainLoader(Bundle bundle) {
        return null;
    }

    @Override
    protected void onLoadMainReset() {
    }

    @Override
    protected boolean initLoaderAtStart() {
        return false;
    }

    private void loadUserInformation() {
        mEmail.setText(mAppPreferences.getUserEmail());
        mUsername.setText(mAppPreferences.getUserName());
        mImageUri = Uri.parse(mAppPreferences.getUserPhoto());
        loadImage();
    }

    private void loadImage() {
        mPicasso.load(mImageUri)
                .fit()
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .into(mProfilePhoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_PICK_IMAGE:
                final Optional<Uri> uri = mPhotoChooserHelper.onResult(resultCode, data);
                if (uri.isPresent()) {
                    mImageUri = uri.get();
                    loadImage();
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void changeUserDetails(){
        boolean valid = true;
        if(!mFormValidator.validateEmail()){
            valid = false;
        }
        if(!mFormValidator.validateUsername()){
            valid = false;
        }
        if(!valid){
            return;
        }
        // TODO Send to API.
        saveUserDetails();
        Intent intent = new Intent(AppConsts.ACTION_SHOW_ENTRIES);
        startActivity(intent);
    }

    private void saveUserDetails(){
        mAppPreferences.edit()
                .setLoggedIn(true)
                .setUserName(mUsername.getText().toString())
                .setUserPhoto(mImageUri.toString())
                .setUserEmail(mEmail.getText().toString())
                .commit();
    }

    @OnClick(R.id.edit_profile_photo)
    public void onEditPhotoClick() {
        final Optional<Intent> imageIntent = mPhotoChooserHelper.createSelectOrCaptureIntent();
        if (imageIntent.isPresent()) {
            startActivityForResult(imageIntent.get(), REQ_CODE_PICK_IMAGE);
        }
    }

    @OnClick(R.id.save_profile_settings)
    public void onSaveSettingsClick() {
        changeUserDetails();
    }
}
