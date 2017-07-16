package it.unibo.matteo.jappo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Adapter.FavoritesAdapter;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.R;

public class FavoritesFragment extends Fragment {

    public static ArrayList<Item> favorites;
    private ListView mFavoriteList;

    public FavoritesFragment() {
    }

    public static FavoritesFragment newInstance(ArrayList<Item> fav) {
        FavoritesFragment fragment = new FavoritesFragment();
        favorites = fav;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        mFavoriteList = (ListView)v.findViewById(R.id.favorites_list);
        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(getContext(), R.layout.favorite_item, favorites);
        mFavoriteList.setAdapter(favoritesAdapter);

        return v;
    }

}
