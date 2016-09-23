package com.blackout.blackoutmachine;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;


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
                if(endGame) {
                    // Set images back to normal
                    randomImage((ImageView)findViewById(R.id.slot1));
                    randomImage((ImageView)findViewById(R.id.slot2));
                    randomImage((ImageView)findViewById(R.id.slot3));
                    ((ImageView)findViewById(R.id.letras)).setImageResource(R.drawable.letrasjuego);
                    endGame = false;
                } else {
                    startGame();
                    endGame = true;
                }
            }

        };

        rlayout.setOnClickListener(bgListener);

    }

    protected void startGame() {
        rlayout.setOnClickListener(null);

        new CountDownTimer(3500, 500) {
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
                    img.setImageResource(R.drawable.botella);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.llavero);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.sticker);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.gorras);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.chupito);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.powerbank);
                } else if(Math.random() < 0.30) {
                    img.setImageResource(R.drawable.camiseta);
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
    }

    protected void randomImage(ImageView img) {
        if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.botella);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.llavero);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.sticker);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.gorras);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.chupito);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.powerbank);
        } else if(Math.random() < 0.30) {
            img.setImageResource(R.drawable.camiseta);
        }
    }

}
