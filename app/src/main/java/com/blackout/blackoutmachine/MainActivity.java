package com.blackout.blackoutmachine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    protected int id;
    protected boolean internetAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if there is internet connection
        InternetCheck check = new InternetCheck();
        try {
            if(!check.execute().get()) {
                Toast.makeText(MainActivity.this, "No se ha podido conectar con Internet", Toast.LENGTH_LONG).show();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }

        loadData();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    protected void loadData() {
        GridLayout mainLayout = (GridLayout)findViewById(R.id.gamesLayout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DBHandler db = new DBHandler(this);
        List<GameObject> games = db.getAllGames();
        db.close();

        int i = 0;
        if(!games.isEmpty()) {
            List<View> frames = new ArrayList<View>();
            for(GameObject game : games) {
                final int id = game.getId();
                View frame = inflater.inflate(R.layout.content_main, null);

                TextView text = (TextView)frame.findViewById(R.id.gameName);
                text.setText(game.getNombre());

                Button details = (Button)frame.findViewById(R.id.gameDetails);
                details.setVisibility(View.VISIBLE);
                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra("game_id", id);
                        startActivity(intent);
                    }
                });

                Button detele = (Button)frame.findViewById(R.id.gameDelete);
                detele.setVisibility(View.VISIBLE);
                detele.setTag(game.getId());

                ((LinearLayout)frame.findViewById(R.id.gameFrame)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), LoginActivity.class);
                        intent.putExtra("game_id", id);
                        intent.putExtra("internet", internetAvailable);
                        startActivity(intent);
                    }
                });

                frames.add(frame);
                i++;
            }
            if(i < 3) {
                for(;i < 3; i++) {
                    View frame = inflater.inflate(R.layout.content_main, null);
                    ((LinearLayout)frame.findViewById(R.id.gameFrame)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                            startActivity(intent);
                        }
                    });

                    mainLayout.addView(frame, 0);
                }
            } else {
                View frame = inflater.inflate(R.layout.content_main, null);
                ((LinearLayout)frame.findViewById(R.id.gameFrame)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                });

                mainLayout.addView(frame, 0);
            }
            for(int u = frames.size() - 1; u >= 0; u--) {
                mainLayout.addView(frames.get(u), 0);
            }
        } else {
            for(; i < 3; i++) {
                View frame = inflater.inflate(R.layout.content_main, null);
                ((LinearLayout)frame.findViewById(R.id.gameFrame)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                });

                mainLayout.addView(frame, 0);
            }
        }
    }

    protected void deleteGame(final View view) {
        // Alerta! Seguro?
        new AlertDialog.Builder(this)
                .setTitle("Eliminar partida")
                .setMessage(getResources().getString(R.string.deletequestion))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.alertyes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Delete game
                        DBHandler db = new DBHandler(getApplicationContext());
                        db.deleteGame(db.getGame((Integer)view.getTag()));
                        db.close();

                        Toast.makeText(MainActivity.this, "Partida eliminada", Toast.LENGTH_SHORT).show();

                        // Reload
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                    }})
                .setNegativeButton(R.string.alertno, null).show();

    }

    private class InternetCheck extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            internetAvailable = hasInternetAccess(getApplicationContext());
            return internetAvailable;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }

        public boolean hasInternetAccess(Context context) {
            if (isNetworkAvailable()) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)(new URL("http://clients3.google.com/generate_204").openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

}
