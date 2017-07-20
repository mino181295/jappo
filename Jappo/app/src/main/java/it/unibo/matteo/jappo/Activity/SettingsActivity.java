package it.unibo.matteo.jappo.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.transition.Visibility;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.AlarmNotificationReceiver;
import it.unibo.matteo.jappo.Utils.HTTPHelper;
import it.unibo.matteo.jappo.Utils.SharedPreferencesManager;

import static it.unibo.matteo.jappo.Utils.HTTPHelper.downloadImageBase64;
import static java.lang.System.in;
import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    Bitmap currentImageBitmap;
    ProgressBar imageProgressBar;
    TextView mNameTextView;
    User loggedUser;
    DataModel dm;

    public static final int PICK_PHOTO_FOR_AVATAR = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dm = new DataModel(getApplicationContext());
        dm.load();
        loggedUser = dm.getLoggedUser();

        mNameTextView = (TextView)findViewById(R.id.settings_name);
        mNameTextView.setText(loggedUser.getName());

        imageProgressBar = (ProgressBar)findViewById(R.id.settings_progress_bar);
        showProgress(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ImageButton backButton =  (ImageButton) toolbar.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        new AsyncTask<String, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(String... strings) {
                String response = HTTPHelper.downloadImageBase64(strings[0]);
                currentImageBitmap = decodeBase64(response);
                if (!response.isEmpty()){
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result){
                    circleImageView.setImageBitmap(currentImageBitmap);
                } else {
                    circleImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.test));
                }
                showProgress(false);
            }
        }.execute(dm.getLoggedUser().getProfileImage());
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
        stopAlarm();
        finish();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                //Gets the input stream of the picked image
                InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                //Compression part
                bmp = compressImage(bmp);
                bmp = scaleDown(bmp, 200, true);
                //Encode to upload
                String imageEncoded = encodeTobase64(bmp);
                circleImageView.setImageBitmap(bmp);
                showProgress(true);
                //Upload
                new AsyncTask<String, Void, Boolean>(){
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        String imageName = strings[0];
                        String imageEncoded = strings[1];
                        HTTPHelper.uploadImageBase64( imageName, imageEncoded);
                        return true;
                    }
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        showProgress(false);
                    }
                }.execute(dm.getLoggedUser().getProfileImage(),
                        imageEncoded);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeTobase64(Bitmap image){
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input){
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private Bitmap compressImage (Bitmap original){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 50, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void showProgress(boolean isVisible){
        if (isVisible){
            imageProgressBar.setVisibility(View.VISIBLE);
        } else {
            imageProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void stopAlarm(){
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this , AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.cancel(pendingIntent);
    }

}
