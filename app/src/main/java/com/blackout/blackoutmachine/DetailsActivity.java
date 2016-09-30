package com.blackout.blackoutmachine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.TreeMap;

public class DetailsActivity extends AppCompatActivity {

    protected EditText[] editTexts = new EditText[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        loadData();
    }

    private void loadData() {
        Resources res = getResources();
        DBHandler db = new DBHandler(getApplicationContext());
        GameObject game = db.getGame(getIntent().getIntExtra("game_id", 0));
        TreeMap<String, Integer> prizes = game.getPremios(game);
        db.close();

        TableLayout table1 = (TableLayout)findViewById(R.id.detailstable1);
        TableLayout table2 = (TableLayout)findViewById(R.id.detailstable2);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int i = 0;
        for(TreeMap.Entry<String, Integer> entry : prizes.entrySet()) {
            if((!entry.getKey().equals("id")) && (entry.getKey() != null)) {
                View row = inflater.inflate(R.layout.content_details, null);

                ImageView img = (ImageView)row.findViewById(R.id.imagenpremio);
                img.setImageResource(res.getIdentifier(entry.getKey(), "drawable", getPackageName()));

                TextView text = (TextView)row.findViewById(R.id.nombrepremio);
                text.setText(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1));

                TextView uds = (TextView)row.findViewById(R.id.cantidadpremio);
                uds.setText("" + entry.getValue() + " uds.");

                editTexts[i] = (EditText)row.findViewById(R.id.editpremio);
                editTexts[i].setText("" + entry.getValue());

                if(i < 4) {
                    table1.addView(row, 0);
                } else {
                    table2.addView(row, 0);
                }
                i++;
            }
        }
        ((TextView)findViewById(R.id.nombrepartida)).setText("Nombre: " + game.getNombre());
    }

    protected void editGame(View view) {
        if(((Button)findViewById(R.id.editbutton)).getText().equals("Guardar")) {
            saveGame();

            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        for(int i = 0; i < editTexts.length; i++) {
            editTexts[i].setVisibility(View.VISIBLE);
        }

        ((Button)findViewById(R.id.editbutton)).setText("Guardar");
        ((Button)findViewById(R.id.cancelbutton)).setVisibility(View.VISIBLE);
    }

    protected void cancelEdit(View view) {
        for(int i = 0; i < editTexts.length; i++) {
            editTexts[i].setVisibility(View.INVISIBLE);
        }
        ((Button)findViewById(R.id.editbutton)).setText("Modificar");
        ((Button)findViewById(R.id.cancelbutton)).setVisibility(View.INVISIBLE);
    }

    protected void saveGame() {

        // Inputs are saved to editTexts by alphabetical order
        int[] values = new int[8];
        String tmp;
        for(int i = 0; i < 8; i++) {
            tmp = editTexts[i].getText().toString();
            if(tmp.matches(""))
                values[i] = 0;
            else
                values[i] = Integer.parseInt(tmp);
        }
        // Open DB and get game
        DBHandler db = new DBHandler(getApplicationContext());
        GameObject game = db.getGame(getIntent().getIntExtra("game_id", 0));

        //Create object and add it to the DB
        GameObject updatedGame = new GameObject(game.getId(), game.getNombre(), values[0], values[1], values[2], values[3],
                values[4], values[5], values[6], values[7]);

        db.updateGame(updatedGame);
        db.close();
    }
}
