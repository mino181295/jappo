package it.unibo.matteo.jappo.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferencesManager spManager;
    TextView mNameTextView;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spManager = new SharedPreferencesManager("Default", getApplicationContext());
        loggedUser = spManager.getLoggedUser();

        mNameTextView = (TextView)findViewById(R.id.settings_name);
        mNameTextView.setText(loggedUser.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ImageButton backButton =  (ImageButton) toolbar.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
                finish();
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void saveSettings(){
    }

    public void showDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Sei sicuro di voler effettuare logout?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        performLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void clearPreferences(){
        DataModel dm = new DataModel(getApplicationContext());
        dm.remove();
    }

    public void performLogout(){
        clearPreferences();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
