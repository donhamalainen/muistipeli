package com.muistipeli;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import com.muistipeli.Screens.RootScreen;

public class App {
    public static void main(String[] args) {
        System.out.println("\nMuistipeli käynnistyy...\nAlustetaan...");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        new RootScreen();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            System.err.println("Virhe pelin käynnistyksessä: " + ex.getMessage());
        }
    }
}
