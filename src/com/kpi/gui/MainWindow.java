package com.kpi.gui;

import com.kpi.HopfieldNN;
import com.kpi.Letter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainWindow extends JFrame implements ActionListener, UpdateLetterCallback {
    private static final int WINDOW_WIDTH  = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int PREDICT_PANEL_HEIGHT = 300;
    private static final int LETTER_SIZE = 150;
    private static final int LIBRARY_LETTER_SIZE = 100;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;

    private static final int LIBRARY_SIZE = 3;

    private final ImagePanel predictPanel;
    private final JPanel addPanel;

    private Vector<Letter> letterLibrary;
    private Letter inputLetter;
    private Letter outputLetter;
    private HopfieldNN hopfieldNN;

    public MainWindow() {
        letterLibrary = new Vector<>();
        for (int i = 0; i < LIBRARY_SIZE; i++)
            letterLibrary.add(new Letter());

        inputLetter = new Letter();
        outputLetter = new Letter();
        hopfieldNN = new HopfieldNN();

        setTitle("Letter recognition. Hopfield Network");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        predictPanel = new ImagePanel();
        predictPanel.setImage(new ImageIcon("src/com/kpi/gui/sources/background.png").getImage());
        predictPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, PREDICT_PANEL_HEIGHT));
        predictPanel.setBorder(BorderFactory.createLineBorder(new Color(152, 75, 108), 5));
        showPredictPanelContent();
        add(predictPanel);

        addPanel = new JPanel();
        addPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT - PREDICT_PANEL_HEIGHT));
        addPanel.setBackground(new Color(233, 151, 176));
        showAddPanelContent();
        add(addPanel);
    }

    private void showPredictPanelContent() {
        JButton predButton = new JButton("Recognize");
        predButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        predButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        predButton.setFont(predButton.getFont().deriveFont(20.0f));
        predButton.setBackground(Color.WHITE);
        predButton.addActionListener(this);
        predButton.setActionCommand("recognize letter");

        predictPanel.add(predButton);

        ImagePanel arrowImg = new ImagePanel();
        arrowImg.setImage(new ImageIcon("src/com/kpi/gui/sources/arrow.png").getImage());
        arrowImg.setOpaque(false);
        arrowImg.setPreferredSize(new Dimension(100, 100));

        JPanel lettersPanel = new JPanel();
        lettersPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, LETTER_SIZE));
        lettersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        lettersPanel.add(letterPanel(inputLetter, LETTER_SIZE));
        lettersPanel.add(arrowImg);
        lettersPanel.add(letterPanel(outputLetter, LETTER_SIZE));
        lettersPanel.setOpaque(false);

        predictPanel.add(lettersPanel);
    }

    private void showAddPanelContent() {
        JButton addButton = new JButton("Add letter");
        addButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        addButton.setFont(addButton.getFont().deriveFont(20.0f));
        addButton.setBackground(Color.WHITE);
        addButton.addActionListener(this);
        addButton.setActionCommand("add letter");

        addPanel.add(addButton);

        JPanel lettersInLibrary = new JPanel();
        lettersInLibrary.setPreferredSize(new Dimension(WINDOW_WIDTH, LIBRARY_LETTER_SIZE));
        lettersInLibrary.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));
        for (Letter letter : letterLibrary)
            lettersInLibrary.add(letterPanel(letter, LIBRARY_LETTER_SIZE));
        lettersInLibrary.setOpaque(false);

        addPanel.add(lettersInLibrary);
    }

    private JPanel letterPanel(Letter letter, int letterSize) {
        JPanel letterPanel = new JPanel(new GridLayout(letter.getRowsNum(), letter.getColsNum(), 3, 3));
        letterPanel.setPreferredSize(new Dimension(letterSize, letterSize));
        letterPanel.setBorder(BorderFactory.createLineBorder(new Color(152, 75, 108), 3));

        Vector<JLabel> tiles = new Vector<>();
        for (Float i : letter.getLetter()) {
            JLabel tile = new JLabel();
            tile.setBackground(i == 1.0f ? new Color(93, 25, 45) : Color.WHITE);
            tile.setOpaque(true);
            tiles.add(tile);

            letterPanel.add(tile);
        }

        return letterPanel;
    }

    private Letter getLastEmpty() {
        for(Letter letter : letterLibrary)
            if (letter.isEmpty())
                return letter;

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("recognize letter")) {
            DrawWindow drawWindow = new DrawWindow(this, true, inputLetter);

        } else if (e.getActionCommand().equals("add letter")) {
            DrawWindow drawWindow = new DrawWindow(this, false, getLastEmpty());
        }
    }

    @Override
    public void updatePredictPanelCallback() {
        predictPanel.removeAll();
        predictPanel.revalidate();
        predictPanel.repaint();

        try {
            hopfieldNN.train();
            outputLetter = hopfieldNN.predict(inputLetter);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null,
                    "Unfortunately, your letter was not recognized. ((",
                    "Recognition error",
                    JOptionPane.WARNING_MESSAGE);

            outputLetter = new Letter();
        }

        showPredictPanelContent();
    }

    @Override
    public void updateAddPanelCallback(Letter updatedLetter) {
        addPanel.removeAll();
        addPanel.revalidate();
        addPanel.repaint();

        hopfieldNN.addLetter(updatedLetter);

        showAddPanelContent();
    }
}
