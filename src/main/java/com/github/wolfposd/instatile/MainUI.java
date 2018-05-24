/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2018
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.wolfposd.instatile;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class MainUI {

    private static final Dimension DIM400_400 = new Dimension(400, 400);
    JFrame frame;
    JTextField pathInput;
    JButton imageSelectButton;
    JButton applyButton;
    JTextField rowsField;
    JTextField columnField;
    ImagePanel imagePanel;
    JComboBox<ImageHandle> chooser;

    public enum ImageHandle {
        Stretch, Crop, Fill;
    }

    public MainUI() {

        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Insta Tile");
        frame.setLayout(new MigLayout());

        imagePanel = new ImagePanel();
        imagePanel.setPreferredSize(DIM400_400);
        imagePanel.setMaximumSize(DIM400_400);
        imagePanel.setMinimumSize(DIM400_400);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        JPanel right = new JPanel(new MigLayout());

        frame.add(imagePanel, "dock center");
        frame.add(right, "dock east");

        imageSelectButton = new JButton("Select");

        applyButton = new JButton("Split Image");

        pathInput = new JTextField(20);
        rowsField = new JTextField("3", 5);
        columnField = new JTextField("3", 5);

        right.add(imageSelectButton, "wrap");
        right.add(pathInput, "span, grow, wrap 30");

        right.add(new JLabel("rows:"), "");
        right.add(rowsField, "span, grow, wrap");

        right.add(new JLabel("column:"), "");
        right.add(columnField, "span, grow, wrap 30");

        right.add(new JLabel("Image handling:"), "span, wrap");
        chooser = new JComboBox<ImageHandle>(ImageHandle.values());
        right.add(chooser, "span,grow, wrap 30");

        right.add(applyButton, "wrap");

        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void show(boolean show) {
        frame.setVisible(show);
    }
}
