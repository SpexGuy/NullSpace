package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/10/15.
 */
public class Wingman extends Entity {
    private static final int offset = 35;
    private enum Mode {
        ENTERING, ACTIVE, LEAVING, INACTIVE
    }

    private Game game;
    private ShipEntity ship;
    private Mode mode = Mode.INACTIVE;

    private static final int maxHeight = 100;
    private int height = 0;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param game   The game
     * @param ship   The ship for which this ship is the wingman
     * @param ref The reference to the image to be displayed for this entity
     */
    public Wingman(Game game, ShipEntity ship, String ref) {
        super(ref, ship.getX() + offset, game.getHeight());
        this.game = game;
        this.ship = ship;
    }

    @Override
    public void draw(Graphics g) {
        switch(mode) {
            case ENTERING:
                if (height < maxHeight) {
                    ++height;
                } else {
                    mode = Mode.ACTIVE;
                }
                break;
            case LEAVING:
                if (height > 0) {
                    --height;
                } else {
                    mode = Mode.INACTIVE;
                }
                break;
        }
        this.x = ship.getX() + offset;
        this.y = game.getHeight() - (game.getHeight() - 550) * height / maxHeight;

        super.draw(g);
    }

    public void enter() {
        mode = Mode.ENTERING;
    }

    public void leave() {
        mode = Mode.LEAVING;
    }

    @Override
    public void collidedWith(Entity other) {

    }
}
