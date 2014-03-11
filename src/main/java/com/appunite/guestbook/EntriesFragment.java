package com.appunite.guestbook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunite.guestbook.adapter.EntryAdapter;
import com.appunite.guestbook.content.UserPreferences;
import com.appunite.guestbook.dialogs.EntryDialog;
import com.appunite.guestbook.dialogs.NewEntryDialog;
import com.appunite.guestbook.dialogs.NewEntryDialog.NewEntryListener;
import com.appunite.guestbook.helpers.data.ApiAsyncLoader;
import com.appunite.guestbook.helpers.data.Result;
import com.appunite.syncer.AUSyncerStatus;
import com.appunite.syncer.DownloadHelper;
import com.appunite.syncer.DownloadHelper.DownloadReceiver;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntriesFragment extends ErrorHelperApiLoaderFragment<Result<String>>
        implements AdapterView.OnItemClickListener, NewEntryListener {

    private DownloadReceiver mReceiver = new DownloadReceiver() {
        @Override
        protected void onReceive(Uri uri, AUSyncerStatus status) {
            Log.e("RECEIVE", "RECEIVE");
        }
    };

    private static class EntriesLoader extends ApiAsyncLoader<String> {

        public EntriesLoader(Context context) {
            super(context);
        }

        @Override
        protected String loadFromApi() throws Exception {
            Thread.sleep(3000);
            //   URL url = new URL("http://www.extremesstech.com/wp-content/uploads/2013/05/image-1.jpg");
            // Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return null;
        }
    }

    private static final String ENTRY_DIALOG = "entry_dialog";
    private static final String NEW_ENTRY_DIALOG = "new_entry_dialog";

    @InjectView(R.id.profile_header)
    RelativeLayout mProfileHeader;
    @InjectView(R.id.guest_information)
    RelativeLayout mGuestInformation;
    @InjectView(R.id.user_avatar)
    ImageView mUserAvatar;
    @InjectView(R.id.user_name)
    TextView mUserName;
    @InjectView(R.id.entry_button)
    Button mEntryButton;
    @InjectView(R.id.entries_list)
    ListView mEntriesList;
    @Inject
    EntryAdapter mAdapter;
    @Inject
    UserPreferences mAppPreferences;
    @Inject
    Resources mResources;
    @Inject
    Picasso mPicasso;
    @Inject
    GoogleApiClient mGoogleApiClient;

    private boolean mIsUserLogged;


    public static EntriesFragment newInstance() {
        return new EntriesFragment();
    }

    // TODO Change this with API.
    @Override
    protected boolean isScreenEmpty(Result<String> result) {
        return false;
    }

    @Override
    protected View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.entries_fragment, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEntriesList.setAdapter(mAdapter);
        mEntriesList.setOnItemClickListener(this);
        mIsUserLogged = mAppPreferences.isLoggedIn();
        mGoogleApiClient.connect();
    }

    private void setUserInfo() {
        mUserName.setText(mAppPreferences.getUserName());
        mPicasso.load(mAppPreferences.getUserPhoto())
                .fit()
                .into(mUserAvatar);

        Log.e("URL", "" + mAppPreferences.getUserPhoto());

//        Bitmap avatar = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ava);
//
//        mUserAvatar.setImageBitmap(getRoundedBitmap(avatar));

    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    @Override
    protected Loader<Result<String>> onCreateMainLoader(Bundle bundle) {
        return new EntriesLoader(getActivity());
    }

    @Override
    protected void onLoadMainFinished(Result<String> result) {
        super.onLoadMainFinished(result);

        if (result.isSuccess()) {
            mAdapter.swapData(new ArrayList<String>());
            updateUiComponents();
            if (mIsUserLogged) {
                setUserInfo();
            }
        }
    }

    private void updateUiComponents() {
        mProfileHeader.setVisibility(mIsUserLogged ? View.VISIBLE : View.GONE);
        mGuestInformation.setVisibility(mIsUserLogged ? View.GONE : View.VISIBLE);
        mEntryButton.setVisibility(mIsUserLogged ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onLoadMainReset() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EntryDialog entryDialog = EntryDialog.newInstance(mAdapter.getItem(position));
        entryDialog.show(getChildFragmentManager(), ENTRY_DIALOG);
    }

    NewEntryDialog newEntryDialog;
    @OnClick(R.id.entry_button)
    public void onEntryClick() {
        newEntryDialog = NewEntryDialog.newInstance(this);
        newEntryDialog.show(getChildFragmentManager(), NEW_ENTRY_DIALOG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            newEntryDialog = NewEntryDialog.newInstance(this);
            newEntryDialog.setTargetFragment(this, 123);

    }

    @OnClick(R.id.logout_button)
    public void onLogoutClick() {
        mIsUserLogged = false;
        updateUiComponents();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    @OnClick(R.id.settings_button)
    public void onSettingsClick() {
        Intent intent = new Intent(AppConsts.ACTION_SHOW_SETTINGS);
        startActivity(intent);
    }

    @OnClick(R.id.refresh_button)
    public void onRefreshClick() {
        if (!getLoaderManager().hasRunningLoaders()) {
            forceReload();
        }
    }

    @OnClick(R.id.not_logged_label)
    public void onLabelClick() {
        Intent intent = new Intent(AppConsts.ACTION_SHOW_LOGIN);
        startActivity(intent);
    }

    @OnClick(R.id.not_logged_information)
    public void onInformationClick(){
        Toast toast = Toast.makeText(getActivity(), mResources.getText(R.string.entries_not_logged_information), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onNewEntryFinish(String content) {
//        Bundle bundle = new Bundle();
//        bundle.putString("CONTENT", content);
//        Uri uri = Uri.parse("content://com.example/login");
//        DownloadHelper.startAsyncDownload(getActivity(), "string", uri, bundle, true);

        mAdapter.addData(content);
    }

}