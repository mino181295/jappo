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

    public OrderAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.order_item, null);
        convertView.setTag(position);

        /* View setup */
        final ImageView mTypeImage = (ImageView) convertView.findViewById(R.id.ordered_image);
        final TextView mItemName = (TextView) convertView.findViewById(R.id.ordered_name);

        final TextView mItemNumber = (TextView) convertView.findViewById(R.id.ordered_number);
        final TextView mOrderedTime = (TextView) convertView.findViewById(R.id.ordered_time);

        final ImageButton mAddFavourites = (ImageButton) convertView.findViewById(R.id.ordered_favorite);

        final MainActivity mMainActivity = (MainActivity)getContext();
        final FavoritesFragment mFavouritesFragment = (FavoritesFragment)mMainActivity.getViewerFragment(0);

        final View finalConvertView = convertView;
        final Item currentItem = getItem(position);
        mAddFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (Integer) finalConvertView.getTag();
                if (currentItem.isFavorite(mFavouritesFragment.getFavourites())){
                    mFavouritesFragment.removeItemFromFavourites(currentItem);
                    notifyDataSetChanged();
                } else {
                    mFavouritesFragment.addItemToFavourites(currentItem);
                    Snackbar.make(view, R.string.added_favorites, Snackbar.LENGTH_LONG)
                            .setAction(R.string.show, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMainActivity.setViewerPage(0);
                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.white))
                            .show();
                    notifyDataSetChanged();
                }
            }
        });
        /* Item star setup */
        if (currentItem.isFavorite(mFavouritesFragment.getFavourites())){
            mAddFavourites.setImageResource(R.mipmap.star_icon_fill);
        } else {
            mAddFavourites.setImageResource(R.mipmap.star_icon_empty);
        }
        /* Values setup */
        mTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), currentItem.getType().getImage()));
        mItemNumber.setText(String.valueOf(currentItem.getNumber()));
        mOrderedTime.setText(currentItem.getTimeString());
        mItemName.setText(currentItem.getName());

        return convertView;
    }
}
