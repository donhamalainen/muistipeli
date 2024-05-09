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
    // [0] = sana & [1] = käännös
    private String[] selectedWord = new String[2];

    // GridBagConstraints
    private GridBagConstraints constraints = new GridBagConstraints();
    // Database
    private Database database;
    // Root Attributes
    private JPanel rootCards;
    @SuppressWarnings("unused")
    private CardLayout rootCardLayout;
    private RootScreen rootScreen;
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
    JButton backButton, renameButton, modifyDeckButton, deleteCardButton;

    /******* KONSTRUKTORI *******/
    public DeckScreen(JPanel cards, Database db, RootScreen rootScreen) throws SQLException {
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
        backButton.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        backButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
        backButton.setPreferredSize(
                new Dimension(ConstantValue.BACK_BUTTONS_SIZE_WIDTH, ConstantValue.BACK_BUTTONS_SIZE_HEIGHT));
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);

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
            final JButton deleteDeckButton = new JButton("Poista pakka");
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
            deleteDeckButton.setFocusPainted(false);

            deleteDeckButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    deleteDeckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                    deleteDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    deleteDeckButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                    deleteDeckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            pakkaHeaderPanel.add(deleteDeckButton);

            // ADD
            final JButton addDeckButton = new JButton("Luo pakka");
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
            addDeckButton.setFocusPainted(false);

            addDeckButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    addDeckButton.setBackground(Color.GREEN.darker());
                    addDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    addDeckButton.setBackground(Color.GREEN);
                    addDeckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

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
            modifyDeckButton.setFocusPainted(false);

            modifyDeckButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (modifyDeckButton.isEnabled()) {
                        modifyDeckButton.setBackground(Color.ORANGE.darker());
                        modifyDeckButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (modifyDeckButton.isEnabled()) {
                        modifyDeckButton.setBackground(Color.ORANGE);
                        modifyDeckButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });

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

            // Otsikkopaneeli
            korttiHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            korttiHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            korttiHeaderPanel.setBackground(Color.white);
            // Lisää takaisin-painike
            final JButton scrollBackButton = new JButton("Takaisin pakkoihin");
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
            scrollBackButton.setFocusPainted(false);

            scrollBackButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    scrollBackButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
                    scrollBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    scrollBackButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                    scrollBackButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            korttiHeaderPanel.add(scrollBackButton);

            // luo uusikortit
            final JButton createNewCardButton = new JButton("Luo uusi kortti");

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
            createNewCardButton.setFocusPainted(false);

            createNewCardButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    createNewCardButton.setBackground(Color.GREEN.darker());
                    createNewCardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    createNewCardButton.setBackground(Color.GREEN);
                    createNewCardButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            korttiHeaderPanel.add(createNewCardButton);

            // luo uusikortit
            deleteCardButton = new JButton("Poista kortti");
            deleteCardButton.setEnabled(false);
            deleteCardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteCardButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.BACK_BUTTONS_SIZE_FONT));
            deleteCardButton.setPreferredSize(new Dimension(ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_WIDTH,
                    ConstantValue.SCROL_LIST_HEADER_BUTTONS_SIZE_HEIGHT));
            deleteCardButton.setBorderPainted(false);
            deleteCardButton.setOpaque(true);
            deleteCardButton.setBackground(Color.LIGHT_GRAY);
            deleteCardButton.setFocusPainted(false);

            deleteCardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (selectedWord[0] == null || selectedWord[1] == null) {
                            JOptionPane.showMessageDialog(null, "Valitse poistettava kortti",
                                    "Virhe",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (database.deleteKortti(selectedWord[0], selectedWord[1])) {
                                refreshCardScrollPane(pakkaNimi);
                                deleteCardButton.setBackground(Color.LIGHT_GRAY);
                                deleteCardButton.setEnabled(false);
                            } else {
                                throw new SQLException("Tuntematon virhe poistettaessa korttia.");
                            }
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Kortin poistaminen epäonnistui: " + ex.getMessage(),
                                "Virhe",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();

                    }
                }

            });

            deleteCardButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (deleteCardButton.isEnabled()) {
                        deleteCardButton.setBackground(Color.ORANGE.darker());
                        deleteCardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    ;
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (deleteCardButton.isEnabled()) {
                        deleteCardButton.setBackground(Color.ORANGE);
                        deleteCardButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            });
            korttiHeaderPanel.add(deleteCardButton);

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
                            selectedWord[0] = parts.length > 0 ? parts[0].substring(parts[0].indexOf('.') + 2) : "";
                            selectedWord[1] = parts.length > 1 ? parts[1] : "";
                            popUP(selectedWord[0], selectedWord[1], pakkaNimi);
                        }
                    }
                });

                korttiLista.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        String kortti = korttiLista.getSelectedValue();
                        if (e.getValueIsAdjusting()) {
                            if (kortti != null) {
                                String[] parts = kortti.split(" - ");
                                selectedWord[0] = parts.length > 0 ? parts[0].substring(parts[0].indexOf('.') + 2) : "";
                                selectedWord[1] = parts.length > 1 ? parts[1] : "";
                                System.out.println(selectedWord[0] + " " + selectedWord[1]);
                                deleteCardButton.setEnabled(true);
                                deleteCardButton.setBackground(Color.ORANGE);
                            }
                        }
                    }

                });
            }

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

        final JOptionPane optionPane = new JOptionPane(message,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                new Object[] { okButton, cancelButton },
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
                        String newWord = wordField.getText().toLowerCase();
                        String newTranslate = translationField.getText().toLowerCase();
                        database.addKortti(newWord, newTranslate, pakkaNimi);
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
                "Anna uusi nimi:", newName,
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
                    String newNameTrimmed = newName.getText().toLowerCase();
                    database.setPakkaName(deck, newNameTrimmed);
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
                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);
                dialog.setUndecorated(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setType(Window.Type.POPUP);

                JOptionPane optionPane = new JOptionPane(
                        "Pakka " + pakkaNimi + " poistettu onnistuneesti.",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null, new Object[] {}, null);

                dialog.setContentPane(optionPane);

                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

                Timer timer = new Timer(1200, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                timer.setRepeats(false);
                timer.start();

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
        korttiLista.setEnabled(false);
        kortit = database.getKortit(pakkaNimi);
        korttiModel = new DefaultListModel<>();
        int count = 1;
        if (!kortit.isEmpty()) {
            for (Map.Entry<String, String> entry : kortit.entrySet()) {
                korttiModel.addElement(count + ". " + entry.getKey() + " - " + entry.getValue());
                count++;
            }
            korttiLista.setEnabled(true);
        } else {
            korttiModel.addElement("Kortteja ei ole vielä lisätty tähän pakkaan");
            korttiLista.setEnabled(false);
        }

        if (kortit.isEmpty()) {
            korttiLista.setEnabled(false);
        }

        korttiLista.setModel(korttiModel);

        // Hiiren kuuntelija
        korttiLista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && korttiLista.isEnabled()) {
                    int index = korttiLista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String fullText = korttiLista.getModel()
                                .getElementAt(korttiLista.locationToIndex(e.getPoint()));
                        String[] parts = fullText.split(" - ");
                        selectedWord[0] = parts.length > 0 ? parts[0].substring(parts[0].indexOf('.') + 2) : "";
                        selectedWord[1] = parts.length > 1 ? parts[1] : "";
                        popUP(selectedWord[0], selectedWord[1], pakkaNimi);
                    }
                }
            }
        });

        korttiLista.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                String kortti = korttiLista.getSelectedValue();
                if (e.getValueIsAdjusting() && korttiLista.isEnabled()) {
                    if (kortti != null) {
                        String[] parts = kortti.split(" - ");
                        selectedWord[0] = parts.length > 0 ? parts[0].substring(parts[0].indexOf('.') + 2) : "";
                        selectedWord[1] = parts.length > 1 ? parts[1] : "";
                        System.out.println(selectedWord[0] + " " + selectedWord[1]);
                        deleteCardButton.setEnabled(true);
                        deleteCardButton.setBackground(Color.ORANGE);
                    }
                }
            }

        });

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
