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
    private static final int appearFrames = 50;
    private static final int showFrames = 240;

    private Game game;
    Mode mode = Mode.HIDDEN;
    private int counter = appearFrames;

    public Cheater(Game game) {
        this.game = game;
    }

    public void activate() {
        mode = Mode.APPEARING;
        counter = Math.min(appearFrames, counter);
    }

    public void draw(Graphics2D g) {
        switch(mode) {
            case APPEARING:
                if (counter > 0) {
                    --counter;
                } else {
                    mode = Mode.SHOWING;
                    counter = showFrames;
                }
                break;
            case DISAPPEARING:
                if (counter < appearFrames) {
                    ++counter;
                } else {
                    mode = Mode.HIDDEN;
                }
                break;
            case SHOWING:
                if (counter > 0) {
                    --counter;
                } else {
                    mode = Mode.DISAPPEARING;
                }
                break;
        }

        switch(mode) {
            case APPEARING:
            case DISAPPEARING:
                raptor.draw(g, game.getWidth() - raptor.getWidth(), game.getHeight() - raptor.getHeight() + raptor.getHeight() * counter / appearFrames);
                break;
            case SHOWING:
                raptor.draw(g, game.getWidth() - raptor.getWidth(), game.getHeight() - raptor.getHeight());
                break;
        }
    }
}
