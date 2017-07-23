package it.unibo.matteo.jappo.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.R;

import static it.unibo.matteo.jappo.R.id.map;

/**
 * Fragment that set up a new {@link it.unibo.matteo.jappo.Model.Order} in a specific {@link Restaurant}
 */
public class NewOrderFragment extends Fragment implements OnMapReadyCallback {

    public static final int LOCATION_REQUEST = 7652;

    private GoogleMap googleMap;
    private Spinner mSpinner;
    private View mView;

    private OnNewOrderInteractionListener mListener;

    private static ArrayList<Restaurant> restaurants;

    public NewOrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static NewOrderFragment newInstance(ArrayList<Restaurant> rest) {
        NewOrderFragment fragment = new NewOrderFragment();
        restaurants = rest;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_order, container, false);
        mSpinner = (Spinner) mView.findViewById(R.id.restaurant_spinner);

        /* Spinner setup with the model restaurants */
        ArrayAdapter<Restaurant> restaurantAdapter = new ArrayAdapter<>(getContext(), R.layout.resaturant_item,
                R.id.restaurant_name_text, restaurants);
        restaurantAdapter.setDropDownViewResource(R.layout.resaturant_item);
        mSpinner.setAdapter(restaurantAdapter);
        mSpinner.setSelection(0);

        /* Left and Right navigation buttons */
        ImageButton mLeftNavigation = (ImageButton) mView.findViewById(R.id.restaurant_left);
        mLeftNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mSpinner.getCount();
                int next = (mSpinner.getSelectedItemPosition()) - 1;
                next = next < 0 ? count-1 : next;
                mSpinner.setSelection(next);
            }
        });
        ImageButton mRightNavigation = (ImageButton) mView.findViewById(R.id.restaurant_right);
        mRightNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mSpinner.getCount();
                int next = (mSpinner.getSelectedItemPosition()) + 1;
                next = next > count-1 ? 0 : next;
                mSpinner.setSelection(next);
            }
        });
        /* Item selection listener setup */
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant r = (Restaurant) mSpinner.getSelectedItem();
                setMapLocation(r);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button startOrderButton = (Button) mView.findViewById(R.id.start_order_button);
        startOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant r = (Restaurant) mSpinner.getSelectedItem();
                onButtonPressed(r);
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mapView = (MapView) mView.findViewById(map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewOrderInteractionListener) {
            mListener = (OnNewOrderInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewOrderInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* Method that is triggered when the GMap is ready to use from the API */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            googleMap.setMyLocationEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
        }
        setMapLocation((Restaurant) mSpinner.getSelectedItem());
    }

    /**
     * Sets the {@link GoogleMap} focus on a {@link MarkerOptions} with a targer {@link Restaurant}
     * @param r the restaurant target of the animation movement of the {@link GoogleMap}
     */
    public void setMapLocation(Restaurant r){
        if (googleMap != null){
            LatLng coordinates = r.getCoordinates();
            MarkerOptions mMarker = new MarkerOptions().position(coordinates)
                    .title(r.getName())
                    .snippet(r.getAddress());
            /* Clears the previous targets */
            googleMap.clear();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15.0f));
            googleMap.addMarker(mMarker);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    double destinationLatitude = marker.getPosition().latitude;
                    double destinationLongitude = marker.getPosition().longitude;
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", destinationLatitude, destinationLongitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                    return false;
                }
            });
        }
    }

    public void onButtonPressed(Restaurant r) {
        if (mListener != null) {
            mListener.onNewOrderInteraction(r);
        }
    }

    /**
     * Interface that permits the interaction with the attached {@link ActivityCompat}
     */
    public interface OnNewOrderInteractionListener {
        void onNewOrderInteraction(Restaurant r);
    }
}
