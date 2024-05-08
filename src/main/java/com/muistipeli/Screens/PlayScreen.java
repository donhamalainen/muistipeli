package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.muistipeli.ConstantValue;
import com.muistipeli.Database;
import com.muistipeli.Game;

public class PlayScreen extends JPanel {

    /******* ATTRIBUUTIT *******/
    // Attribuutit
    private String valittuPakka;
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

    //Peli
    Game game;

    /******* KONSTRUKTORI *******/
    public PlayScreen(JPanel cards, Database db) throws SQLException {
        this.rootCards = cards;
        rootCardLayout = (CardLayout) rootCards.getLayout();
        this.database = db;
        initializePlayScreen();
        run();
    }

    //PELIRUUTU
/*      public PlayScreen() throws SQLException{
        
        game = new Game(getValittuPakka());
        GridBagConstraints c = new GridBagConstraints();
        c = new GridBagConstraints();
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());
        setVisible(true);
        addInfoPanel();
    } 
    
        // Vasemman reunan infopaneelin lisääminen
    private void addInfoPanel() {
        
        GridBagConstraints c = new GridBagConstraints();
        JLabel oikea, vaara, jaljella;
       JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        // GBC ALUSTUS
        c = new GridBagConstraints();

        // LABELEITTEN ALUSTUS JA LISÄÄMINEN INFOPANEELIIN
        oikea = new JLabel("Oikein: " + game.getOikea());
        vaara = new JLabel("Väärin: " + game.getVaara());
        jaljella = new JLabel("Kortteja jäljellä: " + game.getJaljella());

        c.gridy = 0;
        infoPanel.add(oikea, c);
        // Oikea labelin alapuolelle

        c.gridy = 1;
        infoPanel.add(vaara, c);
        // Väärin labelin alapuolelle
        c.gridy = 2;
        infoPanel.add(jaljella, c);
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // POISTU NÄPPÄIMEN LISÄÄMINEN PANEELIIN

        JButton lopeta = new JButton("Lopeta peli");
        
        lopeta.setFont(new Font("Arial", Font.PLAIN, ConstantValue.DEFAULT_PLAY_BUTTON_SIZE));
        lopeta.setFocusable(false);
        lopeta.setPreferredSize(
                new Dimension(200, 40));
        lopeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Infopanelin pohjalle
        c.gridy = 3;
        c.insets = new Insets(150, 30, 0, 30);

        infoPanel.add(lopeta, c);

        // Asetetaan infopanel game screen paneelin vasempaan reunaan
        // Alustetaan GBC
        c = new GridBagConstraints();

        // Ankkuroidaan vasempaan reunaan

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;

        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.WEST;
        this.add(infoPanel, c);
    }
 */

    /******* ALUSTUS *******/
    private void initializePlayScreen() throws SQLException {
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());

        // Haetaan pakat listaan
        try {
            if (database.hasPakka()) {
                pakat = database.getAllPakka();
            } else {
                pakat = null;
            }
        } catch (Exception e) {
            System.err.println("Virhe 'PlayScreen:ssä' pakkoja haettaessa " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Virhe pakkojen haussa: " + e.getMessage(), "Virhe",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        // TAKAISIN PAINIKE
        backButton = new JButton("Päävalikko");
        backButton.addActionListener(new Switcher(ConstantValue.ROOTSCREEN_STRING));
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
        modifyButton.addActionListener(new Switcher(ConstantValue.DECKSCREEN_STRING));
        modifyButton.setFont(new Font("Arial", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        modifyButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH + 50, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        modifyButton.setBorderPainted(false);
        modifyButton.setOpaque(true);
        modifyButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        modifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modifyButton.setFocusPainted(false);

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
                
                rootCardLayout.show(rootCards, ConstantValue.IN_GAME_SCREEN_STRING);
/*                 try {
                    Game game = new Game(getValittuPakka());
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } */
            }
            
        });
        playPanel.setFont(new Font("Arial", Font.PLAIN, ConstantValue.DEFAULT_PLAY_BUTTON_SIZE));
        playPanel.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        playPanel.setBorderPainted(false);
        playPanel.setOpaque(true);
        playPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playPanel.setFocusPainted(false);

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
            if (!pakat.isEmpty()) {
                for (Map.Entry<String, String> pakka : pakat.entrySet()) {
                    model.addElement(pakka.getKey() + ". " + (String) pakka.getValue());
                }
            } else {
                model.addElement("Et ole vielä luonut pelattavia pakkoja");
            }

            pakkaLista = new JList<>(model);
            pakkaLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pakkaLista.setFont(new Font("Arial", Font.ITALIC, 16));
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
                        System.out.println(valittuPakka);
                        playPanel.setEnabled(true);
                        playPanel.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                        playPanel.setForeground(Color.BLACK);
                    }
                }
            }
        });
    }

    public String getValittuPakka() {
        return this.valittuPakka;
    }
}
