package it.unibo.matteo.jappo.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Adapter.CompletedAdapter;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Order;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.MediaHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment that shows the completed products that arrived.
 */
public class CompletedFragment extends Fragment {

    private CompletedAdapter completed;
    private ListView mCompletedList;
    private static Order order;

    private static ArrayList<Item> arrived;
    private TextView mEmptyLabel;

    private Dialog mImageDialog;
    private int position;

    public CompletedFragment() {
    }

    public static CompletedFragment newInstance(Order o) {
        order = o;
        arrived = o.getArrived();
        return new CompletedFragment();
    }

    public static CompletedFragment newInstance() {
        order = null;
        arrived = new ArrayList<>();
        return new CompletedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_completed, container, false);
        /* View setup */
        mEmptyLabel = (TextView)v.findViewById(R.id.info_text);

        completed = new CompletedAdapter(getContext(), R.layout.completed_item, arrived);
        mCompletedList = (ListView)v.findViewById(R.id.completed_list);
        mCompletedList.setAdapter(completed);
        mCompletedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                createAlertDialog(i);
                return false;
            }
        });
        mCompletedList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ( motionEvent.getAction() == MotionEvent.ACTION_UP){
                    hidePhotoDialog();
                }
                return false;
            }
        });
        refreshCompleted();
        return v;
    }

    /**
     * Method to refresh the data inside the {@link ListView}
     */
    public void refreshCompleted(){
        if (completed != null) {
            completed.notifyDataSetChanged();
            int completedNumber = arrived.size();
            if (completedNumber != 0){
                mEmptyLabel.setVisibility(View.INVISIBLE);
            } else {
                mEmptyLabel.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method to shoe the {@link Bitmap} image of the completed {@link Item}
     * @param position number of the item in the {@link ListView}
     */
    public void createAlertDialog(int position){
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_image_preview, null);
        ImageView mImageView = (ImageView) dialogView.findViewById(R.id.popup_image_preview);
        TextView mEmptyText = (TextView) dialogView.findViewById(R.id.popup_no_image);

        if (MediaHelper.hasImage(getContext(), arrived.get(position).toString())){
            Bitmap bitmapImage = MediaHelper.loadImage(getContext(), arrived.get(position).toString());
            mImageView.setImageBitmap(bitmapImage);
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
        }
        mImageDialog = adb.setView(dialogView).create();
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.copyFrom(mImageDialog.getWindow().getAttributes());
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mImageDialog.show();
        mImageDialog.getWindow().setAttributes(mParams);
    }

    /**
     * Hides the shown image {@link Dialog}
     */
    private void hidePhotoDialog(){
        if (mImageDialog != null){
            mImageDialog.dismiss();
        }
    }

    public void putCurrentPosition(int position){
        this.position = position;
    }

    /* Result Bitmap of the captured image */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle mExtras = data.getExtras();
            Bitmap bitmapImage = (Bitmap) mExtras.get("data");
            MediaHelper.saveImage(getContext(), bitmapImage, arrived.get(position).toString());
        }
    }

}
