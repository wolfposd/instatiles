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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Image image;

    private int rows = 3;
    private int cols = 3;

    public ImagePanel() {

    }

    public ImagePanel(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImage(ImageIcon image) {
        this.image = image.getImage();
    }

    public void setGrid(int row, int col) {
        this.rows = row;
        this.cols = col;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (image != null) {
            g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
        }

        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        int w3 = w / rows;
        int h3 = h / cols;

        if (g instanceof Graphics2D) {
            float[] dash1 = {5f};
            ((Graphics2D) g).setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, dash1, 0f));
        }
        g.setColor(Color.red);

        for (int i = 1; i < rows; i++) {
            g.drawLine(w3 * i, 0, w3 * i, h);
        }

        for (int i = 1; i < cols; i++) {
            g.drawLine(0, h3 * i, w, h3 * i);

        }
    }

}
