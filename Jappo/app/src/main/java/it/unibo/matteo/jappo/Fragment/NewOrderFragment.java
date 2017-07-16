package it.unibo.matteo.jappo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.R;

import static android.os.Build.VERSION_CODES.M;
import static it.unibo.matteo.jappo.R.id.map;

public class NewOrderFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap googleMap;
    private MapView mapView;

    View mView;

    public static ArrayList<Restaurant> restourants;

    public NewOrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static NewOrderFragment newInstance(ArrayList<Restaurant> rest) {
        NewOrderFragment fragment = new NewOrderFragment();
        restourants = rest;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_order, container, false);
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.restaurant_spinner);

        //TODO Layout
        //ArrayAdapter<String> restaurantAdapter = new ArrayAdapter<String>();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(map);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

}
