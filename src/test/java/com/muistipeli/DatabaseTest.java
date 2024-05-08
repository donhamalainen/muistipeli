package com.muistipeli;

import java.sql.SQLException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
    private Database database;

    @Before
    public void setUp() throws SQLException {
        database = Database.getInstance();
        // Luodaan pakkat ja lisätään kortit
        database.addPakka("englanti");
        database.addPakka("suomi");
        database.addPakka("ruotsi");
        database.addPakka("saksa");

        database.addKortti("cat", "kissa", "englanti");
        database.addKortti("dog", "koira", "englanti");
        database.addKortti("house", "talo", "englanti");
        database.addKortti("car", "auto", "englanti");
        database.addKortti("tree", "puu", "englanti");
        database.addKortti("book", "kirja", "englanti");
        database.addKortti("computer", "tietokone", "englanti");
        database.addKortti("apple", "omena", "englanti");
        database.addKortti("train", "juna", "englanti");
        database.addKortti("bicycle", "polkupyörä", "englanti");
        database.addKortti("phone", "puhelin", "englanti");
        database.addKortti("flower", "kukka", "englanti");
        database.addKortti("window", "ikkuna", "englanti");
        database.addKortti("chair", "tuoli", "englanti");
        database.addKortti("table", "pöytä", "englanti");
        database.addKortti("fish", "kala", "englanti");
        database.addKortti("bread", "leipä", "englanti");
        database.addKortti("water", "vesi", "englanti");
        database.addKortti("milk", "maito", "englanti");
        database.addKortti("lamp", "lamppu", "englanti");
        database.addKortti("shirt", "paita", "englanti");
        database.addKortti("shoe", "kenkä", "englanti");
        database.addKortti("hat", "hattu", "englanti");
        database.addKortti("school", "koulu", "englanti");
        database.addKortti("pencil", "kynä", "englanti");
        database.addKortti("sun", "aurinko", "englanti");
        database.addKortti("moon", "kuu", "englanti");
        database.addKortti("star", "tähti", "englanti");
        database.addKortti("mountain", "vuori", "englanti");
        database.addKortti("river", "joki", "englanti");
        database.addKortti("lake", "järvi", "englanti");
        database.addKortti("beach", "ranta", "englanti");
        database.addKortti("forest", "metsä", "englanti");
        database.addKortti("rain", "sade", "englanti");
        database.addKortti("snow", "lumi", "englanti");
        database.addKortti("wind", "tuuli", "englanti");
        database.addKortti("cloud", "pilvi", "englanti");
        database.addKortti("sky", "taivas", "englanti");
        database.addKortti("city", "kaupunki", "englanti");
        database.addKortti("village", "kylä", "englanti");
        database.addKortti("farmer", "maanviljelijä", "englanti");
        database.addKortti("nurse", "sairaanhoitaja", "englanti");
        database.addKortti("doctor", "lääkäri", "englanti");
        database.addKortti("fire", "tuli", "englanti");
        database.addKortti("music", "musiikki", "englanti");
        database.addKortti("painting", "maalaus", "englanti");
        database.addKortti("camera", "kamera", "englanti");
        database.addKortti("film", "elokuva", "englanti");

        database.addKortti("ihminen", "person", "suomi");
        database.addKortti("ikkuna", "window", "suomi");

        database.addKortti("hund", "koira", "ruotsi");
        database.addKortti("resa", "matkustaa", "ruotsi");

    }

    @Test
    public void testPrintAll() throws SQLException {
        HashMap<String, String> allPakka = database.getAllPakka();
        System.out.println("Kaikki pakat:\n");
        for (Map.Entry<String, String> pakka : allPakka.entrySet()) {
            String pakkaName = (String) pakka.getKey();
            System.out.println(pakkaName + " pakka:");
            HashMap<String, String> kortit = database.getKortit(pakkaName);
            for (Map.Entry<String, String> entry : kortit.entrySet()) {
                System.out.println("Sana: " + entry.getKey() + ", Käännös: " + entry.getValue());
            }
            System.out.println();
        }
    }

    @Test
    public void testChangeNamePakka() throws SQLException {
        database.setPakkaName("englanti", "ruotsi");
    }
}