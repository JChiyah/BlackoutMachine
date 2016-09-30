package com.blackout.blackoutmachine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureButtons();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void configureButtons() {
        LinearLayout[] buttons = {(LinearLayout)findViewById(R.id.gameButton1), (LinearLayout)findViewById(R.id.gameButton2), (LinearLayout)findViewById(R.id.gameButton3)};

        DBHandler db = new DBHandler(this);
        List<GameObject> games = db.getAllGames();
        db.close();

        if(!games.isEmpty()) {
            Button[] infoButtons = {(Button)findViewById(R.id.info1), (Button)findViewById(R.id.info2), (Button)findViewById(R.id.info3)};
            Button[] deleteButtons = {(Button)findViewById(R.id.delete1), (Button)findViewById(R.id.delete2), (Button)findViewById(R.id.delete3)};

            // Show games
            int i = 0;
            TextView[] ids = {(TextView)findViewById(R.id.game1), (TextView)findViewById(R.id.game2),(TextView)findViewById(R.id.game3)};
            for(GameObject game : games) {
                ids[i].setText(game.getNombre());
                id = game.getId();
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), LoginActivity.class);
                        intent.putExtra("game_id", id);
                        startActivity(intent);
                    }
                });
                infoButtons[i].setVisibility(View.VISIBLE);
                infoButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        intent.putExtra("game_id", id);
                        startActivity(intent);
                    }
                });
                deleteButtons[i].setVisibility(View.VISIBLE);
                deleteButtons[i].setTag(id);
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

    protected void deleteGame(final View view) {
        // Alerta! Seguro?
        new AlertDialog.Builder(this)
                .setTitle("Eliminar partida")
                .setMessage("Estas seguro de querer eliminar esta partida?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

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
                .setNegativeButton(android.R.string.no, null).show();

    }

}
