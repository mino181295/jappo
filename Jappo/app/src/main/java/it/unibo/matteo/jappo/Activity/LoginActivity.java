package it.unibo.matteo.jappo.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.unibo.matteo.jappo.Fragment.LoginFragment;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private Fragment currentFragment;

    public TextView mRegisterLabel;
    public Button mMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView(){
        hideActionBar();
        setupLoginFragment("");
        setupButtons();
    }

    private void setupButtons(){
        mRegisterLabel = (TextView) findViewById(R.id.register_label);
        mRegisterLabel.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainButton.setText(R.string.register);
                mRegisterLabel.setVisibility(View.GONE);
                reduceBox();
                setupRegisterFragment();
            }
        });

        mMainButton = (Button) findViewById(R.id.login_button);
        mMainButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment instanceof LoginFragment) {
                    loginFragment.attemptLogin();
                } else if (currentFragment instanceof  RegisterFragment){
                    registerFragment.attemptRegistration();
                }
            }
        });
    }

    public void reduceBox(){
        View v = findViewById(R.id.input_container);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
        params.weight /= 2;
        v.setLayoutParams(params);
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void setupLoginFragment(String mail){
        currentFragment = loginFragment = LoginFragment.newInstance(mail);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, loginFragment).commit();
    }

    public void setupRegisterFragment(){
        currentFragment = registerFragment = RegisterFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, registerFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof LoginFragment){
            super.onBackPressed();
        } else if (currentFragment instanceof RegisterFragment){
            setupLoginFragment("");

            View view = findViewById(R.id.input_container);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
            params.weight *= 2;

            mMainButton.setText(R.string.login);
            mRegisterLabel.setVisibility(View.VISIBLE);
        }
    }
}
