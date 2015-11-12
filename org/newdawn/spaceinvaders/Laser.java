package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * A Laser draws a flaring line to its target, and destroys it once its duration is complete.
 */
public class Laser {
    private static final int laserDuration = 150;
    private static final int laserWidth = 8;
    private Game game;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private AlienEntity target;
    private int timeShowing = 0;

    public Laser(Game game, int startX, int startY, int endX, int endY, AlienEntity target) {
        this.game = game;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.target = target;
    }

    public void update(int dt) {
        timeShowing += dt;
        if (timeShowing >= laserDuration) {
            if (target != null)
                target.incinerate(); // This is after aliens are drawn, so the alien will still appear this frame.
            game.removeLaser(this);
        }
    }

    public void draw(Graphics2D g) {
        Stroke original = g.getStroke();
        if (target != null) {
            g.setColor(new Color(0xFF, 0, 0, 0xFF - timeShowing * 0x9F / laserDuration));
            int xRadius = target.getWidth()/2;
            int yRadius = target.getHeight()/2;
            int xCenter = target.getX() + xRadius;
            int yCenter = target.getY() + yRadius;
            int scaledXRadius = (int) (1.41 * xRadius * timeShowing / laserDuration);
            int scaledYRadius = (int) (1.41 * yRadius * timeShowing / laserDuration);
            g.fillOval(xCenter - scaledXRadius, yCenter - scaledYRadius, 2*scaledXRadius, 2*scaledYRadius);
        }
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke((laserDuration/2 - Math.abs(laserDuration/2 - timeShowing)) * laserWidth / (laserDuration/2)));
        g.drawLine(startX, startY, endX, endY);
        g.setStroke(original);
    }
}
