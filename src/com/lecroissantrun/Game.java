package com.lecroissantrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.*;
import java.io.File;




/**
 * The main game class that initializes the game window, handles input,
 * and manages the game loop.
 */
public class Game {

    
    
    private static void startGameWindow() {
    JFrame frame = new JFrame("Le Croissant Run");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);

    GamePanel panel = new GamePanel();
    frame.add(panel);
    frame.setVisible(true);
    panel.requestFocusInWindow();
}

    private static void showMainMenu() {
    JFrame frame = new JFrame("Le Croissant Run - Menu");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);
    frame.setLayout(new GridLayout(4, 1));
    frame.setLocationRelativeTo(null);

    JLabel title = new JLabel("Le Croissant Run", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    frame.add(title);

    JButton playButton = new JButton("Play");
    playButton.addActionListener(e -> {
        frame.dispose();
        startGameWindow();
    });

    JButton aboutButton = new JButton("About");
    aboutButton.addActionListener(e -> {
        JOptionPane.showMessageDialog(frame,
                "Collect croissants, defeat enemies,\nand reach the boulangerie!",
                "About", JOptionPane.INFORMATION_MESSAGE);
    });

    JButton highscoreButton = new JButton("Highscore");
    highscoreButton.addActionListener(e -> {
        int high = ScoreManager.loadHighScore();
        JOptionPane.showMessageDialog(frame,
                "Highest Score: " + high,
                "Highscore", JOptionPane.INFORMATION_MESSAGE);
    });

    frame.add(playButton);
    frame.add(aboutButton);
    frame.add(highscoreButton);
    frame.setVisible(true);
}

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showMainMenu();
        });

        
    }

    static class GamePanel extends JPanel implements Runnable {
        Player player;
        Level level;
        boolean[] keys = new boolean[4]; 
        private Image skyImage = new ImageIcon("assets/sky.png").getImage();
        private Image grassImage = new ImageIcon("assets/grass.png").getImage();
        private Image startImage = new ImageIcon("assets/start.png").getImage();
        private Image endImage = new ImageIcon("assets/boulangerie.png").getImage();
        private Clip musicClip;
        private int currentLevel = 1;
        private int cameraX = 0;
        private int cameraY = 0;
        private int highScore = ScoreManager.loadHighScore();


        private void updateCamera() {
            // Horizontal camera movement (X)
            cameraX = player.getX() - getWidth() / 2;
            if (cameraX < 0) cameraX = 0;
            int maxCamX = level.getWidth() - getWidth();
            if (cameraX > maxCamX) cameraX = maxCamX;

            // Vertical camera movement (Y)
            cameraY = player.getY() - getHeight() / 2;
            if (cameraY < 0) cameraY = 0;
            int maxCamY = level.getHeight() - getHeight();
            if (cameraY > maxCamY) cameraY = maxCamY;
        }


        private void playMusic(String filepath){
            try{
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepath));
                musicClip = AudioSystem.getClip();
                musicClip.open(audioInput);
                FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f); // Reduce volume (−80 to 6 dB)
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                musicClip.start();
            }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error playing music: " + e.getMessage());
            }
        }

        private Thread gameThread;
        private boolean running;

        public GamePanel() {

            player = new Player(60, 430);
            level = new Level(currentLevel);

            setFocusable(true);
            setBackground(Color.BLACK);
            playMusic("assets/bgm.wav");
            
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
                        case KeyEvent.VK_SPACE:
                             player.jump(); // Jump on space key
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
            
            player.move(keys, level.getObstacles(), level.getWidth());
            level.update(player);
            
            // Check win condition
            Rectangle goal = new Rectangle(level.getWidth() - 150, 430, 60, 60);

            if (player.getBounds().intersects(goal)) {
                victory();
            }

            updateCamera();
        }

        private void gameOver() {
            running = false;
            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showOptionDialog(this,
                "Game Over! Score: " + player.getScore() + "\nDo you want to restart from Level 1 or quit?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Quit" },
                "Restart");

            if (choice == 0) {
                currentLevel = 1;  // Reset to level 1
                restartGame();
            } else {
                if (player.getScore() > highScore) {
                highScore = player.getScore();
                ScoreManager.saveHighScore(highScore);
               }

                System.exit(0);
            }

            });
        }

        private void victory() {
            running = false;

            if (currentLevel >= 5) {
                JOptionPane.showMessageDialog(this, "You completed all levels!\nFinal Score: " + player.getScore());

                int choice = JOptionPane.showOptionDialog(this,
                        "Do you want to restart from Level 1 or quit?",
                        "Game Complete!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Restart", "Quit"},
                        "Restart");

                if (choice == 0) {
                    currentLevel = 1;
                    restartGame();
                } else {
                    System.exit(0);
                }

            } else {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Victory! You reached the boulangerie!\nScore: " + player.getScore() + "\nProceed to next level?",
                        "Level Complete!",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    currentLevel++;
                    restartGame();
                } else {
                    if (player.getScore() > highScore) {
                    highScore = player.getScore();
                    ScoreManager.saveHighScore(highScore);
                    }
      

                    System.exit(0);
                }
            }
        }


        private void restartGame() {
            int currentScore = player.getScore();   // Save current score
            int currentLives = player.getLives();   // Save current lives

            player = new Player(60, 430);           // Create new player at starting position
            player.addScore(currentScore);          // Restore score

            while (player.getLives() < currentLives) {
                player.addLife();                   // Restore lives (if > default)
            }

            level = new Level(currentLevel);        // Load current level

            for (int i = 0; i < keys.length; i++) {
                keys[i] = false;                    // Reset key states
            }

            startGame();                            // Start the game thread again
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Enable antialiasing for smoother graphics
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            //sky
            for (int x = 0; x < getWidth(); x += skyImage.getWidth(null)) {
                g.drawImage(skyImage, x, 0, null);
}

            // grass
            for (int x = 0; x < getWidth(); x += grassImage.getWidth(null)) {
                 g.drawImage(grassImage, x, getHeight() - grassImage.getHeight(null), null);
}
            // Starting area
            int startX = 40;
            g.drawImage(startImage, startX - cameraX, 380 - cameraY, 80,  80, null);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Start", startX - cameraX, 375 - cameraY);

            // Boulangerie goal
            int endX = level.getWidth() - 150;
            g.drawImage(endImage, endX - cameraX, 380 - cameraY, 120, 100, null);
            g.setColor(Color.BLACK);
            g.drawString("Boulangerie", endX - cameraX, 375 - cameraY);

    

            level.render(g, cameraX, cameraY);
            player.render(g, cameraX, cameraY);


            // UI
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + player.getScore(), 10, 25);
            g.drawString("Lives: " + player.getLives(), 10, 45);
            g.drawString("Health: " + player.getHealth() + "/" + player.getMaxHealth(), 10, 65);
            g.drawString("Level:" + currentLevel,10 , 90);
            
            // Instructions
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Controls: A/D to move, W to attack", 10, getHeight() - 10);
            
            // Additional game stats
            g.drawString("Croissants: " + level.getRemainingCroissants(), 10, getHeight() - 30);
        }
    }
}