package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JTextArea instructions;

    /******* KONSTRUKTORI *******/
    public HelpScreen(JPanel cards) throws SQLException {
        this.rootCards = cards;

        rootCardLayout = (CardLayout) rootCards.getLayout();
        this.setLayout(new GridBagLayout());
        initializeHelpScreen();
        run();
    }

    /******* ALUSTUS *******/
    private void initializeHelpScreen() throws SQLException {
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
            rootCardLayout.show(rootCards, screen);
        }
    }

    /******* ALUSTUS *******/
    private void run() {
        addBackButton();
        addInstructions();
    }

    private void addBackButton() {

        backButton = new JButton("Päävalikko");
        backButton.addActionListener(new Switcher(ConstantValue.ROOTSCREEN_STRING));
        backButton.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        backButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        backButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        backButton.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.insets = new Insets(50, 50, 0, 10);

        add(backButton, constraints);
    }

    private void addInstructions() {

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        
        add(panel, constraints);

        instructions = new JTextArea();
        instructions.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        instructions.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        instructions.setText("1. Aloita luomalla pakka Luo pakka valikossa\n" +
                "2. Tupla klikkaa luomaasi pakkaa, valikko aukeaa jossa pystyt lisäämään kortteja haluamallasi käännöksillä\n"
                +
                "3. Kun olet luonut kortit haluamaasi pakkaan, siirry päävalikkoon ja paina Pelaa nappia\n" +
                "4. Valitse pakka jolla haluat pelata, klikkaa Pelaa nappia ja syötä korttimäärä jonka haluat pelata\n"
                +
                "5. Peli alkaa, ruudullasi näkyy 1 sana kerrallaan, kirjoita sen käännös. Ruudun vasemmalta puolelta näet oikeat/väärät vastaukset sekä kuinka monta korttia on jäljellä");
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 50, 0, 10);
        add(instructions, constraints);
    }
}
