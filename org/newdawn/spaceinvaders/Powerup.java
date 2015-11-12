package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * A Powerup is a simple layer of indirection around activate() and deactivate().
 * It makes the PowerupManager more configurable and simpler by abstracting the specifics.
 */
public abstract class Powerup {
    protected Game game;
    private Color color;
    private String name;
    private double leakageRate;
    private double startPowerPerPress;
    private double lambda; // exponential falloff control

    public Powerup(Game game, Color color, String name, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        this.game = game;
        this.color = color;
        this.name = name;
        this.leakageRate = 1.0 / inactiveDuration;
        this.startPowerPerPress = startPowerPerPress;
        this.lambda = Math.log(500.0 / (inactiveDuration * startPowerPerPress)) / extraDuration;
    }

    public abstract void activate();
    public abstract void deactivate();

    public Color getColor() {
        return color;
    }

    public double getPower(int elapsedTime) {
        return startPowerPerPress * Math.exp(lambda * elapsedTime);
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

    public WeaponPowerup(Game game, Color color, String name, Weapon weapon, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, name, startPowerPerPress, inactiveDuration, extraDuration);
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
    public DoubleScorePowerup(Game game, Color color, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, "Double Score", startPowerPerPress, inactiveDuration, extraDuration);
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
    public WingmanPowerup(Game game, Color color, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, "Wingman", startPowerPerPress, inactiveDuration, extraDuration);
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
    public PausePowerup(Game game, Color color, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, "Pause Time", startPowerPerPress, inactiveDuration, extraDuration);
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
    public BackInTimePowerup(Game game, Color color, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, "Reverse Time", startPowerPerPress, inactiveDuration, extraDuration);
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
    public BreakoutPowerup(Game game, Color color, double startPowerPerPress, int inactiveDuration, int extraDuration) {
        super(game, color, "Breakout", startPowerPerPress, inactiveDuration, extraDuration);
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
