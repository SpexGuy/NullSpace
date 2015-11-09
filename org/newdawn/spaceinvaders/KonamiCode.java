package org.newdawn.spaceinvaders;

import java.awt.event.KeyEvent;

/**
 * Created by martin on 11/9/15.
 */
public class KonamiCode {
    private Game game;
    private int currentPosition = 0;

    public KonamiCode(Game game) {
        this.game = game;
    }

    public boolean acceptCharacter(int character) {
        if (character == KeyEvent.VK_UP) {
            if (currentPosition > 2)
                currentPosition = 0;
            currentPosition = Math.min(currentPosition + 1, 2); // allow any number of up, as long as there are at least 2
            return true;
        } else if (currentPosition < 4 && character == KeyEvent.VK_DOWN) {
            currentPosition++;
            return true;
        } else if ((currentPosition == 4 || currentPosition == 6) && character == KeyEvent.VK_LEFT) {
            currentPosition++;
            return true;
        } else if ((currentPosition == 5 || currentPosition == 7) && character == KeyEvent.VK_RIGHT) {
            currentPosition++;
            return true;
        } else if (currentPosition == 8 && character == KeyEvent.VK_B) {
            currentPosition++;
            return true;
        } else if (currentPosition == 9 && character == KeyEvent.VK_A) {
            currentPosition = 0;
            game.acceptKonamiCode();
            return true;
        } else {
            currentPosition = 0;
            return false;
        }
    }
}
