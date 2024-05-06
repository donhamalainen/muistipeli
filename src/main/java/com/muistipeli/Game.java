package com.muistipeli;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

public class Game {

    // Attribuutit
    private int oikea;
    private int vaara;
    private int jaljella;
    private String randomWord;
    private String correctWord;
    private String deckName;
    // DATABASE
    Database database = Database.getInstance();
    HashMap<String, String> sanat;
    // List<Pair<String, String>> sanat = new ArrayList<>();

    // Konstruktorit
    public Game(String deckName) throws SQLException {
        oikea = ConstantValue.DEFAULT_STATS_CORRECT;
        vaara = ConstantValue.DEFUALT_STATS_INCORRECT;
        jaljella = ConstantValue.DEFAULT_SIZE_OF_DECK;
        sanat = database.getKortit(deckName);
    }

    // Metodit
    /******** GAME ********/
    public void startGame() {
        /*** START GAME ***/
        System.out.println("Aloitetaan peli...");

    }

    public boolean endGame() {
        /*** END GAME ***/
        if (jaljella <= 0) {
            System.out.println("Lopetetaan peli...");
            return true;
        }

        return false;
    }

    public String getRandomWord() {
        // Tarkistus onko sanoja jäljellä ja ettei pakka ole tyh
        if (jaljella <= 0 || sanat.isEmpty()) {
            return null;
        }
        // Otetaan random indeksi hashMapin koon mukaan
        int randomIndex = new Random().nextInt(sanat.size());

        // Haetaan satunnais indeksillä sana
        randomWord = (String) sanat.keySet().toArray()[randomIndex];
        jaljella = jaljella - 1;
        return randomWord;
    }

    public void printSize() {
        System.out.println(sanat.size());
    }

    public boolean checkAnswer(String answer) {

        System.out.println(sanat.get(randomWord));

        if (answer.equalsIgnoreCase(sanat.get(randomWord))) {
            oikea = oikea + 1;
            return true;
        }
        vaara = vaara + 1;
        return false;
    }

    public int getOikea() {
        return oikea;
    }

    public int getVaara() {
        return vaara;
    }

    public int getJaljella() {
        return jaljella;
    }

    public void setOikea(int oikea) {
        this.oikea = oikea;
    }

    public void setVaara(int vaara) {
        this.vaara = vaara;
    }

    public void setJaljella(int jaljella) {
        this.jaljella = jaljella;
    }

}