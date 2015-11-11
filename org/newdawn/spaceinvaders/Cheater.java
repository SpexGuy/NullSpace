package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * A surprise helper to be revealed when the konami code is entered
 * Nobody knows what it is, or where it came from, or where it got that stylish hat, but it's here to help!
 */
public class Cheater {
    private static final Sprite raptor = SpriteStore.get().getSprite("sprites/vogue_raptor.png");
    private enum Mode {
        INACTIVE,
        APPEARING,
        AGITATING,
        ACTIVE,
        CALMING,
        DISAPPEARING
    }
    private static final int appearTime = 800;
    private static final int firingPeriod = 350;
    private static final int angerTime = 600;
    private static final int eyeX = 654;
    private static final int eyeY = 389;
    private static final int eyeRadius = 11;
    private static final Color eyeColor = new Color(1.0f, 0.0f, 0.0f, 0.9f);

    private Game game;
    private Mode mode = Mode.INACTIVE;
    private int height = appearTime;
    private int shotTimeRemaining = 0;
    private int anger = 0;

    public Cheater(Game game) {
        this.game = game;
    }

    public void activate() {
        mode = Mode.APPEARING;
    }

    public void update(int dt) {
        switch(mode) {
            case APPEARING:
                height -= dt;
                if (height <= 0) {
                    height = 0;
                    mode = Mode.AGITATING;
                }
                break;
            case AGITATING:
                anger += dt;
                if (anger >= angerTime) {
                    anger = angerTime;
                    mode = Mode.ACTIVE;
                    fire();
                }
                break;
            case ACTIVE:
                shotTimeRemaining -= dt;
                if (shotTimeRemaining <= 0) {
                    if (game.getAliens().size() <= 1) {
                        mode = Mode.CALMING;
                    } else {
                        fire();
                    }
                }
                break;
            case CALMING:
                anger -= dt;
                if (anger <= 0) {
                    anger = 0;
                    mode = Mode.DISAPPEARING;
                }
                break;
            case DISAPPEARING:
                height += dt;
                if (height >= appearTime) {
                    height = appearTime;
                    mode = Mode.INACTIVE;
                }
                break;
        }
    }

    private void fire() {
        shotTimeRemaining = firingPeriod;
        AlienEntity target = game.getAliens().getRandom();
        game.addLaser(new Laser(game,
                eyeX, eyeY,
                target.getX() + target.getWidth()/2, target.getY() + target.getHeight()/2,
                target));
    }

    public void draw(Graphics2D g) {
        raptor.draw(g, game.getWidth() - raptor.getWidth(), game.getHeight() - raptor.getHeight() + raptor.getHeight() * height / appearTime, 1.0);
        int radius = eyeRadius * anger / angerTime;
        if (radius > 0) {
            g.setColor(eyeColor);
            g.fillOval(eyeX - radius, eyeY - radius, 2 * radius, 2 * radius);
        }
    }
}
