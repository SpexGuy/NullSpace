package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/9/15.
 */
public class Cheater {
    private static final Sprite raptor = SpriteStore.get().getSprite("sprites/vogue_raptor.png");
    private enum Mode {
        HIDDEN,
        APPEARING,
        SHOWING,
        DISAPPEARING
    }
    private static final int appearTime = 500;
    private static final int showTime = 2400;

    private Game game;
    Mode mode = Mode.HIDDEN;
    private int counter = appearTime;

    public Cheater(Game game) {
        this.game = game;
    }

    public void activate() {
        mode = Mode.APPEARING;
        counter = Math.min(appearTime, counter);
    }

    public void update(int dt) {
        switch(mode) {
            case APPEARING:
                counter -= dt;
                if (counter <= 0) {
                    mode = Mode.SHOWING;
                    counter = showTime;
                }
                break;
            case DISAPPEARING:
                counter += dt;
                if (counter >= appearTime) {
                    counter = appearTime;
                    mode = Mode.HIDDEN;
                }
                break;
            case SHOWING:
                counter -= dt;
                if (counter <= 0) {
                    counter = 0;
                    mode = Mode.DISAPPEARING;
                }
                break;
        }
    }

    public void draw(Graphics2D g) {
        switch(mode) {
            case APPEARING:
            case DISAPPEARING:
                raptor.draw(g, game.getWidth() - raptor.getWidth(), game.getHeight() - raptor.getHeight() + raptor.getHeight() * counter / appearTime);
                break;
            case SHOWING:
                raptor.draw(g, game.getWidth() - raptor.getWidth(), game.getHeight() - raptor.getHeight());
                break;
        }
    }
}
