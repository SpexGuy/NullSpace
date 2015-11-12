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
	private int size;
	
	/**
	 * Create a new alien entity
	 * 
	 * @param game The game in which this entity is being created
	 * @param ref The sprite which should be displayed for this alien
	 * @param x The initial x location of this alien
	 * @param y The initial y location of this alien
	 */
	public AlienEntity(Game game,String ref,double x,double y, int size) {
		super(ref, x, y, 1 << size);
		
		this.game = game;
		dx = -moveSpeed;
		this.size = size;
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
		if ((dx > 0 ^ delta < 0) && (x+getWidth() > 790)) {
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
		if (y + this.getHeight() >= 600) {
			game.notifyDeath();
		}
	}

	public boolean kill() {
		if (dead)
			return false;

		if (size > 0) {
			for (int c = 0; c < 2; c++) {
				for (int d = 0; d < 2; d++) {
					AlienEntity child = new AlienEntity(game, sprite.getRef(), x, y, size-1);
					child.dx = dx;
					child.dy = dy;
					child.x += c * child.getWidth();
					child.y += d * child.getHeight();
					game.getAliens().add(child);
				}
			}
		}
		die();
		return true;
	}

	public boolean incinerate() {
		if (dead)
			return false;
		die();
		return true;
	}

	private void die() {
		game.getAliens().remove(this);
		game.notifyAlienKilled(this);
		dead = true;
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