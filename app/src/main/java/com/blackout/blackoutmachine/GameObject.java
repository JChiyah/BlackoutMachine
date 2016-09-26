package com.blackout.blackoutmachine;

/**
 * Created by JChiyah on 26/09/2016.
 */

public class GameObject {

    private int id;
    private String nombre;
    private int botella, camiseta, chupito, descuento, gorra, llavero, powerbank, sticker;

    public GameObject() {

    }

    public GameObject(int id, String nombre, int botella, int camiseta, int chupito,
                      int descuento, int gorra, int llavero, int powerbank, int sticker) {
        this.id = id;
        this.nombre = nombre;
        this.botella = botella;
        this.camiseta = camiseta;
        this.chupito = chupito;
        this.descuento = descuento;
        this.gorra = gorra;
        this.llavero = llavero;
        this.powerbank = powerbank;
        this.sticker = sticker;
    }

    /**
     * Getters and setters
     */
    public String getNombre() {
        return nombre;
    }
    public int getId() {
        return id;
    }
    public int getBotella() {
        return botella;
    }
    public int getCamiseta() {
        return camiseta;
    }
    public int getChupito() {
        return chupito;
    }
    public int getDescuento() {
        return descuento;
    }
    public int getGorra() {
        return gorra;
    }
    public int getLlavero() {
        return llavero;
    }
    public int getPowerbank() {
        return powerbank;
    }
    public int getSticker() {
        return sticker;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setBotella(int botella) {
        this.botella = botella;
    }
    public void setCamiseta(int camiseta) {
        this.camiseta = camiseta;
    }
    public void setChupito(int chupito) {
        this.chupito = chupito;
    }
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
    public void setGorra(int gorra) {
        this.gorra = gorra;
    }
    public void setLlavero(int llavero) {
        this.llavero = llavero;
    }
    public void setPowerbank(int powerbank) {
        this.powerbank = powerbank;
    }
    public void setSticker(int sticker) {
        this.sticker = sticker;
    }

}
