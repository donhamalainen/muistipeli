package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import com.muistipeli.ConstantValue;
import com.muistipeli.Database;

public class RootScreen extends JFrame {
    /******* ATTRIBUTES *******/
    // Database
    private Database database;
    // Dimension
    private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    // GridBagConstraints
    GridBagConstraints constraints = new GridBagConstraints();
    // Layout
    private JPanel rootCards, buttonPanel;
    private CardLayout rootCardLayout;
    // Painikkeet
    private JButton playButton, deckButton, helpButton;
    // Näkymät
    PlayScreen PLAYSCREEN;
    DeckScreen DECKSCREEN;
    HelpScreen HELPSCREEN;
    InGameScreen INGAMESCREEN;

    /******* KONSTRUKTORI *******/
    public RootScreen() throws SQLException {
        database = Database.getInstance();
        initializeRootScreen();

        rootCardLayout = new CardLayout();
        rootCards = new JPanel(rootCardLayout);
        getContentPane().add(rootCards);

        run();
        add(rootCards);
        setVisible(true);
    }

    /******* ALUSTUS *******/
    private void initializeRootScreen() {
        setTitle("Muistipeli");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(dimension.width, dimension.height);
        setMinimumSize(new Dimension(ConstantValue.DEFAULT_MIN_WIDTH, ConstantValue.DEFAULT_MIN_HEIHGT));
        setLocationRelativeTo(null);
    }

    /******* RUN *******/
    public void run() throws SQLException {
        // PANEELIT
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        rootCards.add(mainPanel, ConstantValue.ROOTSCREEN_STRING);

        // LABEL
        JLabel labelPanel = new JLabel("Muistipeli");
        labelPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        labelPanel.setFont(new Font("Verdana", Font.BOLD, ConstantValue.TITLE_DEFAULT_SIZE_ROOTSCREEN));

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(50, 50, 10, 10);

        mainPanel.add(labelPanel, constraints);

        // BUTTON
        buttonPanel = new JPanel(new GridLayout(3, 1, 0, 25));
        buttonPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));

        initializeButtons();

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;

        mainPanel.add(buttonPanel, constraints);

    }

    /* Check the changes */
    public void initializeButtons() throws SQLException {
        final boolean hasPakka = database.hasPakka();
        // PLAY BUTTON
        playButton = new JButton(hasPakka ? "Pelaa" : "Pakkoja ei ole luotu");
        playButton.setEnabled(hasPakka);
        playButton.setCursor(hasPakka ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        playButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE));
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setOpaque(true);
        playButton.setBackground(
                hasPakka ? Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR) : Color.LIGHT_GRAY);
        playButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initializeScreens(ConstantValue.PLAYSCREEN_STRING);
            }

        });
        playButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (hasPakka) {
                    playButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                    playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    playButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playButton.setBackground(
                        hasPakka ? Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR) : Color.LIGHT_GRAY);
                playButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        playButton.setPreferredSize(new Dimension(250, 50));

        buttonPanel.add(playButton);
        // DECK BUTTON
        deckButton = new JButton(database.hasPakka() ? "Muokkaa pakkoja" : "Luo pakkoja");
        deckButton.setPreferredSize(new Dimension(250, 50));
        deckButton.setFocusPainted(false);
        deckButton.setBorderPainted(false);
        deckButton.setOpaque(true);
        deckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        deckButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE));
        deckButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initializeScreens(ConstantValue.DECKSCREEN_STRING);
            }

        });

        deckButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                deckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                deckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                deckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        buttonPanel.add(deckButton);
        // HELP BUTTON
        helpButton = new JButton("Pelin ohjeet");
        helpButton.setPreferredSize(new Dimension(250, 50));
        helpButton.setFocusPainted(false);
        helpButton.setBorderPainted(false);
        helpButton.setOpaque(true);
        helpButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        helpButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE));
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initializeScreens(ConstantValue.HELPSCREEN_STRING);
            }

        });

        helpButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                helpButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                helpButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                helpButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        buttonPanel.add(helpButton);
    }

    /* initializeScreens */
    public void startGame(String selectedDeck, int playSize) throws SQLException {
        INGAMESCREEN = new InGameScreen(rootCards, selectedDeck, playSize);
        rootCards.add(INGAMESCREEN, ConstantValue.IN_GAME_SCREEN_STRING);
    }

    public void initializeScreens(String screen) {
        try {
            switch (screen) {
                case ConstantValue.ROOTSCREEN_STRING:
                    rootCardLayout.show(rootCards, screen);
                    buttonPanel.remove(playButton);
                    buttonPanel.remove(deckButton);
                    buttonPanel.remove(helpButton);
                    initializeButtons();
                case ConstantValue.PLAYSCREEN_STRING:
                    PLAYSCREEN = new PlayScreen(rootCards, database, this);
                    rootCards.add(PLAYSCREEN, ConstantValue.PLAYSCREEN_STRING);
                    rootCardLayout.show(rootCards, screen);
                    break;
                case ConstantValue.DECKSCREEN_STRING:
                    DECKSCREEN = new DeckScreen(rootCards, database, this);
                    rootCards.add(DECKSCREEN, ConstantValue.DECKSCREEN_STRING);
                    rootCardLayout.show(rootCards, screen);
                    break;
                case ConstantValue.HELPSCREEN_STRING:
                    HELPSCREEN = new HelpScreen(rootCards);
                    rootCards.add(HELPSCREEN, ConstantValue.HELPSCREEN_STRING);
                    rootCardLayout.show(rootCards, screen);
                    break;
                default:
                    System.err.println("Parametri ei vastannut näkymien nimiä");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}