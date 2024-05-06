package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import com.muistipeli.ConstantValue;
import com.muistipeli.Game;

public class InGameScreen extends JPanel implements ActionListener {

    /******* ATTRIBUUTIT *******/
    private GridBagConstraints c;

    /******* UI KOMPONENTIT *******/
    private JLabel oikea, vaara, jaljella, sana;
    private JTextField vastausKentta;
    private JPanel infoPanel;
    private JButton lopeta;
    private Game game;
    private JPanel rootCards;
    private CardLayout rootCardLayout;

    /*******
     * KONSTRUKTORI
     * 
     * @throws SQLException
     *******/
    public InGameScreen(JPanel cards) throws SQLException {
        this.rootCards = cards;
        rootCardLayout = (CardLayout) rootCards.getLayout();
        game = new Game("suomi");
        c = new GridBagConstraints();
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());
        setVisible(true);
        addInfoPanel();
        addGameComponents();

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

    // Vasemman reunan infopaneelin lisääminen
    private void addInfoPanel() {
        infoPanel = new JPanel(new GridBagLayout());
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

        lopeta = new JButton("Lopeta peli");
        lopeta.addActionListener(this);
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

    // Lisää vastauskentän ja kysytyn sanan
    private void addGameComponents() {

        // Luodaan paneeli johon lisätään pelikomponentit
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        gamePanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));

        // GBC ALUSTUS
        c = new GridBagConstraints();
        sana = new JLabel(game.getRandomWord());
        sana.setFont(new Font("Arial", Font.BOLD, 24));
        sana.setPreferredSize(new Dimension(200, 100));
        sana.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        vastausKentta = new JTextField("Syötä vastaus");
        vastausKentta.setPreferredSize(new Dimension(200, 30));
        vastausKentta.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        vastausKentta.addFocusListener(new FocusListener() {

            // Otetaan "Syötä vastaus" teksti pois jos vastauskenttä on valittuna
            @Override
            public void focusGained(FocusEvent e) {
                if (vastausKentta.getText().equals("Syötä vastaus")) {
                    vastausKentta.setText("");
                }
            }

            // Jos tekstikenttä ei ole valittuna "Syötä vastaus" laitetaan näkyviin
            @Override
            public void focusLost(FocusEvent e) {
                if (vastausKentta.getText().isEmpty()) {
                    vastausKentta.setText("Syötä vastaus");
                }
            }

        });

        vastausKentta.addActionListener(this);
        gamePanel.add(sana, c);
        // Asetetaan vastauskenttä kysytyn sanan alapuolelle
        c.gridy = 1;
        c.insets = new Insets(50, 0, 0, 0);
        gamePanel.add(vastausKentta, c);

        // Vastauksen syöttö näppäin
        JButton button = new JButton("->");
        button.setPreferredSize(new Dimension(50, 30));
        button.setFocusable(false);
        c.gridx = 1;
        gamePanel.add(button, c);
        // Asetetaan paneelin sijainniksi keskikohta
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;

        // Lisätään pelikomponentti paneeli pääpaneeliin
        this.add(gamePanel, c);
    }

    public void refreshUI() {
        oikea.setText("Oikein: " + game.getOikea());
        vaara.setText("Väärin:" + game.getVaara());
        jaljella.setText("Kortteja jäljellä: " + game.getJaljella() + "/" + "15");
        sana.setText(game.getRandomWord());
        vastausKentta.setText("");
    }

    public void endingScreen() {
        String[] responses = { "Palaa päävalikkoon", "Pelaa uudestaan" };
        int answer = JOptionPane.showOptionDialog(null,
                "Olet pelannut kaikki kortit, haluatko palata päävalikkoon?",
                "Peli lopetus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                responses,
                null);

        if (answer == 0){
            rootCardLayout.show(rootCards, ConstantValue.ROOTSCREEN_STRING);
        }

        if (answer == 1){
            try{
                game = new Game("suomi");
                refreshUI();
            }catch (SQLException e){
                System.out.println("SQL ERROR: ");
                e.printStackTrace();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lopeta) {
            String[] responses = { "Kyllä", "Ei" };
            int answer = JOptionPane.showOptionDialog(null,
                    "Haluatko varmasti lopettaa pelin?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, responses, null);
            if (answer == 0) {
                rootCardLayout.show(rootCards, ConstantValue.ROOTSCREEN_STRING);
            }
        } else if (e.getSource() == vastausKentta) {
            game.checkAnswer(vastausKentta.getText());
            refreshUI();
            if (game.endGame() == true) {
                endingScreen();
            }
        }
    }
}
