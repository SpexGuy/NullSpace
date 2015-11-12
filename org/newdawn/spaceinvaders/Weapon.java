package org.newdawn.spaceinvaders;

/**
 * A Weapon abstracts the concepts of reload time and projectile creation.
 */
public abstract class Weapon {
    protected Game game;
    /** The time at which last fired a shot */
    private long lastFire = 0;
    /** The interval between our players shot (ms) */
    private long firingInterval;

    public Weapon(Game game, long firingInterval) {
        this.game = game;
        this.firingInterval = firingInterval;
    }

    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        for (Entity ship : game.getShips()) {
            createShotEntity(ship);
        }
    }

    protected abstract void createShotEntity(Entity ship);
}

class ProjectileWeapon extends Weapon {
    public ProjectileWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected void createShotEntity(Entity ship) {
        game.getProjectiles().add(new ShotEntity(game,"sprites/shot.gif",ship.getX()+10,ship.getY()-30, 1));
    }
}

class PiercingWeapon extends Weapon {
    public PiercingWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected void createShotEntity(Entity ship) {
        game.getProjectiles().add(new ShotEntity(game,"sprites/drill.png",ship.getX()+10,ship.getY()-30, 5));
    }
}

class LaserWeapon extends Weapon {
    public LaserWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected void createShotEntity(Entity ship) {
        int xPos = ship.getX()+17;

        int bestYPos = 0;
        AlienEntity bestAlien = null;
        for (AlienEntity alien : game.getAliens()) {
            int yPos = alien.getY() + alien.getHeight();
            if (yPos > bestYPos) {
                if (alien.getX() <= xPos && alien.getX() + alien.getWidth() > xPos) {
                    bestYPos = yPos;
                    bestAlien = alien;
                }
            }
        }

        game.addLaser(new Laser(game, xPos, ship.getY(), xPos, bestYPos, bestAlien));
    }
}
