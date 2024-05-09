package com.muistipeli.Screens;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import com.muistipeli.ConstantValue;
import com.muistipeli.Game;

public class InGameScreen extends JPanel {

    /******* ATTRIBUUTIT *******/
    // Others
    private Game game;
    // Attributes
    private int currentCardIndex = 0;
    private int playSize = 0;
    // GridBagConstraints
    private GridBagConstraints constraints = new GridBagConstraints();
    // Root Attributes
    private JPanel rootCards;
    private CardLayout rootCardLayout;
    // Paneelit
    private JPanel leftPanel, rightPanel, statisticPanel, answerPanel;
    // Kortit
    private HashMap<String, String> kortit = new HashMap<>();
    private java.util.List<String> sanaLista;
    private java.util.List<String> kaannosLista;
    // Labels
    JLabel rightAnswer, wrongAnswer, leftQuestions, wordLabel;
    // Painikkeet
    private JButton enterAnswerButton;

    /******* KONSTRUKTORI *******/
    public InGameScreen(JPanel cards, String deckName, int playSize) throws SQLException {
        this.rootCards = cards;
        this.rootCardLayout = (CardLayout) rootCards.getLayout();
        this.playSize = playSize;
        game = new Game(deckName, playSize);
        kortit = game.getRandomCards(playSize);
        sanaLista = new ArrayList<>(kortit.keySet());
        kaannosLista = new ArrayList<>(kortit.values());
        initializeInGameScreen();
        run();
    }

