package org.newdawn.spaceinvaders;

/**
 * A VelocityEntity is an Entity which moves with a semi-constant velocity.
 */
public abstract class VelocityEntity extends Entity {
    /** The current speed of this entity horizontally (pixels/sec) */
    protected double dx = 0;
    /** The current speed of this entity vertically (pixels/sec) */
    protected double dy = 0;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     * @param scale The initial scale of this entity
     */
    public VelocityEntity(String ref, double x, double y, double scale) {
        super(ref, x, y, scale);
    }
    public VelocityEntity(String ref, double x, double y) {
        super(ref, x, y);
    }

    /**
     * Request that this entity move itself based on a certain ammount
     * of time passing.
     *
     * @param delta The ammount of time that has passed in milliseconds
     */
    public void update(int delta) {
        // update the location of the entity based on move speeds
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    /**
     * Set the horizontal speed of this entity
     *
     * @param dx The horizontal speed of this entity (pixels/sec)
     */
    public void setHorizontalMovement(double dx) {
        this.dx = dx;
    }

    /**
     * Set the vertical speed of this entity
     *
     * @param dy The vertical speed of this entity (pixels/sec)
     */
    public void setVerticalMovement(double dy) {
        this.dy = dy;
    }

    /**
     * Get the horizontal speed of this entity
     *
     * @return The horizontal speed of this entity (pixels/sec)
     */
    public double getHorizontalMovement() {
        return dx;
    }

    /**
     * Get the vertical speed of this entity
     *
     * @return The vertical speed of this entity (pixels/sec)
     */
    public double getVerticalMovement() {
        return dy;
    }

}
