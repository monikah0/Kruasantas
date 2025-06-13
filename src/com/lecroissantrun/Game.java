package com.lecroissantrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The main game class that initializes the game window, handles input,
 * and manages the game loop.
 */
public class Game {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Le Croissant Run");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // Center the window

            GamePanel panel = new GamePanel();
            frame.add(panel);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }

    static class GamePanel extends JPanel implements Runnable {
        Player player;
        Level level;
        boolean[] keys = new boolean[4]; 

        private Thread gameThread;
        private boolean running;

        public GamePanel() {
            player = new Player(60, 430);
            level = new Level();

            setFocusable(true);
            setBackground(Color.BLACK);
            
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            keys[0] = true;
                            attackEnemies(); // Attack on key press
                            break;
                        case KeyEvent.VK_S:
                            keys[1] = true;
                            break;
                        case KeyEvent.VK_A:
                            keys[2] = true;
                            break;
                        case KeyEvent.VK_D:
                            keys[3] = true;
                            break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            keys[0] = false;
                            break;
                        case KeyEvent.VK_S:
                            keys[1] = false;
                            break;
                        case KeyEvent.VK_A:
                            keys[2] = false;
                            break;
                        case KeyEvent.VK_D:
                            keys[3] = false;
                            break;
                    }
                }

                private void attackEnemies() {
                    Rectangle attackRange = new Rectangle(player.getX() - 20, player.getY() - 20, 
                                                        player.width + 40, player.height + 40);
                    for (Enemy e : level.getEnemies()) {
                        if (!e.isDead() && e.getBounds().intersects(attackRange)) {
                            e.takeDamage();
                            player.attack();
                            if (e.isDead()) {
                                player.addScore(50); // Bonus for defeating enemy
                            }
                            break; // Only attack one enemy at a time
                        }
                    }
                }
            });
            
            startGame();
        }

        private void startGame() {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public void run() {
            long lastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;
            
            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    updateGame();
                    delta--;
                }
                
                repaint();
                
                try {
                    Thread.sleep(16); // Roughly 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        public void updateGame() {
            if (player.getLives() <= 0) {
                gameOver();
                return;
            }
            
            player.move(keys);
            level.update(player);
            
            // Check win condition
            Rectangle goal = new Rectangle(700, 430, 60, 60);
            if (player.getBounds().intersects(goal)) {
                victory();
            }
        }

        private void gameOver() {
            running = false;
            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showConfirmDialog(this, 
                    "Game Over! Score: " + player.getScore() + "\nPlay again?", 
                    "Game Over", 
                    JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    restartGame();
                } else {
                    System.exit(0);
                }
            });
        }

        private void victory() {
            running = false;
            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showConfirmDialog(this, 
                    "Victory! You reached the boulangerie!\nScore: " + player.getScore() + "\nPlay again?", 
                    "Victory!", 
                    JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    restartGame();
                } else {
                    System.exit(0);
                }
            });
        }

        private void restartGame() {
            player = new Player(60, 430);
            level = new Level();
            startGame();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Enable antialiasing for smoother graphics
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Sky background
            g.setColor(new Color(135, 206, 235));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Grass
            g.setColor(new Color(34, 139, 34));
            g.fillRect(0, getHeight() - 100, getWidth(), 100);

            // Starting area
            g.setColor(new Color(173, 216, 230));
            g.fillRect(40, 410, 40, 50);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Start", 45, 405);

            // Boulangerie goal
            g.setColor(new Color(255, 204, 102));
            g.fillRect(700, 430, 60, 50);
            g.setColor(Color.BLACK);
            g.drawString("Boulangerie", 705, 425);

            // Render game objects
            level.render(g);
            player.render(g);

            // UI
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + player.getScore(), 10, 25);
            g.drawString("Lives: " + player.getLives(), 10, 45);
            g.drawString("Health: " + player.getHealth() + "/" + player.getMaxHealth(), 10, 65);
            
            // Instructions
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Controls: A/D to move, W to attack", 10, getHeight() - 10);
            
            // Additional game stats
            g.drawString("Croissants: " + level.getRemainingCroissants(), 10, getHeight() - 30);
        }
    }
}