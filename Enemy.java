//Monika
/* Enemies or obstacles that chase you
  Walks back and forth(or chases you)
  Hurts you (takes away a life)
  Knows how to draw itself on screen
 */
package com.lecroissantrun;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Graphics;

public class Enemy extends Entity {

private Image image;
private int direction = 1; //1=right -1=left

public Enemy(int x, int y) {

    super(x, y, 32, 32);
    image = new ImageIcon("assets/enemy.png").getImage();

}

public void update() {

    x = x + direction * speed;
    if (x <= 0 || x >= 400) direction *= -1;

}

public void draw(Graphics g) {

    g.drawImage(image, x, y, null);

}

public Rectangle getBounds() {

    return new Rectangle(x, y, width, height);

}

}