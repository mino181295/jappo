package it.unibo.matteo.jappo.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.R;

/**
 * Splash screen to load {@link DataModel} if present or to redirect to {@link LoginActivity}
 */
public class SplashScreenActivity extends AppCompatActivity {

    ImageView mImageView;
    ProgressBar mProgressBar;

    DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideActionBar();

        dataModel = new DataModel(getApplicationContext());

        mImageView = (ImageView) findViewById(R.id.image_logo);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Animation animation = getAnimationInstance();
        mImageView.setAnimation(animation);
        animation.start();
    }

    /**
     * Fade in animation setup
     * @return Animation instance that fades in
     */
    private Animation getAnimationInstance(){
        /* Animation setup */
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
        Animation.AnimationListener fadeInListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new LoadDataTask().execute((Void) null);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
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

    /**
     * Task that load the {@link DataModel} with an {@link AsyncTask}
     */
    public class LoadDataTask extends AsyncTask<Void, Void, Boolean>{
        LoadDataTask() {}
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean logged = dataModel.isLoggedIn();
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