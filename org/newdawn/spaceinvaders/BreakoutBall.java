package org.newdawn.spaceinvaders;

/**
 * Created by martin on 11/12/15.
 */
public class BreakoutBall extends VelocityEntity {
    private Game game;

    public BreakoutBall(Game game, String ref, double x, double y) {
        super(ref, x, y);
        this.dx = 200;
        this.dy = -200;
        this.game = game;
    }

    @Override
    public void update(int dt) {
        if (dx < 0 && x < 10 || dx > 0 && x + getWidth() > game.getWidth() - 10) {
            dx = -dx;
        }
        if (dy < 0 && y < 10) {
            dy = -dy;
        } else if (dy > 0 && y > game.getHeight()) {
            game.breakoutBallOut(this);
        }

        super.update(dt);
    }

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof AlienEntity) {
            ((AlienEntity) other).kill();
        } else if (other instanceof ShipEntity && dy > 0) {
            dy = -dy;
        }
    }
}
