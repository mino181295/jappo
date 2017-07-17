package it.unibo.matteo.jappo.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Fragment.FavoritesFragment;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;


public class OrderAdapter extends ArrayAdapter<Item> {

    private List<Item> mDataSet;

    public OrderAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mDataSet = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.order_item, null);
        convertView.setTag(position);

        final ImageView typeImage = (ImageView) convertView.findViewById(R.id.ordered_image);
        final TextView itemName = (TextView) convertView.findViewById(R.id.ordered_name);

        final TextView itemNumber = (TextView) convertView.findViewById(R.id.ordered_number);
        final TextView orderedTime = (TextView) convertView.findViewById(R.id.ordered_time);

        final ImageButton addFavorite = (ImageButton) convertView.findViewById(R.id.ordered_favorite);

        final MainActivity mainActivity = (MainActivity)getContext();
        final FavoritesFragment favoritesFragment = (FavoritesFragment)mainActivity.getViewerFragment(0);

        final View finalConvertView = convertView;
        final Item currentItem = getItem(position);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (Integer) finalConvertView.getTag();
                if (currentItem.isFavorite(favoritesFragment.getFavourites())){
                    favoritesFragment.removeItemFromFavourites(currentItem);
                    notifyDataSetChanged();
                } else {
                    favoritesFragment.addItemToFavourites(currentItem);
                    Snackbar.make(view, "Aggiunto ai preferiti", Snackbar.LENGTH_LONG)
                            .setAction("Visualizza", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mainActivity.setViewerPage(0);
                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.white))
                            .show();
                    notifyDataSetChanged();
                }
            }
        });
        //If for the favorite star appearence
        if (currentItem.isFavorite(favoritesFragment.getFavourites())){
            addFavorite.setImageResource(R.mipmap.star_icon_fill);
        } else {
            addFavorite.setImageResource(R.mipmap.star_icon_empty);
        }

        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), currentItem.getType().getImage()));
        itemNumber.setText(String.valueOf(currentItem.getNumber()));
        orderedTime.setText(currentItem.getTimeString());
        itemName.setText(currentItem.getName());

        return convertView;
    }
}
