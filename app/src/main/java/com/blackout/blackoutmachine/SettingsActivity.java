package com.blackout.blackoutmachine;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;

public class SettingsActivity extends AppCompatActivity {

    protected EditText[] editTexts = new EditText[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadData();
    }

    protected void loadData() {
        Resources res = getResources();
        GameObject game = new GameObject(); // Get an empty game for names and images
        TreeMap<String, Integer> prizes = game.getPremios(game);

        TableLayout table1 = (TableLayout)findViewById(R.id.detailstable1);
        TableLayout table2 = (TableLayout)findViewById(R.id.detailstable2);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int i = 0;
        for(TreeMap.Entry<String, Integer> entry : prizes.entrySet()) {
            if((!entry.getKey().equals("id")) && (entry.getKey() != null)) {
                View row = inflater.inflate(R.layout.content_settings, null);

                ImageView img = (ImageView)row.findViewById(R.id.imagenpremio);
                img.setImageResource(res.getIdentifier(entry.getKey(), "drawable", getPackageName()));

                TextView text = (TextView)row.findViewById(R.id.nombrepremio);
                text.setText(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1));

                editTexts[i] = (EditText)row.findViewById(R.id.editpremio);

                if(i < 4) {
                    table1.addView(row, 0);
                } else {
                    table2.addView(row, 0);
                }
                i++;
            }
        }
    }

    protected void createGame(View view) {

        String nombre = ((EditText)findViewById(R.id.inputNombre)).getText().toString();
        if(nombre.equals("")) {
            Toast.makeText(SettingsActivity.this, "Debes especificar un nombre para la partida", Toast.LENGTH_SHORT).show();
            return ;
        }

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

        //Create object and add it to the DB
        GameObject game = new GameObject(0, nombre, values[0], values[1], values[2], values[3],
                values[4], values[5], values[6], values[7]);

        DBHandler db = new DBHandler(getApplicationContext());
        db.addGame(game);
        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
