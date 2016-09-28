package com.blackout.blackoutmachine;

/**
 * Created by JChiyah on 26/09/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DB_NAME = "BlackoutDB";
    // Principal table name
    private static final String GAMES_TABLE = "Partidas";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS " + GAMES_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT, "
                + "botella INT, "
                + "camiseta INT, "
                + "chupito INT, "
                + "descuento INT, "
                + "gorra INT, "
                + "llavero INT, "
                + "powerbank INT, "
                + "sticker INT"
                + ")";
        db.execSQL(CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE);
        // Creating tables again
        onCreate(db);
    }

    /**
     * Add a game to the DB
     * @param game
     */
    public void addGame(GameObject game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        /* Game values */
        values.put("nombre", game.getNombre());
        values.put("botella", game.getBotella());
        values.put("camiseta", game.getCamiseta());
        values.put("chupito", game.getChupito());
        values.put("descuento", game.getDescuento());
        values.put("gorra", game.getGorra());
        values.put("llavero", game.getLlavero());
        values.put("powerbank", game.getPowerbank());
        values.put("sticker", game.getSticker());

        // Inserting row
        db.insert(GAMES_TABLE, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Get a game from the DB
     * @param id
     * @return
     */
    public GameObject getGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GAMES_TABLE, new String[] { "id",
                "nombre", "botella", "camiseta", "chupito", "descuento",
                "gorra", "llavero", "powerbank", "sticker" }, "id" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        GameObject game = new GameObject(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)));
        // return game
        return game;
    }

    /**
     * Get all games from the DB
     * @return
     */
    public List<GameObject> getAllGames() {
        List<GameObject> gameList = new ArrayList<GameObject>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + GAMES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GameObject game = new GameObject(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)));

                // Adding contact to list
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        // return list
        return gameList;
    }

    /**
     * Update a game
     * @param game
     * @return
     */
    public int updateGame(GameObject game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        /* Game values */
        values.put("nombre", game.getNombre());
        values.put("botella", game.getBotella());
        values.put("camiseta", game.getCamiseta());
        values.put("chupito", game.getChupito());
        values.put("descuento", game.getDescuento());
        values.put("gorra", game.getGorra());
        values.put("llavero", game.getLlavero());
        values.put("powerbank", game.getPowerbank());
        values.put("sticker", game.getSticker());

        // Inserting row
        return db.update(GAMES_TABLE, values, "id" + " = ?",
                new String[]{String.valueOf(game.getId())});
    }

    /**
     * Delete a game
     * @param game
     */
    public void deleteGame(GameObject game) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GAMES_TABLE, "id" + " = ?",
                new String[] { String.valueOf(game.getId()) });
        db.close();
    }

}