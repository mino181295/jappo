package it.unibo.matteo.jappo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Score;
import it.unibo.matteo.jappo.Model.User;
import it.unibo.matteo.jappo.R;

public class HighscoresAdapter extends ArrayAdapter<Score> {

    List<Score> mDataSet;
    DataModel dm;
    User loggedUser;

    public HighscoresAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Score> objects) {
        super(context, resource, objects);
        mDataSet = objects;
        dm = new DataModel(getContext());
        dm.load();
        loggedUser = dm.getLoggedUser();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.highscore_item, null);

        final TextView nameText = (TextView)v.findViewById(R.id.highscore_name);
        final TextView positionText = (TextView)v.findViewById(R.id.highscores_position);
        final TextView timeText = (TextView)v.findViewById(R.id.highscores_time);
        final TextView valueText = (TextView)v.findViewById(R.id.highscore_value);

        Score s = getItem(position);

        if (s.getName().equals(loggedUser.getName())) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_color));
        }

        nameText.setText(s.getName());
        positionText.setText(String.valueOf(position+1));
        switch (position){
            case 0: positionText.setTextColor(ContextCompat.getColor(getContext(), R.color.fab_material_amber_500));
                break;
            case 1: positionText.setTextColor(Color.LTGRAY);
                break;
            case 2: positionText.setTextColor(ContextCompat.getColor(getContext(), R.color.background_color));
                break;
        }
        timeText.setText(s.getDate());
        valueText.setText(String.valueOf(s.getValue()));

        return v;
    }
}
