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

import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.wolfposd.instatile.MainUI.ImageHandle;

public class MainController {

    private MainUI ui;

    private BufferedImage originalImage;
    private BufferedImage modifiedImage;

    private int rows = 3;
    private int cols = 3;

    public MainController() {
        ui = new MainUI();

        ui.applyButton.addActionListener(e -> applyTileButton());
        ui.imageSelectButton.addActionListener(e -> selectImageButton());

        ui.rowsField.addKeyListener(new KeyListenerLamba(ev -> rowOrCellsChanged()));
        ui.columnField.addKeyListener(new KeyListenerLamba(ev -> rowOrCellsChanged()));

        ui.chooser.addActionListener(e -> imageHandleSelectionChanged());

        ui.show(true);
    }

    private void imageHandleSelectionChanged() {
        if (originalImage != null)
            modifyImage();
    }

    private void loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            originalImage = image;
            modifiedImage = image;
            modifyImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifyImage() {
        switch ((ImageHandle) ui.chooser.getSelectedItem()) {
            case Stretch :
                stretchImage();
                break;
            case Crop :
                cropImage();
                break;
            case Fill :
                fillImage();
                break;
        }
        System.out.println("RESULT h:" + modifiedImage.getHeight() + " w:" + modifiedImage.getWidth());
        ui.imagePanel.setImage(modifiedImage);
        ui.imagePanel.repaint();
    }

    private void stretchImage() {
        System.out.println("stretchImage");

        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        int dimension = Math.max(w, h);

        BufferedImage bufferedImage = new BufferedImage(dimension, dimension, originalImage.getType());

        Graphics2D gr = bufferedImage.createGraphics();
        gr.drawImage(originalImage, 0, 0, dimension, dimension, null);
        gr.dispose();

        modifiedImage = bufferedImage;
    }

    private void cropImage() {
        System.out.println("cropImage");

        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        int dimension = Math.min(w, h);

        if (w < h) {
            int diff2 = Math.abs(h - w) / 2;
            modifiedImage = originalImage.getSubimage(0, diff2, dimension, dimension);
        } else if (w > h) {
            int diff2 = Math.abs(h - w) / 2;
            modifiedImage = originalImage.getSubimage(diff2, 0, dimension, dimension);
        } else {
            modifiedImage = originalImage;
        }
    }
    private void fillImage() {
        System.out.println("fillImage");

        int w = originalImage.getWidth();
        int h = originalImage.getHeight();

        int dimension = Math.max(w, h);
        BufferedImage bufferedImage = new BufferedImage(dimension, dimension, originalImage.getType());
        Graphics2D gr = bufferedImage.createGraphics();
        if (w < h) {
            int diff = Math.abs(h - w) / 2;
            gr.drawImage(originalImage, diff, 0, dimension - diff, dimension, 0, 0, w, h, null);

        } else if (w > h) {
            int diff = Math.abs(h - w) / 2;
            gr.drawImage(originalImage, 0, diff, dimension, dimension - diff, 0, 0, w, h, null);
        } else {
            gr.drawImage(originalImage, 0, 0, null);
        }
        gr.dispose();

        modifiedImage = bufferedImage;
    }

    public void rowOrCellsChanged() {

        String rowsString = ui.rowsField.getText();
        String colsString = ui.columnField.getText();

        if (!rowsString.isEmpty() && !colsString.isEmpty()) {
            try {
                this.rows = Integer.parseInt(rowsString);
                this.cols = Integer.parseInt(colsString);
                ui.imagePanel.setGrid(rows, cols);
                ui.imagePanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void selectImageButton() {

        FileDialog dialog = new FileDialog(ui.frame);
        dialog.setTitle("Select Image");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);

        String directory = dialog.getDirectory();
        String file = dialog.getFile();

        if (directory != null && file != null) {
            String fullpath = directory + file;
            ui.pathInput.setText(fullpath);
            loadImage(fullpath);
        }
    }

    public void applyTileButton() {

        final int h = modifiedImage.getHeight();
        final int w = modifiedImage.getWidth();
        final int chunks = rows * cols;

        int chunkWidth = w / cols; // determines the chunk width and height
        int chunkHeight = h / rows;

        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, modifiedImage.getType());

                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(modifiedImage, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x
                        + chunkHeight, null);
                gr.dispose();
            }
        }
        String path = ui.pathInput.getText();
        File file = new File(path);
        int lastIndexOf = file.getName().lastIndexOf(".");
        String fileExtension = file.getName().substring(lastIndexOf + 1);

        String name = file.getName().substring(0, lastIndexOf);
        System.out.println(name);

        for (int i = 0; i < imgs.length; i++) {
            int outputindex = imgs.length - 1 - i;
            try {
                File output = new File(file.getParentFile(), name + "-" + outputindex + "." + fileExtension);
                System.out.println(output);
                ImageIO.write(imgs[i], fileExtension, output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
