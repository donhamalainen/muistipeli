package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

import com.muistipeli.ConstantValue;

public class HelpScreen extends JPanel {

    /******* ATTRIBUUTIT *******/
    // Root Attributes
    private JPanel rootCards;
    private CardLayout rootCardLayout;

    /******* KONSTRUKTORI *******/
    public HelpScreen(JPanel cards) throws SQLException {
        this.rootCards = cards;
        rootCardLayout = (CardLayout) rootCards.getLayout();
        initializePlayScreen();
        run();
    }

    /******* ALUSTUS *******/
    private void initializePlayScreen() throws SQLException {
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());
    }

    /******* SWITCHER *******/
    private class Switcher implements ActionListener {
        String screen;

        Switcher(String selectedScreen) {
            this.screen = selectedScreen;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Vaihdetaan näkymään: " + screen);
            // rootCardLayout.show(rootCards, screen);
        }
    }

    /******* ALUSTUS *******/
    private void run() {

    }

}
