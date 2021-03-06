package org.newdawn.spaceinvaders;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public class ShotEntity extends VelocityEntity {
	/** The vertical speed at which the players shot moves */
	private static final double moveSpeed = -300;
	/** The game in which this entity exists */
	private Game game;
	private int numHits;
	
	/**
	 * Create a new shot from the player
	 * 
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public ShotEntity(Game game,String sprite,int x,int y, int numHits) {
		super(sprite,x,y);
		
		this.game = game;
		
		dy = moveSpeed;
		this.numHits = numHits;
	}

	/**
	 * Request that this shot moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	@Override
	public void update(int delta) {
		// proceed with normal move
		super.update(delta);
		
		// if we shot off the screen, remove ourselves
		if (y < -100) {
			game.getProjectiles().remove(this);
		}
	}
	
	/**
	 * Notification that this shot has collided with another
	 * entity
	 * 
	 * @param other The other entity with which we've collided
	 */
	@Override
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (numHits == 0) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof AlienEntity) {
			// remove the affected entities
			if (((AlienEntity) other).kill()) {
				if (numHits == 1)
					game.getProjectiles().remove(this);
				--numHits;
			}
		}
	}
}