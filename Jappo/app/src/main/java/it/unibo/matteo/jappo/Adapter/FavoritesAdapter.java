package it.unibo.matteo.jappo.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;

public class FavoritesAdapter extends ArrayAdapter<Item> {

    List<Item> mDataSet;

    public FavoritesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mDataSet = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.favorite_item, null);

        ImageView typeImage = (ImageView) convertView.findViewById(R.id.item_image);
        TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
        TextView itemNumber = (TextView) convertView.findViewById(R.id.item_number);
        TextView typeName = (TextView) convertView.findViewById(R.id.item_type);

        if (position % 2 != 0) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.background_color));
        }

        Item i = getItem(position);
        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), i.getType().getImage()));
        itemName.setText(i.getName());
        itemNumber.setText(String.valueOf(i.getNumber()));
        typeName.setText(i.getType().getName());

        ImageButton deleteItem = (ImageButton) convertView.findViewById(R.id.item_delete);
        deleteItem.setTag(position);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int index = (Integer) view.getTag() ;
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Elimina")
                        .setMessage("Sei sicuro di voler eliminare " + getItem(index).getName() + " dai preferiti?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDataSet.remove(index);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }
}
