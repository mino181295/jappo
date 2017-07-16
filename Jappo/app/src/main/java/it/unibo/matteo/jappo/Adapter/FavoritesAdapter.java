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

public class FavoritesAdapter extends ArrayAdapter<Item> {

    public FavoritesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.favorite_item, null);

        ImageView typeImage = (ImageView) convertView.findViewById(R.id.item_image);
        TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
        TextView itemNumber = (TextView) convertView.findViewById(R.id.item_number);

        if (position % 2 != 0) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.background_color));
        }
        Item i = getItem(position);

        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.test));

        itemName.setText(i.getName());
        itemNumber.setText(String.valueOf(i.getNumber()));

        return convertView;
    }
}
