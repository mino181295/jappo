package it.unibo.matteo.jappo.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.AlarmNotificationReceiver;
import it.unibo.matteo.jappo.Utils.HTTPHelper;

/**
 * Activity that contain the profile image, the name and the Logout option.
 */
public class SettingsActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    ProgressBar imageProgressBar;
    Bitmap currentImageBitmap;

    TextView mNameTextView;
    DataModel dataModel;
    User loggedUser;

    public static final int PICK_PHOTO_FOR_AVATAR = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        /* Components setup */
        dataModel = new DataModel(getApplicationContext());
        dataModel.load();
        loggedUser = dataModel.getLoggedUser();

        mNameTextView = (TextView)findViewById(R.id.settings_name);
        mNameTextView.setText(loggedUser.getName());

        imageProgressBar = (ProgressBar)findViewById(R.id.settings_progress_bar);
        showProgress(true);
        /* Action Bar setup */
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

        /* Task that shows a progress bar and loads the Profile picture */
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
        }.execute(dataModel.getLoggedUser().getProfileImage());
    }

    /**
     * Method that shows the logout {@link AlertDialog}
     */
    public void showDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        performLogout();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Logout routine that opens the {@link LoginActivity}
     */
    public void performLogout(){
        clearPreferences();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopAlarm();
        finish();
    }

    /**
     * Method that removes the {@link DataModel} from the {@link android.content.SharedPreferences}
     */
    public void clearPreferences(){
        DataModel dataModel = new DataModel(getApplicationContext());
        dataModel.remove();
    }

    /**
     * Imagepicker for the Profile Image.
     */
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
                InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);
                /* Compression part */
                bitmapImage = compressImage(bitmapImage);
                bitmapImage = scaleDown(bitmapImage, 200, true);
                /* Encode to upload */
                String imageEncoded = encodeToBase64(bitmapImage);
                circleImageView.setImageBitmap(bitmapImage);
                showProgress(true);
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
                }.execute(dataModel.getLoggedUser().getProfileImage(),
                        imageEncoded);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Bitmap encoding
     * @param image
     * @return
     */
    public static String encodeToBase64(Bitmap image){
        Bitmap bitmapImage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input){
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * Image compression to save space while uploading {@link Bitmap}
     * @param original
     * @return
     */
    private Bitmap compressImage (Bitmap original){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 50, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    /**
     * Method that scales a {@link Bitmap} picture
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return
     */
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

    /**
     * Method that sets the {@link ProgressBar} Visibility
     * @param isVisible
     */
    private void showProgress(boolean isVisible){
        if (isVisible){
            imageProgressBar.setVisibility(View.VISIBLE);
        } else {
            imageProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method that stop the {@link android.app.Notification} from the {@link AlarmManager}
     */
    private void stopAlarm(){
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this , AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.cancel(pendingIntent);
    }

}
