package it.unibo.matteo.jappo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Fragment loginFragment;
    Fragment registerFragment;
    Fragment currentFragment;

    TextView mRegisterLabel;
    Button mMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView(){
        hideActionBar();
        setupLoginFragment();
        setupButtons();
    }

    private void setupButtons(){
        mRegisterLabel = (TextView) findViewById(R.id.register_label);
        mRegisterLabel.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainButton.setText("Registrati");
                mRegisterLabel.setVisibility(View.GONE);

                View v = findViewById(R.id.input_container);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
                params.weight /= 2;
                v.setLayoutParams(params);

                setupRegisterFragment();
            }
        });

        mMainButton = (Button) findViewById(R.id.login_button);
        mMainButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void setupLoginFragment(){
        currentFragment = loginFragment = LoginFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.input_container, loginFragment).commit();
    }

    private void setupRegisterFragment(){
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
            setupLoginFragment();

            View view = findViewById(R.id.input_container);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
            params.weight *= 2;

            mMainButton.setText("Accedi");
            mRegisterLabel.setVisibility(View.VISIBLE);
        }
    }
}
