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
import java.util.TreeMap;

public class GameActivity extends AppCompatActivity {

    protected GameObject game;
    protected FrameLayout rlayout;
    protected Random rand = new Random();
    protected int resID;
    protected List<String> premios;
    protected String premio; // El proximo premio en salir
    protected TreeMap<String, Integer> prizes;
    // Algoritmo para sacar imgs
    protected int totalSum;

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


        rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    protected void startGame() {
        rlayout.setOnClickListener(null);

        new ImageChanger(8, 250).start();
    }

    protected void randomImage(ImageView img) {
        Resources res = getResources();
        resID = res.getIdentifier(premios.get(rand.nextInt(premios.size())) , "drawable", getPackageName());
        img.setImageResource(resID);
    }

    private class ImageChanger extends Thread {
        final int numberOfTimes;
        final long sleep;

        public ImageChanger(int numberOfTimes, long sleep) {
            this.numberOfTimes = numberOfTimes + rand.nextInt(3);
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

                }
            });
        }

        private void displayWinText() {
            GameActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    ((ImageView)findViewById(R.id.letras)).setImageResource(R.drawable.letraspremio);
                    ((ImageView)findViewById(R.id.premio)).setImageResource(resID);
                    // Change a listener for main screen
                    rlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.putExtra("game_id", getIntent().getIntExtra("game_id", 0));
                            startActivity(intent);
                        }
                    });
                }
            });
        }

        @Override
        public void run() {
            try {
                // Juego
                for(int i = 0; i < numberOfTimes; i++) {
                    Thread.sleep(sleep);
                    changeImage();
                }
                displayPrize(); // Decidir y poner un premio
                //Thread.sleep(sleep/4);
                displayWinText(); // Mostrar letras de premio
                updatePrize(premio);
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
                totalSum = totalSum + entry.getValue();
            }
        }
        if(totalSum < 1) { // No hay mas premios a repartir
            killActivity();
        }

        selectPrize();
    }

    private void updatePrize(String premioAnterior) {
        // Quita una unidad del premio anterior
        prizes.put(premioAnterior, prizes.get(premioAnterior) - 1);

        // Reflection! Llama a una funcion dinamicamente
        try {
            GameObject.class.getMethod("set" + premioAnterior.substring(0, 1).toUpperCase() + premioAnterior.substring(1), Integer.TYPE).invoke(game, prizes.get(premioAnterior));
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
        totalSum--;
        if(totalSum < 1) {
            // Change a listener for main screen
            rlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    killActivity();
                }
            });
        }
    }

    /**
     * Selecciona un premio para dar en esta ronda
     */
    private void selectPrize() {
        try {
            // Seleccionar el proximo premio
            do {
                int index = rand.nextInt(totalSum);
                int sum = 0;
                int i = 0;
                while(sum < index ) {
                    sum = sum + (int)GameObject.class.getMethod("get" + premios.get(i).substring(0, 1).toUpperCase() + premios.get(i).substring(1)).invoke(game);;
                    i++;
                }
                premio = premios.get(Math.max(0,i-1));
            } while (prizes.get(premio) < 1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            killActivity();
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        } catch(InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Para los procesos y vuelve al menú principal, posiblemente porque no quedan premios
     */
    private void killActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(GameActivity.this, "No quedan premios en esta partida", Toast.LENGTH_LONG).show();
    }

    protected void share() {

    }

}

