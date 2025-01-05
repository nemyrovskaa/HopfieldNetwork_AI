package com.kpi.gui;

import com.kpi.Letter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class DrawWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH  = 300;
    private static final int WINDOW_HEIGHT = 400;
    private static final int OK_PANEL_HEIGHT = 50;

    private JPanel drawPanel;
    private JPanel okPanel;

    private Vector<MyButton> tiles;

    private UpdateLetterCallback updateLetterCallback;
    private boolean toPredict;
    private Letter letterToFill;

    public DrawWindow(UpdateLetterCallback updateLetterCallback, boolean toPredict, Letter letterToFill) {
        this.updateLetterCallback = updateLetterCallback;
        this.toPredict = toPredict;
        this.letterToFill = letterToFill;

        setTitle("Letter drawing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        createDrawPanel();
        add(drawPanel);

        createOkPanel();
        add(okPanel);
    }

    private void createDrawPanel() {
        if (letterToFill == null)
            return;

        drawPanel = new JPanel(new GridLayout(letterToFill.getRowsNum(), letterToFill.getColsNum(), 0, 0));
        drawPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT - OK_PANEL_HEIGHT));
        drawPanel.setBorder(BorderFactory.createLineBorder(new Color(152, 75, 108), 3));

        tiles = new Vector<>();
        for (int i = 0; i < 5*5; i++) {
            MyButton tile = new MyButton(Color.WHITE, new Color(93, 25, 45));
            tile.setBorder(BorderFactory.createLineBorder(new Color(152, 75, 108)));
            tiles.add(tile);

            drawPanel.add(tile);
        }
    }

    private void createOkPanel() {
        okPanel = new JPanel();
        okPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, OK_PANEL_HEIGHT));
        okPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        okPanel.setBackground(new Color(233, 151, 176));

        int buttonWidth = 200;
        int buttonHeight = 40;

        JButton button = new JButton(toPredict ? "Recognize" : "To the library");
        button.setFont(button.getFont().deriveFont(20.0f));
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        button.addActionListener(this);
        button.setActionCommand(toPredict ? "predict" : "add");

        okPanel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("predict")) {
            ArrayList<Float> letter = new ArrayList<>();
            for (MyButton tile : tiles)
                letter.add(tile.isClicked() ? 1.0f : -1.0f);

            letterToFill.setLetter(letter);
            updateLetterCallback.updatePredictPanelCallback();

        } else if (e.getActionCommand().equals("add")){
            if (letterToFill == null){
                JOptionPane.showMessageDialog(null,
                        "Unfortunately, the library is already full. ((",
                        "Library overflow",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                ArrayList<Float> letter = new ArrayList<>();
                for (MyButton tile : tiles)
                    letter.add(tile.isClicked() ? 1.0f : -1.0f);

                letterToFill.setLetter(letter);
                updateLetterCallback.updateAddPanelCallback(letterToFill);
            }
        }
        this.dispose();
    }
}
