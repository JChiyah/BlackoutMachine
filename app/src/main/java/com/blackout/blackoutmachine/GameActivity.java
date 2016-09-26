package com.blackout.blackoutmachine;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    protected boolean endGame = false;
    protected View.OnClickListener bgListener;
    protected FrameLayout rlayout;
    protected Random rdm = new Random();
    protected int resID;
    protected String[] prizes = {"botella", "camiseta", "chupito",
            "descuento", "gorra", "llavero", "sticker", "powerbank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rlayout = (FrameLayout) findViewById(R.id.activity_game);

        bgListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(endGame) {
                    // Set images back to normal
                    randomImage((ImageView)findViewById(R.id.slot1));
                    randomImage((ImageView)findViewById(R.id.slot2));
                    randomImage((ImageView)findViewById(R.id.slot3));
                    ((ImageView)findViewById(R.id.premio)).setImageResource(android.R.color.transparent); // Set prize image to transparent
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

        new ImageChanger(8, 250).start();
    }

    protected void randomImage(ImageView img) {
        Resources res = getResources();
        int resID = res.getIdentifier(prizes[rdm.nextInt(prizes.length)] , "drawable", getPackageName());
        img.setImageResource(resID);
    }

    private class ImageChanger extends Thread {
        final int numberOfTimes;
        final long sleep;

        public ImageChanger(int numberOfTimes, long sleep) {
            this.numberOfTimes = numberOfTimes + rdm.nextInt(3);
            this.sleep = sleep;
        }

        private void changeImage() {
            GameActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    randomImage((ImageView)findViewById(R.id.slot1));
                    randomImage((ImageView)findViewById(R.id.slot2));
                    randomImage((ImageView)findViewById(R.id.slot3));
                }
            });
        }

        private void displayPrize() {
            GameActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Resources res = getResources();
                    resID = res.getIdentifier(prizes[rdm.nextInt(prizes.length)] , "drawable", getPackageName());

                    ((ImageView)findViewById(R.id.slot1)).setImageResource(resID);
                    ((ImageView)findViewById(R.id.slot2)).setImageResource(resID);
                    ((ImageView)findViewById(R.id.slot3)).setImageResource(resID);
                }
            });
        }

        private void displayWinText() {
            GameActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    ((ImageView)findViewById(R.id.letras)).setImageResource(R.drawable.letraspremio);
                    ((ImageView)findViewById(R.id.premio)).setImageResource(resID);
                    rlayout.setOnClickListener(bgListener);
                }
            });
        }

        @Override
        public void run() {
            try {
                // Juego
                for(int i = 0; i < numberOfTimes; i++) {
                    changeImage();
                    Thread.sleep(sleep);
                }
                displayPrize(); // Decidir y poner un premio
                Thread.sleep(sleep/2);
                displayWinText(); // Mostrar letras de premio

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

