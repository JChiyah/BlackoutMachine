package com.blackout.blackoutmachine;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    protected boolean endGame = false;
    protected GameObject game;
    protected View.OnClickListener bgListener;
    protected FrameLayout rlayout;
    protected Random rdm = new Random();
    protected int resID;
    protected List<String> premios;
    protected String premio; // El proximo premio en salir
    protected HashMap<String, Integer> prizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get intent and game
        DBHandler db = new DBHandler(getApplicationContext());
        game = db.getGame(getIntent().getIntExtra("game_id", 0));
        db.close();
        //Toast toast = Toast.makeText(getApplicationContext(), "ID partida: " + getIntent().getIntExtra("game_id", 0), Toast.LENGTH_LONG);
        //toast.show();

        configureGame(game);

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
        int resID = res.getIdentifier(premios.get(rdm.nextInt(premios.size())) , "drawable", getPackageName());
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
                    resID = res.getIdentifier(premio , "drawable", getPackageName());

                    ((ImageView)findViewById(R.id.slot1)).setImageResource(resID);
                    ((ImageView)findViewById(R.id.slot2)).setImageResource(resID);
                    ((ImageView)findViewById(R.id.slot3)).setImageResource(resID);

                    selectPrize(premio);
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
                //Thread.sleep(sleep/4);
                displayWinText(); // Mostrar letras de premio

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void configureGame(GameObject game) {
        // Configurar partida
        premios = new ArrayList<String>();
        prizes = game.getPremios(game);
        for(HashMap.Entry<String, Integer> entry : prizes.entrySet()) {
            if((entry.getValue() != 0) && (!entry.getKey().equals("id")) && (entry.getKey() != null)) {
                premios.add(entry.getKey());
            }
        }
        if(premios.size() < 1) { // No hay mas premios a repartir
            killActivity();
        }

        selectPrize(null);
    }

    private void selectPrize(String premioAnterior) {
        // Quita una unidad del premio anterior
        if(premioAnterior != null) {
            prizes.put(premioAnterior, prizes.get(premioAnterior) - 1);
            // Reflection! Llama a una funcion dinamicamente
            String message = "no";
            try {
                GameObject.class.getMethod("set" + premioAnterior.substring(0, 1).toUpperCase() + premioAnterior.substring(1), Integer.TYPE).invoke(game, prizes.get(premioAnterior));
                message = premioAnterior + " restantes: " + prizes.get(premioAnterior);
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            } catch(InvocationTargetException e) {
                e.printStackTrace();
            }
            DBHandler db = new DBHandler(getApplicationContext());
            db.updateGame(game);
            db.close();

            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
        }

        try {
            // Seleccionar el proximo premio
            int i = 0;
            do {
                premio = premios.get(rdm.nextInt(premios.size()));
                i++;
                if (i > 100) // Parar en caso de que se trabe
                    killActivity();
            } while (prizes.get(premio) < 1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            killActivity();
        }
    }

    /**
     * Para los procesos y vuelve al menú principal, posiblemente porque no quedan premios
     */
    private void killActivity() {
        Toast.makeText(GameActivity.this, "No quedan premios en esta partida", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}

