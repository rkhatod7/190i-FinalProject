package com.example.radhika.finalproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    TextView name;
    private ProfileTracker mProfileTracker;
    public static Profile profile;
    CallbackManager callbackManager;
    Button continueButton;
    ProfilePictureView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        continueButton = (Button) findViewById(R.id.continue_button);
        profileImage = (ProfilePictureView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        TextView appName = (TextView) findViewById(R.id.appName);

        loginButton.setReadPermissions("email");


        // firebase
        Firebase.setAndroidContext(this);



        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            profileImage.setProfileId(profile.getId());
            name.setText("Signed in as " + profile.getFirstName() );
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            if (Profile.getCurrentProfile() == null) {
                                mProfileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                        // profile2 is the new profile
                                        Log.v("facebook - profile", profile2.getFirstName());
                                        mProfileTracker.stopTracking();
                                    }
                                };
                                // no need to call startTracking() on mProfileTracker
                                // because it is called by its constructor, internally.
                            } else {
                                Profile profile = Profile.getCurrentProfile();
                                profileImage.setProfileId(profile.getId());

                                Log.v("facebook - profile", profile.getFirstName());
                            }
                            // App code
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(this, HomePage.class);

        startActivity(intent);
    }

}
