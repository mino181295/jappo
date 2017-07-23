package it.unibo.matteo.jappo.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import it.unibo.matteo.jappo.Fragment.LoginFragment;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Fragment.RegisterFragment;

/**
 * Activity divided in 2 {@link Fragment} that can be {@link LoginFragment} or
 * {@link RegisterFragment}. In this activity you can access the basic functionality of logging in
 * or registering a new User.
 */
public class LoginActivity extends AppCompatActivity {

    private RegisterFragment mRegisterFragment;
    private LoginFragment mLoginFragment;
    private Fragment mCurrentFragment;

    public TextView mRegisterLabel;
    public Button mMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    /**
     * Method that setups the User Interface of the {@link android.app.Activity}.
     */
    private void setupView(){
        setupLoginFragment("");
        hideActionBar();
        setupButtons();
    }

    /**
     * Method that setups the buttons and their {@link android.view.View.OnClickListener}
     */
    private void setupButtons(){
        mRegisterLabel = (TextView) findViewById(R.id.register_label);
        mRegisterLabel.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainButton.setText(R.string.register);
                fadeOutView(mRegisterLabel);
                setupRegisterFragment();
            }
        });

        mMainButton = (Button) findViewById(R.id.login_button);
        mMainButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment instanceof LoginFragment) {
                    mLoginFragment.attemptLogin();
                } else if (mCurrentFragment instanceof  RegisterFragment){
                    mRegisterFragment.attemptRegistration();
                }
            }
        });
    }

    /**
     * Hides the {@link ActionBar}
     */
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * Setup the {@link LoginFragment}
     * @param mail is the initial mail in the {@link android.widget.EditText}
     */
    public void setupLoginFragment(String mail){
        mCurrentFragment = mLoginFragment = LoginFragment.newInstance(mail);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, mLoginFragment).commit();
    }

    /**
     * Setup the {@link RegisterFragment}
     */
    public void setupRegisterFragment(){
        mCurrentFragment = mRegisterFragment = RegisterFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, mRegisterFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof LoginFragment){
            super.onBackPressed();
        } else if (mCurrentFragment instanceof RegisterFragment){
            setupLoginFragment("");
            mMainButton.setText(R.string.login);
            mRegisterLabel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Animation of the Fade Out to a target {@link View}
     * @param view target {@link View} that has to be animated
     */
    private void fadeOutView(View view) {
        Animation slideOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out_animation);
        final View finalView = view;
        if (slideOut != null) {
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    finalView.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(slideOut);
        }
    }
}
