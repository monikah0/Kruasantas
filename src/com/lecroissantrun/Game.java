package com.lecroissantrun;

import javax.swing.*;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//Jore
//Game.java
/**
 * The main game class that initializes the game window, handles input,
 * and manages the game loop.
 */
public class Game {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Le Croissant Run");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setVisible(true);
        panel.requestFocusInWindow();

    }

    static class GamePanel extends JPanel implements Runnable{
        Player player;
        Level level;
        boolean[] keys = new boolean[4]; 


        private Thread gameThread;
        private boolean running;

        public GamePanel(){
            player = new Player(60,420);
            level = new Level();

            setFocusable(true);
            requestFocusInWindow(); // Ensure the panel can receive key events

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // Handle key presses for player movement
                    switch (e.getKeyCode()) {
                    
                    case KeyEvent.VK_A:
                        keys[2] = true;
                        break;
                    case KeyEvent.VK_D:
                        keys[3] = true;
                        break;
                    
                }
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        attackEnemies(); // Attack enemies when w is pressed
                    }


                }

                public void keyReleased(KeyEvent e) {
                    // Handle key releases for player movement
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

                private void attackEnemies(){
                    Rectangle attackRange = new Rectangle(player.x - 10, player.y - 50, player.width + 20, player.height + 50);
                    for (Enemy e : level.getEnemies()){
                        if(!e.isDead() && e.getBounds().intersects(attackRange)){
                            e.takeDamage();
                            break;
                        }    
                       player.attack();
                    }
                }
            });
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void run(){
            long lastTime = System.nanoTime();
            double amountofTicks = 60.0;
            double ns = 1000000000 / amountofTicks;
            double delta = 0;
            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    updateGame();
                    repaint(); 
                    delta--;
                }
                
                try {
                    Thread.sleep(2); // Sleep to limit frame rate
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void updateGame(){
            player.move(keys);
            level.update(player);
            Rectangle goal = new Rectangle(700, 430, 60, 60);

            if (player.getBounds().intersects(goal)) {
                System.out.println("You reached the boulangerie!");
                running = false; // stop game loop
                JOptionPane.showMessageDialog(this, "Victory! You reached the boulangerie!");
                System.exit(0); // exit game (optional)
            }
            

        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            //sky
            g.setColor(new Color(135, 206, 235)); // Sky blue
            g.fillRect(0, 0, getWidth(), getHeight()); // Fill background

            //grass
            g.setColor(new Color(34, 139, 34)); // Forest green
            g.fillRect(0, getHeight() - 100, getWidth(), 100); // Fill grass area

            // Starting area
            g.setColor(new Color(173, 216, 230)); // light blue
            g.fillRect(40, 410, 40, 40);
            g.setColor(Color.BLACK);
            g.drawString("Start", 45, 355);

            // Draw boulangerie goal
            g.setColor(new Color(255, 204, 102)); // croissant-colored!
            g.fillRect(700,410 , 60, 60); // goal area

            g.setColor(Color.BLACK);
            g.drawString("Boulangerie", 705, 355);


            level.render(g);
            player.render(g);

            g.setColor(Color.BLACK);
            g.drawString("Score: " + player.getScore(), 10, 20);
            g.drawString("Lives: " + player.getLives(), 10, 40);
        }
    }
}