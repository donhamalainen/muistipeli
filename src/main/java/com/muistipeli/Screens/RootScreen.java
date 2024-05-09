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
    private JPanel rootCards;
    private CardLayout rootCardLayout;

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
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 25));
        buttonPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        JButton playButton = createButton(database.hasPakka() ? "Pelaa" : "Pakkoja ei ole luotu",
                ConstantValue.PLAYSCREEN_STRING, 50, 250,
                database.hasPakka() ? Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR) : Color.LIGHT_GRAY,
                database.hasPakka() ? null : Color.GRAY, ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE, this);
        playButton.setEnabled(database.hasPakka());
        buttonPanel.add(playButton);
        buttonPanel.add(createButton(database.hasPakka() ? "Muokkaa pakkoja" : "Luo pakkoja",
                ConstantValue.DECKSCREEN_STRING, 50, 250,
                Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR), null,
                ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE, this));
        buttonPanel.add(createButton("Ohjeet", ConstantValue.HELPSCREEN_STRING, 50, 250,
                Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR), null,
                ConstantValue.NAVIGATIONS_BUTTONS_FONT_SIZE, this));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;

        mainPanel.add(buttonPanel, constraints);

    }

    /* COMPONENTS */
    private JButton createButton(final String text, final String screen, int height, int width, final Color bgColor,
            Color foreColor,
            int fontSize, final RootScreen rootScreen) throws SQLException {
        final JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.PLAIN, fontSize));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initializeScreens(screen);
            }

        });
        button.setPreferredSize(new Dimension(width, height));

        if (bgColor != null) {
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.setBackground(bgColor);
        }
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                try {
                    if (text.equals("Pakkoja ei ole luotu") && !database.hasPakka()) {
                        // button.setBackground(bgColor.darker());
                    } else {
                        button.setBackground(bgColor.darker());
                    }
                } catch (SQLException sqlMouseE) {
                    sqlMouseE.printStackTrace();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.setBackground(bgColor);
            }
        });

        return button;
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