package it.unibo.matteo.jappo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.unibo.matteo.jappo.Adapter.CompletedAdapter;
import it.unibo.matteo.jappo.Adapter.FavoritesAdapter;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Order;
import it.unibo.matteo.jappo.R;

import static it.unibo.matteo.jappo.R.id.container;

public class CompletedFragment extends Fragment {

    private CompletedAdapter completedAdapter;
    private ListView mCompletedList;

    private TextView mEmptyLabel;

    private static Order order;
    private static ArrayList<Item> arrivedItems;

    public CompletedFragment() {
    }

    public static CompletedFragment newInstance(Order o) {
        order = o;
        arrivedItems = o.getArrivedItems();
        return new CompletedFragment();
    }

    public static CompletedFragment newInstance() {
        order = null;
        arrivedItems = new ArrayList<>();
        return new CompletedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_completed, container, false);

        mEmptyLabel = (TextView)v.findViewById(R.id.infot_text);

        mCompletedList = (ListView)v.findViewById(R.id.completed_list);
        completedAdapter = new CompletedAdapter(getContext(), R.layout.completed_item, arrivedItems);
        mCompletedList.setAdapter(completedAdapter);

        return v;
    }

    public void refreshCompleted(){
        if (completedAdapter != null) {
            completedAdapter.notifyDataSetChanged();
        }
    }

}