    /******* ALUSTUS *******/
    private void initializeInGameScreen() throws SQLException {
        setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));
        setLayout(new GridBagLayout());
    }

    /******* ALUSTUS *******/
    private void run() {
        // Vasen paneeli
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.fill = GridBagConstraints.VERTICAL;
        add(leftPanel, constraints);

        // Oikea paneeli
        rightPanel = new JPanel(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        add(rightPanel, constraints);

        // Määritellään paneelien sisältö
        statisticUI();
        answerUI();
    }

    /******* PELI *******/

    private void statisticUI() {
        // Paneeli
        statisticPanel = new JPanel(new GridBagLayout());
        statisticPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statisticPanel.setBackground(Color.decode(ConstantValue.STATISTIC_BACKGROUND_COLOR));

        // JLabel
        rightAnswer = new JLabel("Oikein: " + game.getOikea(), JLabel.LEFT);
        wrongAnswer = new JLabel("Väärin: " + game.getVaara(), JLabel.LEFT);
        leftQuestions = new JLabel("Kortteja jäljellä: " + game.getJaljella(), JLabel.LEFT);

        rightAnswer.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.ANSWER_LABEL_FONT_SIZE));
        wrongAnswer.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.ANSWER_LABEL_FONT_SIZE));
        leftQuestions.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.ANSWER_LABEL_FONT_SIZE));

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 10, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridy = 0;
        statisticPanel.add(rightAnswer, constraints);
        constraints.gridy = 1;
        statisticPanel.add(wrongAnswer, constraints);
        constraints.gridy = 2;
        statisticPanel.add(leftQuestions, constraints);

        final JButton endGameButton = new JButton("Lopeta peli");
        endGameButton.setFont(new Font("Verdana", Font.PLAIN, ConstantValue.IN_GAME_SCREEN_END_BUTTON_SIZE));
        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] responses = { "Kyllä", "Ei" };
                int answer = JOptionPane.showOptionDialog(null,
                        "Haluatko varmasti lopettaa pelin?",
                        "Lopeta peli",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, responses, responses[1]);
                if (answer == JOptionPane.YES_OPTION) {
                    game.endGame();
                    rootCardLayout.show(rootCards, ConstantValue.PLAYSCREEN_STRING);
                } else if (answer == JOptionPane.NO_OPTION) {
                    JOptionPane.getRootFrame().dispose();
                }
            }
        });

        endGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        endGameButton.setPreferredSize(new Dimension(getWidth(), 40));
        endGameButton.setFocusPainted(false);
        endGameButton.setBackground(Color.decode("#ffcccc"));
        endGameButton.setBorderPainted(false);
        endGameButton.setOpaque(true);
        endGameButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));

        endGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                endGameButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                endGameButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
            }
        });

        constraints.gridy = 3;
        constraints.insets = new Insets(20, 10, 10, 0);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;

        statisticPanel.add(endGameButton, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1;
        constraints.weighty = 1;

        // Lisäys
        leftPanel.add(statisticPanel, constraints);

    }

    private void answerUI() {
        // Paneeli
        answerPanel = new JPanel(new GridBagLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 150, 20));
        answerPanel.setBackground(Color.decode(ConstantValue.BACKGROUND_COLOR));

        wordLabel = new JLabel(sanaLista.get(currentCardIndex), JLabel.CENTER);
        final JTextField answerField = new JTextField(25);
        answerField.setPreferredSize(new Dimension(getWidth(), 50));
        answerField.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(0, 20, 0, 20)));

        enterAnswerButton = new JButton("Vastaus");
        enterAnswerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        enterAnswerButton.setPreferredSize(new Dimension(getWidth(), 50));
        enterAnswerButton.setBorderPainted(false);
        enterAnswerButton.setOpaque(true);
        enterAnswerButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
        enterAnswerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enterAnswerButton.setFocusPainted(false);

        enterAnswerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.checkAnswer(answerField.getText(), sanaLista.get(currentCardIndex))) {
                    answerField.setText("");
                    rightAnswer.setText("Oikein: " + game.getOikea());
                    leftQuestions.setText("Kortteja jäljellä: " + game.getJaljella());

                    currentCardIndex++;
                    if (currentCardIndex < sanaLista.size()) {
                        wordLabel.setText(sanaLista.get(currentCardIndex));
                    } else {
                        finish();
                    }

                } else {
                    showAnswer();
                    answerField.setText("");
                    wrongAnswer.setText("Väärin: " + game.getVaara());
                    leftQuestions.setText("Kortteja jäljellä: " + game.getJaljella());

                    currentCardIndex++;
                    if (currentCardIndex < sanaLista.size()) {
                        wordLabel.setText(sanaLista.get(currentCardIndex));
                    } else {
                        finish();
                    }
                }
                answerField.requestFocusInWindow();
            }

        });
        enterAnswerButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                enterAnswerButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR).darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                enterAnswerButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
            }
        });

        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterAnswerButton.doClick();
                }
            }
        });

        wordLabel.setFont(new Font("Verdana", Font.BOLD, 34));
        answerField.setFont(new Font("Verdana", Font.PLAIN, 14));
        enterAnswerButton.setFont(new Font("Verdana", Font.PLAIN, 14));

        // Lisätään "Kortin sana" -kenttä
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        answerPanel.add(wordLabel, constraints);

        // Lisätään vastauskenttä
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 20, 0);
        answerPanel.add(answerField, constraints);

        // Lisätään vastauspainike
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        answerPanel.add(enterAnswerButton, constraints);

        // Lopetus lisäys
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;

        rightPanel.add(answerPanel, constraints);

    }

    private void showAnswer() {
        enterAnswerButton.setEnabled(false);
        enterAnswerButton.setBackground(Color.LIGHT_GRAY);
        enterAnswerButton.setForeground(Color.GRAY);
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setType(Window.Type.POPUP);

        JOptionPane optionPane = new JOptionPane(
                "Vastaus on: " + kaannosLista.get(currentCardIndex),
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null, new Object[] {}, null);

        dialog.setContentPane(optionPane);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        Timer timer = new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterAnswerButton.setEnabled(true);
                enterAnswerButton.setBackground(Color.decode(ConstantValue.BUTTONS_BACKGROUND_COLOR));
                enterAnswerButton.setForeground(Color.BLACK);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void finish() {
        String[] responses = { "Jatka peliä", "Aloita alusta", "Lopeta peli" };
        int answer = JOptionPane.showOptionDialog(null,
                "Olet pelannut " + game.getCountPlayedCards() + "/" + game.totalDeckSize()
                        + ". Valitse toiminto seuraavista vaihtoehdoista",
                "Pelin lopetus",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                responses,
                responses[0]);
        switch (answer) {
            case JOptionPane.YES_OPTION:
                if (game.realDeckSize() > 0) {
                    while (true) {
                        String input = JOptionPane.showInputDialog(
                                null,
                                "Kuinka monta korttia haluat pelata pakasta? (enintään " + game.realDeckSize() + ")",
                                "Valitse korttien määrä",
                                JOptionPane.QUESTION_MESSAGE);
                        if (input == null) {
                            rootCardLayout.show(rootCards, ConstantValue.PLAYSCREEN_STRING);
                            break;
                        }

                        try {
                            int inputValue = Integer.parseInt(input);
                            if (inputValue > 0 && inputValue <= game.realDeckSize()) {
                                kortit = game.getRandomCards(inputValue);
                                game.resetJaljella(inputValue);
                                sanaLista = new ArrayList<>(kortit.keySet());
                                kaannosLista = new ArrayList<>(kortit.values());

                                if (!sanaLista.isEmpty()) {
                                    wordLabel.setText(sanaLista.get(0));
                                    currentCardIndex = 0;
                                    leftQuestions = new JLabel("Kortteja jäljellä: " + game.getJaljella(), JLabel.LEFT);
                                }
                                break;
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Virheellinen määrä. Anna arvo 1 - " + game.totalDeckSize() + " välillä.",
                                        "Virhe",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    int answerYESOption = JOptionPane.showOptionDialog(null,
                            "Olet pelannut koko pakan. Haluatko aloittaa uudelleen",
                            "Pelin lopetus",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[] { "Aloita alusta", "Lopeta peli" },
                            null);

                    switch (answerYESOption) {
                        case JOptionPane.YES_OPTION:
                            try {
                                game.startGame();

                                while (true) {
                                    String input = JOptionPane.showInputDialog(
                                            null,
                                            "Kuinka monta korttia haluat pelata pakasta? (enintään "
                                                    + game.realDeckSize()
                                                    + ")",
                                            "Valitse korttien määrä",
                                            JOptionPane.QUESTION_MESSAGE);
                                    if (input == null) {
                                        rootCardLayout.show(rootCards, ConstantValue.PLAYSCREEN_STRING);
                                        break;
                                    }

                                    try {
                                        int inputValue = Integer.parseInt(input);
                                        if (inputValue > 0 && inputValue <= game.realDeckSize()) {

                                            kortit = game.getRandomCards(inputValue);
                                            sanaLista = new ArrayList<>(kortit.keySet());
                                            kaannosLista = new ArrayList<>(kortit.values());

                                            game.resetJaljella(inputValue);
                                            if (!sanaLista.isEmpty()) {
                                                wordLabel.setText(sanaLista.get(0));
                                                currentCardIndex = 0;
                                                leftQuestions = new JLabel("Kortteja jäljellä: " + game.getJaljella(),
                                                        JLabel.LEFT);
                                            }
                                            break;
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Virheellinen määrä. Anna arvo 1 - " + game.totalDeckSize()
                                                            + " välillä.",
                                                    "Virhe",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        case JOptionPane.NO_OPTION:
                            game.endGame();
                            rootCardLayout.show(rootCards, ConstantValue.PLAYSCREEN_STRING);
                            break;
                    }
                }
                break;
            case JOptionPane.CANCEL_OPTION:
                JOptionPane.showMessageDialog(null, "Kiitos pelaamisesta!");
                rootCardLayout.show(rootCards, ConstantValue.PLAYSCREEN_STRING);
                break;
            case JOptionPane.NO_OPTION:
                // game.reset();
                kortit = game.getRandomCards(playSize);

                sanaLista = new ArrayList<>(kortit.keySet());
                kaannosLista = new ArrayList<>(kortit.values());

                if (!sanaLista.isEmpty()) {
                    wordLabel.setText(sanaLista.get(0));
                    currentCardIndex = 0;
                    leftQuestions = new JLabel("Kortteja jäljellä: " + game.getJaljella(), JLabel.LEFT);
                }
                break;
        }
    }
}
