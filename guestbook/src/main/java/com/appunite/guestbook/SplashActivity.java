package com.appunite.guestbook;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    @InjectView(R.id.splash_image)
    ImageView mSplashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        final Intent intent = new Intent(AppConsts.ACTION_SHOW_ENTRIES).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSplashImage.setImageBitmap(null);
    }
}
