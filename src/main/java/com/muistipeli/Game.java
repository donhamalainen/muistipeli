package com.muistipeli;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {

    // Attribuutit
    private Random random = new Random();
    private int oikea = ConstantValue.DEFAULT_STATS_CORRECT;
    private int vaara = ConstantValue.DEFUALT_STATS_INCORRECT;
    private int jaljella = ConstantValue.DEFAULT_SIZE_OF_DECK;
    private int pelatutKortit;
    private HashMap<String, String> kortit = new HashMap<>();
    // DATABASE
    Database database = Database.getInstance();

    // Konstruktorit
    public Game(String deckName) throws SQLException {
        startGame(deckName);
    }

    /******** GAME START ********/
    public void startGame(String deckName) throws SQLException {
        kortit = database.getKortit(deckName);
        setOikea(oikea);
        setVaara(vaara);
        setJaljella(jaljella);

    }

    /******** GAME END ********/
    public void endGame() {
        this.oikea = ConstantValue.DEFAULT_STATS_CORRECT;
        this.vaara = ConstantValue.DEFUALT_STATS_INCORRECT;
        this.jaljella = ConstantValue.DEFAULT_SIZE_OF_DECK;
        kortit = new HashMap<>();
    }

    /******** GAME CARD SHUFFLE ********/
    public HashMap<String, String> getRandomCards(int shuffleCount) {
        HashMap<String, String> shuffledList = new HashMap<>();
        List<String> keys = new ArrayList<>(kortit.keySet());

        if (shuffleCount > kortit.size()) {
            shuffleCount = kortit.size();
        }

        for (int i = 0; i < shuffleCount; i++) {
            int randomIndex = random.nextInt(keys.size());
            String selectedKey = keys.get(randomIndex);
            shuffledList.put(selectedKey, kortit.get(selectedKey));
            keys.remove(randomIndex);
        }

        return shuffledList;
    }

    /******** GAME CARD ANSWER CHECKER ********/
    public boolean checkAnswer(String answer, String word) {
        if (answer.equalsIgnoreCase(kortit.get(word))) {
            oikea++;
            jaljella--;
            return true;
        } else {
            vaara++;
            jaljella--;
            return false;
        }
    }

    /******** GETTERS & SETTERS ********/
    public int deckSize() {
        return kortit.size();
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

        if (kortit.size() < 15) {
            this.jaljella = kortit.size();
        } else {
            this.jaljella = jaljella;
        }
    }

    public int getPelatutKortit() {
        return pelatutKortit;
    }
}