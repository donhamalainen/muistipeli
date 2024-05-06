package com.muistipeli;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

public class Game {

    // Attribuutit
    private int oikea = ConstantValue.DEFAULT_STATS_CORRECT;
    private int vaara = ConstantValue.DEFUALT_STATS_INCORRECT;
    private int jaljella = ConstantValue.DEFAULT_SIZE_OF_DECK;
    private String randomWord;
    private String correctWord;
    private String deckName;
    // DATABASE
    Database database = Database.getInstance();
    HashMap<String, String> sanat;
    // List<Pair<String, String>> sanat = new ArrayList<>();

    // Konstruktorit
    public Game(String deckName) throws SQLException {
        sanat = database.getKortit(deckName);
        setOikea(oikea);
        setVaara(vaara);
        setJaljella(jaljella);
    }

    // Metodit
    /******** GAME ********/
    public void startGame() {
        /*** START GAME ***/
        System.out.println("Aloitetaan peli...");

    }

    public boolean endGame() {
        /*** END GAME ***/
        if (jaljella == 0) {
            System.out.println("Lopetetaan peli...");
            return true;
        }

        return false;
    }

    public String getRandomWord() {
        // Tarkistus onko sanoja jäljellä ja ettei pakka ole tyhjä
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

    public boolean checkAnswer(String answer) {
        if (answer.equalsIgnoreCase(sanat.get(randomWord))) {
            oikea = oikea + 1;
            return true;
        }
        vaara = vaara + 1;
        return false;
    }

    private int deckSize(){
        return sanat.size();
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
        if (sanat.size() < 15){
            this.jaljella = sanat.size();
        }else{
        this.jaljella = jaljella;
        }
    }
}