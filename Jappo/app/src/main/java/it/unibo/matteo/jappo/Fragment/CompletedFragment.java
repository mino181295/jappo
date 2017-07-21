package it.unibo.matteo.jappo.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.unibo.matteo.jappo.Adapter.CompletedAdapter;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Order;
import it.unibo.matteo.jappo.R;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;
import static com.google.gson.internal.UnsafeAllocator.create;

public class CompletedFragment extends Fragment {

    private CompletedAdapter completedAdapter;
    private ListView mCompletedList;
    private static Order order;

    private static ArrayList<Item> arrivedItems;
    private TextView mEmptyLabel;

    private Dialog imageDialog;
    private int position;

    public CompletedFragment() {
    }

    public static CompletedFragment newInstance(Order o) {
        order = o;
        arrivedItems = o.getArrivedItems();
        return new CompletedFragment();
    }

    public static CompletedFragment newInstance() {
        order = null;
        arrivedItems = new ArrayList<>();
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

        mEmptyLabel = (TextView)v.findViewById(R.id.info_text);

        mCompletedList = (ListView)v.findViewById(R.id.completed_list);
        completedAdapter = new CompletedAdapter(getContext(), R.layout.completed_item, arrivedItems);
        mCompletedList.setAdapter(completedAdapter);
        mCompletedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                createAlertDialog(position);
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

    public void refreshCompleted(){
        if (completedAdapter != null) {
            completedAdapter.notifyDataSetChanged();

            int completedNumber = arrivedItems.size();
            if (completedNumber != 0){
                mEmptyLabel.setVisibility(View.INVISIBLE);
            } else {
                mEmptyLabel.setVisibility(View.VISIBLE);
            }
        }
    }

    public void createAlertDialog(int position){
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_image_preview, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.popup_image_preview);
        TextView emptyText = (TextView) dialogView.findViewById(R.id.popup_no_image);

        if (arrivedItems.get(position).getCapturedPic() != null){
            imageView.setImageBitmap(arrivedItems.get(position).getCapturedPic());
        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
        imageDialog = adb.setView(dialogView).create();
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.copyFrom(imageDialog.getWindow().getAttributes());
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        imageDialog.show();
        imageDialog.getWindow().setAttributes(mParams);
    }

    private void hidePhotoDialog(){
        if (imageDialog != null){
            imageDialog.dismiss();
        }
    }

    public void putCurrentPosition(int position){
        this.position = position;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle mExtras = data.getExtras();
            Bitmap bitmap = (Bitmap) mExtras.get("data");
            arrivedItems.get(position).setCapturedPic(bitmap);
        }
    }

}
