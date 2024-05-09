package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.muistipeli.ConstantValue;
import com.muistipeli.Database;

public class PlayScreen extends JPanel {

    /******* ATTRIBUUTIT *******/
    // Attribuutit
    private String valittuPakka;
    private RootScreen rootScreen;
    // GridBagConstraints
    private GridBagConstraints constraints = new GridBagConstraints();
    // Database
    private Database database;
    // Root Attributes
    private JPanel rootCards;
    private CardLayout rootCardLayout;
    // Scroll Attributes
    private JScrollPane scrollPane;
    private DefaultListModel<String> model;
    // Pakat
    private HashMap<String, String> pakat = new HashMap<>();
    private JList<String> pakkaLista;
    // Painikkeet
    JButton playPanel, backButton, modifyButton;

    /******* KONSTRUKTORI *******/
    public PlayScreen(JPanel cards, Database db, RootScreen rootScreen) throws SQLException {
        this.rootCards = cards;
        rootCardLayout = (CardLayout) rootCards.getLayout();
        this.database = db;
        this.rootScreen = rootScreen;
        initializePlayScreen();
        run();
    }

    /******* ALUSTUS *******/
    private void initializePlayScreen() throws SQLException {
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());

        // Haetaan pakat listaan
        try {
            if (database.hasPakka()) {
                pakat = database.getAllPakka();
            }
        } catch (Exception e) {
            System.err.println("Virhe 'PlayScreen:ssä' pakkoja haettaessa " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Virhe pakkojen haussa: " + e.getMessage(), "Virhe",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /******* ALUSTUS *******/
    private void run() {
        // TAKAISIN PAINIKE
        backButton = new JButton("Päävalikko");
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rootScreen.initializeScreens(ConstantValue.ROOTSCREEN_STRING);
            }

        });
        backButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        backButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));

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
        constraints.weighty = 0;
        constraints.insets = new Insets(50, 50, 10, 10);

        add(backButton, constraints);

        // OTSIKKO
        JLabel selectDeckLabel = new JLabel("Valitse pelattava pakka");
        selectDeckLabel.setFont(new Font("Aries", Font.BOLD, 20));

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(-50, 25, 0, 25);

        add(selectDeckLabel, constraints);

        // MUOKKAA PAINIKE
        modifyButton = new JButton("Muokkaa pakkoja");
        modifyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rootScreen.initializeScreens(ConstantValue.DECKSCREEN_STRING);
            }

        });
        modifyButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        modifyButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH + 50, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        modifyButton.setBorderPainted(false);
        modifyButton.setOpaque(true);
        modifyButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        modifyButton.setFocusPainted(false);

        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                modifyButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                modifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                modifyButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                modifyButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.weighty = 0;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 50, 50, 10);

        add(modifyButton, constraints);

        // PELAA PAINIKE
        playPanel = new JButton("Aloita peli");
        playPanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (checkDeckValidity(valittuPakka)) {
                        int korttiListaCount = database.getKortit(valittuPakka).size();
                        while (true) {
                            String input = JOptionPane.showInputDialog(
                                    null,
                                    "Kuinka monta korttia haluat pelata pakasta? (enintään "
                                            + database.getKortit(valittuPakka).size() + ")",
                                    "Valitse korttien määrä",
                                    JOptionPane.QUESTION_MESSAGE);
                            if (input == null) {
                                break;
                            }
                            try {
                                int inputValue = Integer.parseInt(input);
                                if (inputValue > 0 && inputValue <= korttiListaCount) {
                                    // Onnistunut
                                    rootScreen.startGame(valittuPakka, inputValue);
                                    rootCardLayout.show(rootCards, ConstantValue.IN_GAME_SCREEN_STRING);
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Virheellinen määrä. Anna arvo 1 - " + korttiListaCount + " välillä.",
                                            "Virhe",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Syötä kelvollinen numero", "Virhe",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    } else {
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "Tässä pakassa ei ole pelattavia kortteja. Haluatko luoda kortteja pakkaan?",
                                "Virhe",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE);

                        if (response == JOptionPane.YES_OPTION) {
                            rootScreen.initializeScreens(ConstantValue.DECKSCREEN_STRING);
                        } else if (response == JOptionPane.NO_OPTION) {
                            JOptionPane.getRootFrame().dispose();
                        }
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Virhe pelin aloittamisessa", "Virhe",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        playPanel.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.DEFAULT_PLAY_BUTTON_SIZE));
        playPanel.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        playPanel.setBorderPainted(false);
        playPanel.setOpaque(true);
        playPanel.setFocusPainted(false);

        playPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (playPanel.isEnabled()) {
                    playPanel.setBackground(Color.GREEN.darker());
                    playPanel.setForeground(Color.BLACK);
                    playPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (playPanel.isEnabled()) {
                    playPanel.setBackground(Color.LIGHT_GRAY);
                    playPanel.setForeground(Color.GRAY);
                    playPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        constraints.weighty = 0;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 10, 50, 50);

        add(playPanel, constraints);

        pakkojenListaus();
    }

    /******* PAKKA ALUSTUS *******/
    private void pakkojenListaus() {
        try {
            model = new DefaultListModel<>();
            int count = 1;
            if (!pakat.isEmpty()) {
                for (Map.Entry<String, String> pakka : pakat.entrySet()) {
                    model.addElement(count + ". " + (String) pakka.getValue());
                    count++;
                }
            } else {
                model.addElement("Et ole vielä luonut pelattavia pakkoja");
            }

            pakkaLista = new JList<>(model);
            pakkaLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pakkaLista.setFont(new Font("Verdana", Font.ITALIC, 16));
            pakkaLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            scrollPane = new JScrollPane(pakkaLista);
            scrollPane.setPreferredSize(new Dimension(300, 400));

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(10, 50, 10, 50);

            add(scrollPane, constraints);

            pakanValinta();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******* PAKAN VALINTA *******/
    private void pakanValinta() {
        playPanel.setEnabled(false);
        playPanel.setBackground(Color.LIGHT_GRAY);
        playPanel.setForeground(Color.GRAY);

        pakkaLista.addListSelectionListener(new ListSelectionListener() {
            /** Pakan valinta **/
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    String pakkaNimi = pakkaLista.getSelectedValue();
                    if (pakkaNimi != null) {
                        valittuPakka = pakkaNimi.substring(pakkaNimi.indexOf('.') + 2);
                        playPanel.setEnabled(true);
                        playPanel.setBackground(Color.GREEN);
                        playPanel.setForeground(Color.BLACK);
                    }
                }
            }
        });
    }

    public String getValittuPakka() {
        return this.valittuPakka;
    }

    /******* PAKAN TARKISTUS *******/
    public boolean checkDeckValidity(String deckName) {
        try {
            if (!database.getKortit(deckName).isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
