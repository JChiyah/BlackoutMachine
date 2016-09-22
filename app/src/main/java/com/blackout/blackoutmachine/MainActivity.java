package com.blackout.blackoutmachine;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.graphics.Color;


public class MainActivity extends Activity {

    protected boolean endGame = false;
    protected OnClickListener bgListener;
    protected FrameLayout rlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlayout = (FrameLayout) findViewById(R.id.activity_main);

        bgListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(endGame = true) {
                    // Set images back to normal
                    ((ImageView)findViewById(R.id.slot1)).setBackgroundColor(Color.parseColor("black"));
                    ((ImageView)findViewById(R.id.slot2)).setBackgroundColor(Color.parseColor("black"));
                    ((ImageView)findViewById(R.id.slot3)).setBackgroundColor(Color.parseColor("black"));
                    ((ImageView)findViewById(R.id.letras)).setImageResource(R.drawable.letrasjuego);
                    endGame = false;
                }
                startGame();
            }

        };

        rlayout.setOnClickListener(bgListener);

    }

    protected void startGame() {
        rlayout.setOnClickListener(null);

        new CountDownTimer(3000, 300) {
            ImageView slot1 = (ImageView) findViewById(R.id.slot1);
            ImageView slot2 = (ImageView) findViewById(R.id.slot2);
            ImageView slot3 = (ImageView) findViewById(R.id.slot3);
            int i = 1;

            public void onTick(long millisUntilFinished) {
                switch(i) {
                    case 1:
                        randomImage(slot1);
                        i++;
                    case 2:
                        randomImage(slot2);
                        i++;
                    case 3:
                        randomImage(slot3);
                        i = 1;
                }
            }

            protected void randomImage(ImageView img) {
                if(Math.random() < 0.30) {
                    //img.setImageResource(R.drawable.botella);
                    img.setBackgroundColor(Color.parseColor("red"));
                } else if(Math.random() < 0.30) {
                    //img.setImageResource(R.drawable.llavero);
                    img.setBackgroundColor(Color.parseColor("blue"));
                } else if(Math.random() < 0.30) {
                    //img.setImageResource(R.drawable.llavero);
                    img.setBackgroundColor(Color.parseColor("yellow"));
                } else if(Math.random() < 0.30) {
                    //img.setImageResource(R.drawable.llavero);
                    img.setBackgroundColor(Color.parseColor("green"));
                }
            }

            public void onFinish() {
                slot1.setImageResource(R.drawable.botella);
                slot2.setImageResource(R.drawable.botella);
                slot3.setImageResource(R.drawable.botella);
                ((ImageView)findViewById(R.id.letras)).setImageResource(R.drawable.letraspremio);
                rlayout.setOnClickListener(bgListener);
            }
        }.start();
        endGame = true;
    }

}
