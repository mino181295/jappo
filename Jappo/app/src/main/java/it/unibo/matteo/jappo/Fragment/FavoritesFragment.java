package it.unibo.matteo.jappo.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unibo.matteo.jappo.R;

public class FavoritesFragment extends Fragment {

    public static ArrayList<String> favorites;

    public FavoritesFragment() {
    }

    public static FavoritesFragment newInstance(ArrayList<String> fav) {
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
        return v;
    }

}
