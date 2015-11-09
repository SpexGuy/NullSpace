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

class WeaponPowerup extends Powerup {
    private Weapon weapon;
    public WeaponPowerup(Game game, Color color, double leakageRate, Weapon weapon, String name) {
        super(game, color, leakageRate, name);
        this.weapon = weapon;
    }
    @Override
    public void activate() {
        game.setWeapon(weapon);
    }
    @Override
    public void deactivate() {
        game.setDefaultWeapon();
    }
}

class DoubleScorePowerup extends Powerup {
    public DoubleScorePowerup(Game game, Color color, double leakageRate) {
        super(game, color, leakageRate, "Double Score");
    }
    @Override
    public void activate() {
        game.startDoubleScore();
    }
    @Override
    public void deactivate() {
        game.stopDoubleScore();
    }
}

class WingmanPowerup extends Powerup {
    public WingmanPowerup(Game game, Color color, double leakageRate) {
        super(game, color, leakageRate, "Wingman");
    }
    @Override
    public void activate() {
        game.addWingman();
    }
    @Override
    public void deactivate() {
        game.removeWingman();
    }
}

class PausePowerup extends Powerup {
    public PausePowerup(Game game, Color color, double leakageRate) {
        super(game, color, leakageRate, "Pause");
    }
    @Override
    public void activate() {
        game.pauseInvaders();
    }
    @Override
    public void deactivate() {
        game.forwardInvaders();
    }
}

class BackInTimePowerup extends Powerup {
    public BackInTimePowerup(Game game, Color color, double leakageRate) {
        super(game, color, leakageRate, "Rewind");
    }
    @Override
    public void activate() {
        game.reverseInvaders();
    }
    @Override
    public void deactivate() {
        game.forwardInvaders();
    }
}

class BreakoutPowerup extends Powerup {
    public BreakoutPowerup(Game game, Color color, double leakageRate) {
        super(game, color, leakageRate, "Breakout");
    }
    @Override
    public void activate() {
        game.startBreakout();
    }
    @Override
    public void deactivate() {
        game.stopBreakout();
    }
}
