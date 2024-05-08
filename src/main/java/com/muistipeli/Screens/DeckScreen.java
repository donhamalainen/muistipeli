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

public class DeckScreen extends JPanel {

    /******* ATTRIBUUTIT *******/
    // Attributes
    private String selectedDeck = null;
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
    private DefaultListModel<String> korttiModel;
    // Pakat
    private HashMap<String, String> pakat = new HashMap<>();
    private HashMap<String, String> kortit = new HashMap<>();
    private JList<String> pakkaLista;
    private JList<String> korttiLista;
    // Painikkeet
    JPanel pakkaHeaderPanel, korttiHeaderPanel;
    JLabel infoLabel;
    JButton backButton, renameButton, modifyDeckButton;

    /******* KONSTRUKTORI *******/
    public DeckScreen(JPanel cards, Database db) throws SQLException {
        this.rootCards = cards;
        rootCardLayout = (CardLayout) rootCards.getLayout();
        this.database = db;
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
        backButton.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        backButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
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
        JLabel selectDeckLabel = new JLabel("Valitse muokattava pakka");
        selectDeckLabel.setFont(new Font("Aries", Font.BOLD, 20));

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(50, 25, 0, 25);

        add(selectDeckLabel, constraints);

        // PELAA PAINIKE
        infoLabel = new JLabel("Tuplaklikkaa valitsemaasi pakkaa siirtyäksesi korttinäkymään");

        infoLabel.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.DEFAULT_PLAY_BUTTON_SIZE));
        infoLabel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        infoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        constraints.weighty = 0;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 10, 50, 50);

        add(infoLabel, constraints);

        pakkojenListaus();
    }

    /********* SHOW THE DECKS *********/
    private void pakkojenListaus() {
        try {
            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(10, 50, 10, 50);

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
            if (pakat.isEmpty()) {
                pakkaLista.setEnabled(false);
            }
            pakkaLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pakkaLista.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pakkaLista.setFont(new Font("Verdana", Font.ITALIC, 16));
            pakkaLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            /******* BUTTONS *******/
            pakkaHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pakkaHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pakkaHeaderPanel.setBackground(Color.white);

            // DELETE
            JButton deleteDeckButton = new JButton("Poista pakka");
            deleteDeckButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedDeck != null) {
                        deleteDeck(selectedDeck);
                    } else {
                        JOptionPane.showMessageDialog(null, "Valitse poistettava pakka",
                                "Virhe",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            });
            deleteDeckButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            deleteDeckButton.setBorderPainted(false);
            deleteDeckButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            deleteDeckButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteDeckButton.setOpaque(true);
            deleteDeckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
            deleteDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteDeckButton.setFocusPainted(false);
            pakkaHeaderPanel.add(deleteDeckButton);

            // ADD
            JButton addDeckButton = new JButton("Luo pakka");
            addDeckButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    createDeck();
                }

            });
            addDeckButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            addDeckButton.setBorderPainted(false);
            addDeckButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            addDeckButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            addDeckButton.setOpaque(true);
            addDeckButton.setBackground(Color.GREEN);
            addDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addDeckButton.setFocusPainted(false);
            pakkaHeaderPanel.add(addDeckButton);

            // MODIFY
            modifyDeckButton = new JButton("Vaihda nimeä");
            modifyDeckButton.setEnabled(false);
            modifyDeckButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    modifyDeck(selectedDeck);
                }

            });
            modifyDeckButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            modifyDeckButton.setBorderPainted(false);
            modifyDeckButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            modifyDeckButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            modifyDeckButton.setOpaque(true);
            modifyDeckButton.setBackground(Color.LIGHT_GRAY);
            modifyDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            modifyDeckButton.setFocusPainted(false);
            pakkaHeaderPanel.add(modifyDeckButton);

            /******** ACTION LISTENERS ********/

            // Hiiren kuuntelija
            pakkaLista.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && pakkaLista.isEnabled()) {
                        int index = pakkaLista.locationToIndex(e.getPoint());
                        if (index >= 0) {
                            String pakka = model.getElementAt(index)
                                    .substring(model.getElementAt(index).indexOf('.') + 2);
                            try {
                                naytaKortit(pakka);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Virhe kortteja haettaessa: " + ex.getMessage(),
                                        "Virhe", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });

            // Valinta kuuntelija
            pakkaLista.addListSelectionListener(new ListSelectionListener() {
                /** Pakan valinta **/
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String pakkaNimi = pakkaLista.getSelectedValue();
                    if (e.getValueIsAdjusting()) {
                        if (pakkaNimi != null) {
                            selectedDeck = pakkaNimi.substring(pakkaNimi.indexOf('.') + 2);
                            modifyDeckButton.setEnabled(true);
                            modifyDeckButton.setBackground(Color.ORANGE);
                        }
                    }
                }
            });

            scrollPane = new JScrollPane(pakkaLista);
            scrollPane.setViewportView(pakkaLista);
            scrollPane.setColumnHeaderView(pakkaHeaderPanel);

            add(scrollPane, constraints);
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    /******** SHOW THE CARDS ********/
    private void naytaKortit(final String pakkaNimi) {
        try {
            kortit = database.getKortit(pakkaNimi);
            korttiModel = new DefaultListModel<>();
            int count = 1;
            if (!kortit.isEmpty()) {
                for (Map.Entry<String, String> entry : kortit.entrySet()) {
                    korttiModel.addElement(count + ". " + entry.getKey() + " - " + entry.getValue());
                    count++;
                }
            } else {
                korttiModel.addElement("Kortteja ei ole vielä lisätty tähän pakkaan");
            }

            korttiLista = new JList<>(korttiModel);
            korttiLista.setModel(korttiModel);
            korttiLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            korttiLista.setCursor(new Cursor(Cursor.HAND_CURSOR));
            korttiLista.setFont(new Font("Verdana", Font.ITALIC, 16));
            korttiLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane.setViewportView(korttiLista);

            if (kortit.isEmpty()) {
                korttiLista.setEnabled(false);
            } else {
                korttiLista.setEnabled(true);
                korttiLista.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            String fullText = korttiLista.getModel()
                                    .getElementAt(korttiLista.locationToIndex(e.getPoint()));
                            String[] parts = fullText.split(" - ");
                            String word = parts.length > 0 ? parts[0].substring(parts[0].indexOf('.') + 2) : "";
                            String translation = parts.length > 1 ? parts[1] : "";
                            popUP(word, translation, pakkaNimi);
                        }
                    }
                });
            }

            // Otsikkopaneeli
            korttiHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            korttiHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            korttiHeaderPanel.setBackground(Color.white);
            // Lisää takaisin-painike
            JButton scrollBackButton = new JButton("Takaisin pakkoihin");
            scrollBackButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    scrollPane.setViewportView(pakkaLista);
                    scrollPane.setColumnHeaderView(pakkaHeaderPanel);
                }

            });
            scrollBackButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            scrollBackButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            scrollBackButton.setBorderPainted(false);
            scrollBackButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            scrollBackButton.setOpaque(true);
            scrollBackButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
            scrollBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            scrollBackButton.setFocusPainted(false);
            korttiHeaderPanel.add(scrollBackButton);

            // luo uusikortit
            JButton createNewCardButton = new JButton("Luo uusi kortti");

            createNewCardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addCard(pakkaNimi);
                }

            });

            createNewCardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            createNewCardButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            createNewCardButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            createNewCardButton.setBorderPainted(false);
            createNewCardButton.setOpaque(true);
            createNewCardButton.setBackground(Color.GREEN);
            createNewCardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            createNewCardButton.setFocusPainted(false);
            korttiHeaderPanel.add(createNewCardButton);

            // luo uusikortit
            JButton deleteCardButton = new JButton("Poista kortti");

            deleteCardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }

            });

            deleteCardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteCardButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            deleteCardButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            deleteCardButton.setBorderPainted(false);
            deleteCardButton.setOpaque(true);
            deleteCardButton.setBackground(Color.orange);
            deleteCardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteCardButton.setFocusPainted(false);
            korttiHeaderPanel.add(deleteCardButton);

            scrollPane.setColumnHeaderView(korttiHeaderPanel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Virhe kortteja haettaessa: " + ex.getMessage(), "Virhe",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /******** POPUP NOTIFICATION ********/
    private void popUP(final String oldWord, final String oldTranslation, final String pakkaNimi) {
        final JTextField wordField = new JTextField(oldWord);
        final JTextField translationField = new JTextField(oldTranslation);
        Object[] message = {
                "Sana:", wordField,
                "Käännös:", translationField,
                "Pakka:", pakkaNimi,
        };

        JButton okButton = new JButton("Vahvista");
        JButton cancelButton = new JButton("Peruuta");
        JButton deleteButton = new JButton("Poista");

        final JOptionPane optionPane = new JOptionPane(message,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                new Object[] { okButton, cancelButton, deleteButton },
                okButton);

        final JDialog dialog = optionPane.createDialog(this, "Muokkaa korttia");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newWord = wordField.getText();
                String newTranslate = translationField.getText();
                updateCard(oldWord, oldTranslation, newWord.toLowerCase(),
                        newTranslate.toLowerCase(),
                        pakkaNimi);
                dialog.dispose();
            }

        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (database.deleteKortti(oldWord, oldTranslation)) {
                        refreshCardScrollPane(pakkaNimi);
                        dialog.dispose();
                    } else {
                        throw new SQLException("Tuntematon virhe poistettaessa korttia.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Kortin poistaminen epäonnistui: " + ex.getMessage(), "Virhe",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();

                }
            }

        });

        dialog.setVisible(true);

    }

    /******** CARDS ********/
    private void addCard(final String pakkaNimi) {
        final JTextField wordField = new JTextField();
        final JTextField translationField = new JTextField();

        Object[] fields = {
                "Sana:", wordField,
                "Käännös", translationField,
                "Pakka", pakkaNimi
        };

        JButton okButton = new JButton("Valmis");
        JButton cancelButton = new JButton("Peruuta");

        final JOptionPane optionPane = new JOptionPane(fields,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                new Object[] { okButton, cancelButton },
                okButton);

        final JDialog dialog = optionPane.createDialog(this, "Luo kortti");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String word = wordField.getText().trim();
                    String translation = translationField.getText().trim();
                    if (word.isEmpty() || translation.isEmpty()) {
                        System.err.println("Kortin uusia tietoja ei ole täytetty");
                        JOptionPane.showMessageDialog(dialog,
                                "Täytä kortin tiedot [sana & käännös] lisätäksesi uusi kortti",
                                "Virhe",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        database.addKortti(wordField.getText(), translationField.getText(), pakkaNimi);
                        refreshCardScrollPane(pakkaNimi);
                        dialog.dispose();
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(dialog,
                            "Tietokantavirhe: Korttia ei voitu lisätä. " + e1.getMessage(),
                            "Virhe",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        });

        dialog.setVisible(true);
    }

    private void updateCard(String oldWord, String oldTranslation, String newWord, String newTranslation,
            String pakkaNimi) {
        try {
            database.setKortti(oldWord, newWord, newTranslation);
            refreshCardScrollPane(pakkaNimi);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Virhe päivitettäessä korttia tietokannassa: " + e.getMessage(),
                    "Virhe", JOptionPane.ERROR_MESSAGE);
        }
    }

    /******** DECKS ********/
    private void modifyDeck(final String deck) {
        final JTextField newName = new JTextField(deck);
        Object[] fields = {
                "nimi:", newName,
        };

        JButton okButton = new JButton("Vahvista");
        JButton cancelButton = new JButton("Peruuta");

        final JOptionPane optionPane = new JOptionPane(fields,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                new Object[] { okButton, cancelButton },
                okButton);

        final JDialog dialog = optionPane.createDialog(this, "Muokkaa pakan nimeä");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    database.setPakkaName(deck, newName.getText());
                    refreshDeckScrollPane();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                dialog.dispose();
            }

        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        });

        dialog.setVisible(true);

    }

    private void createDeck() {
        final JTextField newDeck = new JTextField("");
        Object[] fields = {
                "Anna pakalle nimi:", newDeck,
        };

        JButton okButton = new JButton("Vahvista");
        JButton cancelButton = new JButton("Peruuta");

        final JOptionPane optionPane = new JOptionPane(fields,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                new Object[] { okButton, cancelButton },
                okButton);

        final JDialog dialog = optionPane.createDialog(this, "Anna pakalle nimi");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deckName = newDeck.getText().trim();
                try {
                    if (!deckName.isEmpty()) {
                        database.addPakka(deckName.toLowerCase());
                        refreshDeckScrollPane();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Täytä pakan tiedot [nimi] lisätäksesi luodaksesi uusi pakka",
                                "Virhe",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(dialog,
                            "Tietokantavirhe: Pakkaa ei voitu lisätä. " + e1.getMessage(),
                            "Virhe",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        });

        dialog.setVisible(true);

    }

    private void deleteDeck(String pakkaNimi) {
        try {
            if (database.deletePakka(pakkaNimi)) {
                refreshDeckScrollPane();
                modifyDeckButton.setEnabled(false);
                modifyDeckButton.setBackground(Color.LIGHT_GRAY);
                JOptionPane.showMessageDialog(null, "Pakka " + pakkaNimi + " poistettu onnistuneesti.",
                        "Pakan poisto",
                        JOptionPane.INFORMATION_MESSAGE);
                selectedDeck = null;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Pakan poistossa epäonnistui",
                        "Epäonnistui",
                        JOptionPane.ERROR_MESSAGE);
                selectedDeck = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Pakan poistossa tapahtui virhe " + e.getMessage(),
                    "Virhe",
                    JOptionPane.ERROR_MESSAGE);
            selectedDeck = null;
        }

    }

    /******** REFRESH ********/
    private void refreshCardScrollPane(final String pakkaNimi) throws SQLException {
        kortit = database.getKortit(pakkaNimi);
        korttiModel = new DefaultListModel<>();
        int count = 1;
        if (!kortit.isEmpty()) {
            for (Map.Entry<String, String> entry : kortit.entrySet()) {
                korttiModel.addElement(count + ". " + entry.getKey() + " - " + entry.getValue());
                count++;
            }
        } else {
            korttiModel.addElement("Kortteja ei ole vielä lisätty tähän pakkaan");
        }

        if (kortit.isEmpty()) {
            korttiLista.setEnabled(false);
        }

        korttiLista.setModel(korttiModel);
        scrollPane.setViewportView(korttiLista);
    }

    private void refreshDeckScrollPane() throws SQLException {
        pakkaLista.setEnabled(false);
        final HashMap<String, String> pakat = database.getAllPakka();
        model = new DefaultListModel<>();
        int count = 1;
        if (!pakat.isEmpty()) {
            for (Map.Entry<String, String> pakka : pakat.entrySet()) {
                model.addElement(count + ". " + pakka.getValue());
                count++;
            }
            pakkaLista.setEnabled(true);
        } else {
            model.addElement("Et ole vielä luonut pelattavia pakkoja");
            pakkaLista.setEnabled(false);
        }

        pakkaLista.setModel(model);

        // Hiiren kuuntelija
        pakkaLista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && pakkaLista.isEnabled()) {
                    int index = pakkaLista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String pakka = model.getElementAt(index)
                                .substring(model.getElementAt(index).indexOf('.') + 2);
                        try {
                            naytaKortit(pakka);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Virhe kortteja haettaessa: " + ex.getMessage(),
                                    "Virhe", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        scrollPane.setViewportView(pakkaLista);
    }

}
