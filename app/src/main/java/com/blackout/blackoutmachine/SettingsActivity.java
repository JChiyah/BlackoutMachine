package com.blackout.blackoutmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    protected void createGame(View view) {
        // Get all inputs and their values
        EditText[] inputs = {
                (EditText)findViewById(R.id.inputNombre), (EditText)findViewById(R.id.inputBotella),
                (EditText)findViewById(R.id.inputCamiseta), (EditText)findViewById(R.id.inputChupito),
                (EditText)findViewById(R.id.inputDescuento), (EditText)findViewById(R.id.inputGorra),
                (EditText)findViewById(R.id.inputLlavero), (EditText)findViewById(R.id.inputMechero),
                (EditText)findViewById(R.id.inputSticker)};

        String nombre = inputs[0].getText().toString();
        int[] values = new int[8];
        for(int i = 1; i < 9; i++) {
            if(inputs[i].getText().toString().matches(""))
                values[i-1] = 0;
            else
                values[i-1] = Integer.parseInt(inputs[i].getText().toString());
        }

        //Create object and add it to the DB
        GameObject game = new GameObject(0, nombre, values[0], values[1], values[2], values[3],
                values[4], values[5], values[6], values[7]);

        DBHandler db = new DBHandler(getApplicationContext());
        db.addGame(game);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
