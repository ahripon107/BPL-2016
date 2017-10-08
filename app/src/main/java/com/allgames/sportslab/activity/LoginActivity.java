package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.allgames.sportslab.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * @author Ripon
 */
public class LoginActivity extends CommonActivity {

    LoginButton loginButton;
    TextView textView;

    CallbackManager callbackManager;
    Profile profile;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton.setReadPermissions("public_profile");

        callbackManager = CallbackManager.Factory.create();

        profile = Profile.getCurrentProfile();
        if (profile == null) {
            textView.setText("Please Log In");
        } else {
            textView.setText("You are logged in as " + profile.getName());
        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_LONG).show();
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile == null) {
                    textView.setText("Please Log In");
                } else {
                    textView.setText("You are logged in as " + currentProfile.getName());
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize() {
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.login_text);
    }
}
