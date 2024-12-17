package org.akashdev;

import org.akashdev.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTING
    final int originalTileSize = 16; // 16 x 16 tiles
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //FPS
    int FPS = 60;

    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this,KeyH);

    // Set Player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

/*
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 0.01666 Seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            // 1. UPDATE: update information such as character positions
            update();

            // 2. DRAW: draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
*/

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 0.01666 Seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {

                // 1. UPDATE: update information such as character positions
                update();

                // 2. DRAW: draw the screen with the updated information
                repaint();

                delta--;
                drawCount++;
            }

            if(timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }



        }
    }
    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose();
    }
}
