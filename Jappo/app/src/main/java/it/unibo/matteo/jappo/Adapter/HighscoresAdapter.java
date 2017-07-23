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
    DataModel dataModel;
    User loggedUser;

    public HighscoresAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Score> objects) {
        super(context, resource, objects);
        mDataSet = objects;

        dataModel = new DataModel(getContext());
        dataModel.load();

        loggedUser = dataModel.getLoggedUser();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.highscore_item, null);

        final TextView mPositionText = (TextView)v.findViewById(R.id.highscores_position);
        final TextView mValueText = (TextView)v.findViewById(R.id.highscore_value);
        final TextView mNameText = (TextView)v.findViewById(R.id.highscore_name);
        final TextView mTimeText = (TextView)v.findViewById(R.id.highscores_time);

        Score score = getItem(position);

        if (score.getName().equals(loggedUser.getName())) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_color));
        }

        mNameText.setText(score.getName());
        mPositionText.setText(String.valueOf(position+1));
        switch (position){
            case 0: mPositionText.setTextColor(ContextCompat.getColor(getContext(), R.color.fab_material_amber_500));
                break;
            case 1: mPositionText.setTextColor(Color.LTGRAY);
                break;
            case 2: mPositionText.setTextColor(ContextCompat.getColor(getContext(), R.color.background_color));
                break;
        }
        mTimeText.setText(score.getDate());
        mValueText.setText(String.valueOf(score.getValue()));

        return v;
    }
}
