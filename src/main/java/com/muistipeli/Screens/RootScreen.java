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

    public void showScreen(String screenName) {
        rootCardLayout.show(rootCards, screenName);
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
        labelPanel.setFont(new Font("Arial", Font.BOLD, ConstantValue.TITLE_DEFAULT_SIZE_ROOTSCREEN));

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(50, 50, 10, 10);

        mainPanel.add(labelPanel, constraints);

        // BUTTON
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 25));
        buttonPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        JButton playButton = createButton(database.hasPakka() ? "Pelaa" : "Pakkoja ei ole luotu",
                ConstantValue.PLAYSCREEN_STRING, 50, 200,
                database.hasPakka() ? Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR) : Color.LIGHT_GRAY,
                database.hasPakka() ? null : Color.GRAY, 18);
        playButton.setEnabled(database.hasPakka());
        buttonPanel.add(playButton);
        buttonPanel.add(createButton(database.hasPakka() ? "Muokkaa pakkoja" : "Luo pakkoja",
                ConstantValue.DECKSCREEN_STRING, 50, 200,
                Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR), null, 18));
        buttonPanel.add(createButton("Ohjeet", ConstantValue.HELPSCREEN_STRING, 50, 200,
                Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR), null, 18));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;

        mainPanel.add(buttonPanel, constraints);

        // Muiden paneelien alustus
        PLAYSCREEN = new PlayScreen(rootCards, database);
        DECKSCREEN = new DeckScreen(rootCards, database);
        HELPSCREEN = new HelpScreen(rootCards);
        INGAMESCREEN = new InGameScreen(rootCards, PLAYSCREEN);
        rootCards.add(PLAYSCREEN, ConstantValue.PLAYSCREEN_STRING);
        rootCards.add(DECKSCREEN, ConstantValue.DECKSCREEN_STRING);
        rootCards.add(HELPSCREEN, ConstantValue.HELPSCREEN_STRING);
        rootCards.add(INGAMESCREEN, ConstantValue.IN_GAME_SCREEN_STRING);
    }

    /* COMPONENTS */
    private JButton createButton(final String text, String screen, int height, int width, final Color bgColor,
            Color foreColor,
            int fontSize) throws SQLException {
        final JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
        button.addActionListener(new Switcher(screen));
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
}