package com.hirenpatel.instalogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import java.util.HashMap;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hirenpatel.instalogin.Util.AppConstants;
import com.hirenpatel.instalogin.Util.InstagramApp;

public class LoginActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    RelativeLayout rel;
    private InstagramApp mApp;
    Button button;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(LoginActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor();

        sharedPreferences = getApplication().getSharedPreferences(AppConstants.InstagramSession.SHARED, Context.MODE_PRIVATE);
        rel=(RelativeLayout) findViewById(R.id.activity_login);
        button =(Button) findViewById(R.id.buttoninstalogin);
        animationDrawable = (AnimationDrawable) rel.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        mApp = new InstagramApp(this, AppConstants.InstaData.CLIENT_ID, AppConstants.InstaData.CLIENT_SECRET, AppConstants.InstaData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putBoolean(AppConstants.InstagramSession.FirstLogin,true);
                editor.commit();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApp.authorize();
            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }
}
