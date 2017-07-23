package it.unibo.matteo.jappo.Activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.unibo.matteo.jappo.Adapter.CompletedAdapter;
import it.unibo.matteo.jappo.Adapter.HighscoresAdapter;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Score;
import it.unibo.matteo.jappo.R;

import static java.security.AccessController.getContext;

/**
 * Activity that shows the scores of the user registered to the platform.
 * Every user has a score based on the closed orders that has products.
 */
public class HighscoresActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ArrayList<Score> highscores;

    private HighscoresAdapter highscoresAdapter;
    private ListView mHighscoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        toolbar = (Toolbar) findViewById(R.id.toolbar_highscores);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageButton backButton =  (ImageButton) toolbar.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final DataModel dataModel = new DataModel(getApplicationContext());
        /* Async task that loads the highscores to the ListView */
        AsyncTask<Void, Void, ArrayList<Score>> mHighscoresTask = new AsyncTask<Void, Void, ArrayList<Score>>() {
            @Override
            protected ArrayList<Score> doInBackground(Void... voids) {
                return dataModel.getHighscores();
            }

            @Override
            protected void onPostExecute(ArrayList<Score> resultHighscores) {
                /* Adapter setup and highscores listView component creation */
                highscores = resultHighscores;
                mHighscoresList = (ListView)findViewById(R.id.highscores_list);
                highscoresAdapter = new HighscoresAdapter(getApplicationContext(), R.layout.highscore_item, highscores);
                mHighscoresList.setAdapter(highscoresAdapter);
            }
        };
        mHighscoresTask.execute((Void) null);
    }
}
