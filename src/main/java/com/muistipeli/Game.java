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
    private HashMap<String, String> pelatutKortit = new HashMap<>();
    private HashMap<String, String> kortit = new HashMap<>();
    private int totalCardNumber = 0;
    private String deckName;
    @SuppressWarnings("unused")
    private int playSize;
    // DATABASE
    Database database = Database.getInstance();

    // Konstruktorit
    public Game(String deckName, int playSize) throws SQLException {
        this.deckName = deckName;
        this.playSize = playSize;
        startGame();
    }

    /******** GAME START ********/
    public void startGame() throws SQLException {
        kortit = database.getKortit(deckName);
        this.totalCardNumber = kortit.size();
        setOikea(oikea);
        setVaara(vaara);
        setJaljella(jaljella);
    }

    public void resetJaljella(int newPlaySize) {
        this.jaljella = newPlaySize;
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
            pelatutKortit.put(selectedKey, kortit.get(selectedKey));

            kortit.remove(selectedKey);
            keys.remove(randomIndex);
        }

        return shuffledList;
    }

    /******** GAME CARD ANSWER CHECKER ********/
    public boolean checkAnswer(String answer, String word) {
        if (answer.equalsIgnoreCase(pelatutKortit.get(word))) {
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
    public int realDeckSize() {
        return kortit.size();
    }

    public int totalDeckSize() {
        return this.totalCardNumber;
    }

    public int getOikea() {
        return oikea;
    }

    public int getVaara() {
        return vaara;
    }

    public int getJaljella() {
        return this.jaljella;
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

    public int getCountPlayedCards() {
        return pelatutKortit.size();
    }
}