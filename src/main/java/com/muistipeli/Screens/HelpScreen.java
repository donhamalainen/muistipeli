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
    private JButton backButton;
    private GridBagConstraints constraints;
    private JLabel ohjeet;
    /******* KONSTRUKTORI *******/
    public HelpScreen(JPanel cards) throws SQLException {
        this.rootCards = cards;
        
        rootCardLayout = (CardLayout) rootCards.getLayout();
        this.setLayout(new GridBagLayout());
        addBackButton();
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
            rootCardLayout.show(rootCards, screen);
        }
    }

    /******* ALUSTUS *******/
    private void run() {

    }

    private void addBackButton(){


        backButton = new JButton("Päävalikko");
        backButton.addActionListener(new Switcher(ConstantValue.ROOTSCREEN_STRING));
        backButton.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        backButton.setFont(new Font("Arial", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        backButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);


        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(50, 50, 10, 10);

        add(backButton, constraints);
    }
}
