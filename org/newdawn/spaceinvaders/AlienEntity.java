package org.newdawn.spaceinvaders;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class AlienEntity extends VelocityEntity {
	/** The speed at which the alien moves horizontally */
	private static final double moveSpeed = 75;
	/** The game in which the entity exists */
	private Game game;

	private boolean dead = false;
	
	/**
	 * Create a new alien entity
	 * 
	 * @param game The game in which this entity is being created
	 * @param ref The sprite which should be displayed for this alien
	 * @param x The initial x location of this alien
	 * @param y The initial y location of this alien
	 */
	public AlienEntity(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game;
		dx = -moveSpeed;
	}

	/**
	 * Request that this alien moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	@Override
	public void update(int delta) {
		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update 
		if ((dx < 0 ^ delta < 0) && (x < 10)) {
			game.alienHitEdge();
		}
		// and vice versa, if we have reached the right hand side of
		// the screen and are moving right, request a logic update
		if ((dx > 0 ^ delta < 0) && (x > 750)) {
			game.alienHitEdge();
		}
		
		// proceed with normal move
		super.update(delta);
	}

	/**
	 * Move the aliens down
	 */
	public void moveDown(int direction) {
		// swap over horizontal movement and move down the
		// screen a bit
		dx = -dx;
		y += direction * 10;
		
		// if we've reached the bottom of the screen then the player
		// dies
		if (y > 570) {
			game.notifyDeath();
		}
	}

	public boolean kill() {
		if (dead)
			return false;
		dead = true;

		game.getAliens().remove(this);
		game.notifyAlienKilled(this);
		return true;
	}
	
	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other The other entity
	 */
	@Override
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
}