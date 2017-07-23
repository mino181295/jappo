package it.unibo.matteo.jappo.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Fragment.CompletedFragment;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;

public class CompletedAdapter extends ArrayAdapter<Item> {

    public static final int CAMERA_REQUEST = 12739;

    public CompletedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.completed_item, null);

        /* Components setup */
        final ImageView typeImage = (ImageView) v.findViewById(R.id.completed_image);

        final TextView itemName = (TextView) v.findViewById(R.id.highscore_name);
        final TextView itemNumber = (TextView) v.findViewById(R.id.completed_number);
        final TextView timeText = (TextView) v.findViewById(R.id.completed_time);

        /* Current Item setup */
        Item currentItem = getItem(position);

        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), currentItem.getType().getImage()));
        itemNumber.setText(String.valueOf(currentItem.getNumber()));
        timeText.setText(currentItem.getTimeString());
        itemName.setText(currentItem.getName());

        /* Camera button setup */
        ImageView cameraButton = (ImageView) v.findViewById(R.id.completed_camera_button);
        cameraButton.setTag(position);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (Integer) view.getTag();
                requestCamera();
                startCamera(position);
            }
        });

        return v;
    }

    /* Function that, if the permissions are granted, starts the cam */
    private void startCamera(int position){
        MainActivity currentActivity = (MainActivity) getContext();
        CompletedFragment fragment = (CompletedFragment) currentActivity.getViewerFragment(2);
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fragment.putCurrentPosition(position);
            fragment.startActivityForResult(cameraIntent, 2);
        }
    }
    /* Function that requests the camera permissions */
    private void requestCamera(){
        MainActivity currentActivity = (MainActivity) getContext();
        if (ContextCompat.checkSelfPermission(currentActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST);
        }
    }
}
