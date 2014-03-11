package com.appunite.guestbook.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.appunite.guestbook.R;
import com.appunite.guestbook.dagger.ForApplication;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PhotoChooserHelper {
    private final Context mContext;

    @Inject
    public PhotoChooserHelper(@ForApplication Context context) {
        mContext = context;
    }

    private boolean displayErrorIfExternalStorageNotMounted() {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            new AlertDialog.Builder(mContext)
                    .setMessage(R.string.edit_profile_photo_sd_unmounted)
                    .setTitle(R.string.edit_profile_photo_label)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            return true;
        }
        return false;
    }

    private Intent makeCameraIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    private Intent makeGalleryIntent(){
        return new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                .setType("image/*");
    }

    public Optional<Intent> createSelectOrCaptureIntent() {
        if (displayErrorIfExternalStorageNotMounted()) return Optional.absent();

        final Intent captureIntent = makeCameraIntent();
        final Intent galleryIntent = makeGalleryIntent();

        final PackageManager packageManager = checkNotNull(mContext.getPackageManager());
        final List<ResolveInfo> listCameraApps = packageManager.queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        for (ResolveInfo res : listCameraApps) {
            final ActivityInfo activityInfo = res.activityInfo;
            if (activityInfo == null) {
                continue;
            }
            final String packageName = activityInfo.packageName;
            final String className = activityInfo.name;

            Intent intent = new Intent(captureIntent);
            intent.setClassName(packageName, className);
            cameraIntents.add(intent);
        }

        final String title = mContext.getString(R.string.edit_profile_select_image_source);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, title);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        return Optional.of(chooserIntent);
    }

    public Optional<Uri> onResult(int resultCode, Intent intent){
        if(resultCode == Activity.RESULT_OK){
            if(intent != null && intent.getData() != null){
                return Optional.of(intent.getData());
            }
        }
        return Optional.absent();
    }

}
