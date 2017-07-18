package it.unibo.matteo.jappo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.HashMap;

import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.JSONHelper;
import it.unibo.matteo.jappo.Utils.RequestType;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

import static it.unibo.matteo.jappo.R.string.email;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;

    DataModel dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideActionBar();

        dm = new DataModel(getApplicationContext());

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
                new LoadDataTask().execute((Void) null);
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

    public class LoadDataTask extends AsyncTask<Void, Void, Boolean>{

        LoadDataTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean logged = dm.isLoggedIn();
            if (logged) {
                DataModel dm = new DataModel(getApplicationContext());
                dm.load();
                dm.loadData(dm.getLoggedUser());
                dm.save();
            }
            return logged;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success){
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }

    }
}