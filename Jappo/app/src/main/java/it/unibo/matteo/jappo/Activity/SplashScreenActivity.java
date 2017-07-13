package it.unibo.matteo.jappo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import it.unibo.matteo.jappo.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;

    public static final String userName = "NAME";
    public static final String isLogged = "LOGGED";
    public static final String userEmail = "EMAIL";

    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideActionBar();

        imageView = (ImageView) findViewById(R.id.image_logo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Animation animation = getAnimationInstance();
        imageView.setAnimation(animation);
        animation.start();
    }

    private Animation getAnimationInstance(){
        /* Animation setup */
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
        Animation.AnimationListener fadeInListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                Handler activityController = new Handler();
                Runnable runClose = new Runnable() {
                    @Override
                    public void run() {
                        boolean isLoggedIn = isLoggedIn();
                        if (isLoggedIn) {
                            //TODO: Start main activity with Bundle the info
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
                activityController.post(runClose);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        animation.setAnimationListener(fadeInListener);

        return animation;
    }

    private boolean checkSharedPreferences(){
        userPreferences = getPreferences(Context.MODE_PRIVATE);
        if (userPreferences.contains(isLogged) && userPreferences.contains(userName)
                && userPreferences.contains(userEmail)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLoggedIn(){
        if (checkSharedPreferences()){
            boolean isLoggedIn = false;
            userPreferences.getBoolean(isLogged, isLoggedIn);
            return isLoggedIn;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {}
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}