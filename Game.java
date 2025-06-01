package com.lecroissantrun;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Jore
 * Game.java
 *
 * The “boss” of the game: opens the window, runs the main loop, and
 * checks for win/lose.  It also picks up keyboard input to drive the Player.
 */
public class Game extends Canvas implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private Level level;
    private Player player;
    private int currentLevel;
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    public Game() {
        JFrame frame = new JFrame("Le Croissant Run");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        currentLevel = 1;
        initLevel();
        start();
    }

    private void initLevel() {
        player = new Player(50, 50, 32, 32);
        level = new Level(currentLevel);
        level.setPlayer(player);
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double nsPerFrame = 1_000_000_000.0 / 60.0;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
        stop();
    }

    private void update() {
        player.setDx(0);
        player.setDy(0);
        int speed = 4;
        if (upPressed)    player.setDy(-speed);
        if (downPressed)  player.setDy(speed);
        if (leftPressed)  player.setDx(-speed);
        if (rightPressed) player.setDx(speed);

        level.updateAll();

        if (player.getLives() <= 0) {
            System.out.println("Game Over! You ran out of lives.");
            running = false;
        }

        if (level.isFinished()) {
            if (currentLevel < 5) {
                currentLevel++;
                initLevel();
            } else {
                System.out.println("Congratulations! You completed all levels.");
                running = false;
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        level.renderAll(g);
        g.dispose();
        bs.show();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new Game();
    }
}
