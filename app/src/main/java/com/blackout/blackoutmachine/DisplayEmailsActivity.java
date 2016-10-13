package com.blackout.blackoutmachine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DisplayEmailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayemails);

        displayEmails();
    }

    protected void displayEmails() {
        DBHandler db = new DBHandler(getApplicationContext());
        List<String> emailList = db.getEmails(getIntent().getIntExtra("game_id", 0));
        db.close();

        LinearLayout layout = (LinearLayout) findViewById(R.id.listlayout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(String email : emailList) {
            if(!email.equals("")) {
                TextView text = new TextView(getApplicationContext());
                text.setText(email);
                text.setTextAppearance(getApplicationContext(), R.style.SettingsText);

                layout.addView(text, 0);
            }
        }
    }
}
