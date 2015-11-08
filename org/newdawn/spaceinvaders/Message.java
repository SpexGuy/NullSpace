package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * A Message is a string of characters over a background.
 * It covers the whole screen, so use them sparingly.
 * Created by martin on 11/7/15.
 */
public class Message {
    private static final int MAX_FADE = 30;
    private enum Mode {
        FADE_IN,
        FADE_OUT
    }

    private Game game;
    private String msg;
    private Mode mode = Mode.FADE_IN;
    private Message nextMessage;
    // The current fading progress. Goes from -MAX_FADE (starting point) to 0 (faded in) to MAX_FADE (faded out)
    private int fade = -MAX_FADE;

    public Message(Game game, String msg) {
        this(game, msg, null);
    }

    public Message(Game game, String msg, Message nextMessage) {
        this.game = game;
        this.msg = msg;
        this.nextMessage = nextMessage;
    }

    public void dismiss() {
        mode = Mode.FADE_OUT;
    }
    public void reset() {
        mode = Mode.FADE_IN;
        fade = -MAX_FADE;
    }
    public void draw(Graphics2D g) {
        switch(mode) {
            case FADE_IN:
                if (fade < 0)
                    ++fade;
                break;
            case FADE_OUT:
                if (fade < MAX_FADE)
                    ++fade;
                else
                    finish();
                break;
        }

        //TODO: draw message with the current fade
        int centerX = 800+(800*fade/MAX_FADE); // (between 0 and 1600)
        g.setColor(Color.white);
        g.drawString(msg,(centerX-g.getFontMetrics().stringWidth(msg))/2,250);
        g.drawString("Press any key",(centerX-g.getFontMetrics().stringWidth("Press any key"))/2,300);
    }

    private void finish() {
        if (nextMessage != null) {
            game.setMessage(nextMessage);
        } else {
            game.messageFinished();
        }
    }
}
