package org.newdawn.spaceinvaders;

/**
 * Created by martin on 11/9/15.
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

    public void tryToFire(Entity ship) {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        Entity shot = createShotEntity(ship);
        game.getProjectiles().add(shot);
    }

    protected abstract Entity createShotEntity(Entity ship);
}

class ProjectileWeapon extends Weapon {
    public ProjectileWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected Entity createShotEntity(Entity ship) {
        return new ShotEntity(game,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
    }
}

class PiercingWeapon extends Weapon {
    public PiercingWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected Entity createShotEntity(Entity ship) {
        // TODO: Piercing shots
        return new ShotEntity(game,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
    }
}

class LaserWeapon extends Weapon {
    public LaserWeapon(Game game, long firingInterval) {
        super(game, firingInterval);
    }
    @Override
    protected Entity createShotEntity(Entity ship) {
        // TODO: Laser shots
        return new ShotEntity(game,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
    }
}
