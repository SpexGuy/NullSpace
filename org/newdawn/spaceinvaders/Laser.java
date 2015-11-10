package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/9/15.
 */
public class Laser {
    private static final int laserDuration = 10;
    private Game game;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private AlienEntity target;
    private int frameCount = 0;

    public Laser(Game game, int startX, int startY, int endX, int endY, AlienEntity target) {
        this.game = game;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.target = target;
    }

    public void draw(Graphics2D g) {
        ++frameCount;
        if (frameCount == laserDuration) {
            if (target != null)
                target.kill(); // This is after aliens are drawn, so the alien will still appear this frame.
            game.removeLaser(this);
        } else {
            // I kinda like everything pulsing when the laser is fired, so I'm not restoring the stroke.
            //Stroke original = g.getStroke();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(5 - Math.abs(5 - frameCount)));
            g.drawLine(startX, startY, endX, endY);
            //g.setStroke(original);
        }
    }
}
