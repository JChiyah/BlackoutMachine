package com.blackout.blackoutmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OfflineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void playGame(View view) {
        String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(OfflineActivity.this, R.string.wrongemail, Toast.LENGTH_LONG).show();
            return ;
        }

        saveEmail(email);

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game_id", getIntent().getIntExtra("game_id", 0));
        startActivity(intent);
    }

    protected void saveEmail(String email) {
        DBHandler db = new DBHandler(getApplicationContext());
        db.addEmail(getIntent().getIntExtra("game_id", 0), email);
        db.close();
    }
}
