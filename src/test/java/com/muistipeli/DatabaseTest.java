package com.muistipeli;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
    private static final int ArrayList = 0;
    private Database database;

    @Before
    public void setUp() throws SQLException {
        database = Database.getInstance();
        // Luodaan pakkat ja lisätään kortit
        database.addPakka("englanti");
        database.addPakka("suomi");

        database.addKortti("cat", "kissa", "englanti");
        database.addKortti("car", "auto", "englanti");

        database.addKortti("ihminen", "person", "suomi");
        database.addKortti("ikkuna", "window", "suomi");
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