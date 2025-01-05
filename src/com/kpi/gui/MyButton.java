package com.kpi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyButton extends JButton implements ActionListener {
    private final Color pressedBackgroundColor;
    private final Color defaultBackgroundColor;

    private boolean isClicked;

    public MyButton(Color defaultBackgroundColor, Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        this.defaultBackgroundColor = defaultBackgroundColor;
        this.isClicked = false;

        setBackground(defaultBackgroundColor);
        addActionListener(this);
    }

    public boolean isClicked() {
        return isClicked;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getBackground() == defaultBackgroundColor) {
            setBackground(pressedBackgroundColor);
            isClicked = true;

        } else {
            setBackground(defaultBackgroundColor);
            isClicked = false;
        }
    }
}
