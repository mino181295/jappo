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

    private void setupView(){
        setupLoginFragment("");
        hideActionBar();
        setupButtons();
    }

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

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void setupLoginFragment(String mail){
        mCurrentFragment = mLoginFragment = LoginFragment.newInstance(mail);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, mLoginFragment).commit();
    }

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
