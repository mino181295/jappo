package it.unibo.matteo.jappo.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;

public class CompletedAdapter extends ArrayAdapter<Item> {

    private List<Item> mDataSet;

    public CompletedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mDataSet = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.completed_item, null);

        final ImageView typeImage = (ImageView) v.findViewById(R.id.completed_image);

        final TextView itemName = (TextView) v.findViewById(R.id.completed_name);
        final TextView itemNumber = (TextView) v.findViewById(R.id.completed_number);
        final TextView timeText = (TextView) v.findViewById(R.id.completed_time);

        Item currentItem = getItem(position);

        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), currentItem.getType().getImage()));
        itemNumber.setText(String.valueOf(currentItem.getNumber()));
        timeText.setText(currentItem.getTimeString());
        itemName.setText(currentItem.getName());

        return v;
    }
}
