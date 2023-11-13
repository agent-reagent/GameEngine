package com.ava.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {
    // JFrame is a top-level container that represents a window
    private JFrame frame;
    // BufferedImage allows the creation and manipulation of images
    private BufferedImage image;
    // Canvas is a blank area that can be drawn upon
    private Canvas canvas;
    // Graphics object for drawing on the canvas
    private Graphics g;
    // BufferStrategy is used to manage complex memory buffer manipulations for smooth rendering
    private BufferStrategy bs;


    // Constructor for the Window class
    public Window(GameContainer gc) {
        // Create a BufferedImage with the specified width and height
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
//Buffered image is stored in RAM
        // Create a Canvas with dimensions scaled based on GameContainer properties
        canvas = new Canvas();
        Dimension s = new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale()));
        canvas.setPreferredSize(s);
//        canvas.setMaximumSize(s);
//        canvas.setMinimumSize(s);

        // Initialize the JFrame, the main window of the application
        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.PAGE_END);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setResizable(false); // Disable window resizing
        frame.setVisible(true); // Make the window visible

        // Create a double-buffering strategy for the canvas
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics(); // Get the graphics object from the buffer strategy
    }

    // Method to update the window
    public void update() {
        // Draw the image onto the canvas
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);

        // Show the contents of the buffer strategy
        bs.show();
    }
    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
