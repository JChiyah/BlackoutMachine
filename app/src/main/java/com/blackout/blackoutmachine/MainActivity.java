package com.blackout.blackoutmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String ID = "com.blackout.blackoutmachine.ID";
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureButtons();
    }

    private void configureButtons() {
        LinearLayout[] buttons = {(LinearLayout)findViewById(R.id.gameButton1), (LinearLayout)findViewById(R.id.gameButton2), (LinearLayout)findViewById(R.id.gameButton3)};

        DBHandler db = new DBHandler(this);
        List<GameObject> games = db.getAllGames();

        if(!games.isEmpty()) {
            // Show games
            int i = 0;
            TextView[] ids = {(TextView)findViewById(R.id.game1), (TextView)findViewById(R.id.game2),(TextView)findViewById(R.id.game3)};
            for(GameObject game : games) {
                ids[i].setText(game.getNombre());
                id = game.getId();
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), GameActivity.class);
                        intent.putExtra(ID, id);
                        startActivity(intent);
                    }
                });
                i++;
            }
            if(i < 3) {
                // Assign listeners to configure game
                for(; i < 3; i++) {
                    buttons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        } else {
            // Assign listeners to configure game
            for(int i = 0; i < 3; i++) {
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

    }
}
