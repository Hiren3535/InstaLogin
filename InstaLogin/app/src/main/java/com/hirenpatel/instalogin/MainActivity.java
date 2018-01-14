package com.hirenpatel.instalogin;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hirenpatel.instalogin.Util.AppConstants;
import com.hirenpatel.instalogin.Util.InstagramApp;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llAfterLoginView;
    private String mAccessToken;
    private AppConstants.InstagramSession mSession;
    private ImageView ivProfile;
    SharedPreferences sharedPreferences;
    private InstagramApp InstaAPP;
    TextView tvNoOfFollwers,tvName,tvNoOfFollowing;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = InstaAPP.getUserInfo();
                Picasso.with(MainActivity.this)
                        .load(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE))
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(ivProfile);
                tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));
                tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
                tvNoOfFollwers.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWED_BY));
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getApplication().getSharedPreferences(AppConstants.InstagramSession.SHARED, Context.MODE_PRIVATE);
        mSession = new AppConstants.InstagramSession(MainActivity.this);
        mAccessToken = mSession.getAccessToken();
        InitView();

        InstaAPP = new InstagramApp(this, AppConstants.InstaData.CLIENT_ID, AppConstants.InstaData.CLIENT_SECRET, AppConstants.InstaData.CALLBACK_URL);
        InstaAPP.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                InstaAPP.fetchUserName(handler);
            }
            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        if (InstaAPP.hasAccessToken()) {
            llAfterLoginView.setVisibility(View.VISIBLE);
            InstaAPP.fetchUserName(handler);
        }
    }

    private void InitView() {
        llAfterLoginView = (LinearLayout) findViewById(R.id.llAfterLoginView);
        ivProfile = (ImageView)findViewById(R.id.ivProfileImage);
        tvName = (TextView)findViewById(R.id.tvUserName);
        tvNoOfFollwers = (TextView)findViewById(R.id.tvNoOfFollowers);
        tvNoOfFollowing = (TextView)findViewById(R.id.tvNoOfFollowing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    InstaAPP.resetAccessToken();
                                    SharedPreferences.Editor editor =sharedPreferences.edit();
                                    editor.clear();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}