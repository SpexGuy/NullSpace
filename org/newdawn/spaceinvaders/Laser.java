package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/9/15.
 */
public class Laser {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private AlienEntity target;

    public Laser(int startX, int startY, int endX, int endY, AlienEntity target) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.target = target;
    }

    public void draw(Graphics2D g) {
        if (target != null)
            target.kill(); // This is after aliens are drawn, so the alien will still appear this frame.

        g.setColor(Color.RED);
        g.drawLine(startX, startY, endX, endY);
    }
}
