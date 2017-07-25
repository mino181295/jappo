package it.unibo.matteo.jappo.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Fragment.OrderFragment;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;

public class FavoritesAdapter extends ArrayAdapter<Item> {

    private MainActivity mainActivity;
    private Fragment mCurrentFragment;

    private List<Item> mDataSet;

    public FavoritesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mDataSet = objects;

        mainActivity = (MainActivity)getContext();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.favorite_item, null);

        final ImageView mTypeImage = (ImageView) v.findViewById(R.id.item_image);
        final TextView mItemNumber = (TextView) v.findViewById(R.id.item_number);
        final TextView mItemName = (TextView) v.findViewById(R.id.item_name);
        final TextView mTypeName = (TextView) v.findViewById(R.id.item_type);

        if (position % 2 != 0) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_color));
        }

        final Item item = getItem(position);

        mTypeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), item.getType().getImage()));
        ImageButton mDeleteItem = (ImageButton) v.findViewById(R.id.item_delete);
        mItemNumber.setText(String.valueOf(item.getNumber()));

        if (item.getRestaurant() != null){
            mTypeName.setText(item.getRestaurant().toString());
        } else {
            mTypeName.setText(item.getType().getName());
        }
        mItemName.setText(item.getName());
        mDeleteItem.setTag(position);

        mCurrentFragment = mainActivity.getViewerFragment(1);
        final OrderFragment of = (mCurrentFragment instanceof OrderFragment) ? (OrderFragment) mCurrentFragment : null;

        /* Delete icon listener */
        mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int current = (Integer) view.getTag();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete)
                        .setMessage(getContext().getString(R.string.confirm_delete) + " " +
                                getItem(current).getName() + " " +
                                getContext().getString(R.string.from_favourites))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDataSet.remove(current);
                                notifyDataSetChanged();
                                if (of != null) of.refreshOrder();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });
        return v;
    }
}
