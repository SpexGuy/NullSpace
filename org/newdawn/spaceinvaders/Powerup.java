package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/9/15.
 */
public abstract class Powerup {
    protected Game game;
    private Color color;
    private double leakageRate;
    private String name;

    public Powerup(Game game, Color color, double leakageRate, String name) {
        this.game = game;
        this.color = color;
        this.leakageRate = leakageRate;
        this.name = name;
    }

    public abstract void activate();
    public abstract void deactivate();

    public Color getColor() {
        return color;
    }

    public double getLeakageRate() {
        return leakageRate;
    }

    public String getName() {
        return name;
    }
}
