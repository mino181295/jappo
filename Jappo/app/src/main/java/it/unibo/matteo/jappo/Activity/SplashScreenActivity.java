package it.unibo.matteo.jappo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;

    SharedPreferencesManager spManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        spManager = new SharedPreferencesManager("Default", getApplicationContext());
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
                            if (spManager.isLoggedIn()){
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                finish();
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

    @Override
    public void onBackPressed() {}
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}