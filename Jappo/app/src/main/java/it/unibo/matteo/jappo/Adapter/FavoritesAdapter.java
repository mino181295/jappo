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

    private List<Item> mDataSet;
    private MainActivity mainActivity;
    private Fragment currentFragment;

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

        final ImageView typeImage = (ImageView) v.findViewById(R.id.item_image);
        final TextView itemName = (TextView) v.findViewById(R.id.item_name);
        final TextView itemNumber = (TextView) v.findViewById(R.id.item_number);
        final TextView typeName = (TextView) v.findViewById(R.id.item_type);

        if (position % 2 != 0) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_color));
        }

        final Item i = getItem(position);

        typeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), i.getType().getImage()));
        ImageButton deleteItem = (ImageButton) v.findViewById(R.id.item_delete);
        itemNumber.setText(String.valueOf(i.getNumber()));

        if (i.getRestaurant() != null){
            typeName.setText(i.getRestaurant().toString());
        } else {
            typeName.setText(i.getType().getName());
        }
        itemName.setText(i.getName());
        deleteItem.setTag(position);

        currentFragment = mainActivity.getViewerFragment(1);
        final OrderFragment of = (currentFragment instanceof OrderFragment) ? (OrderFragment)currentFragment : null;

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int index = (Integer) view.getTag();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Elimina")
                        .setMessage("Sei sicuro di voler eliminare " + getItem(index).getName() + " dai preferiti?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDataSet.remove(index);
                                notifyDataSetChanged();
                                //Changes the order fragment view
                                if (of != null) of.favoritesChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });
        return v;
    }
}
