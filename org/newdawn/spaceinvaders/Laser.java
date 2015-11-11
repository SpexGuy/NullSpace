package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/9/15.
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
                target.kill(); // This is after aliens are drawn, so the alien will still appear this frame.
            game.removeLaser(this);
        }
    }

    public void draw(Graphics2D g) {
        Stroke original = g.getStroke();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke((laserDuration/2 - Math.abs(laserDuration/2 - timeShowing)) * laserWidth / (laserDuration/2)));
        g.drawLine(startX, startY, endX, endY);
        g.setStroke(original);
    }
}
