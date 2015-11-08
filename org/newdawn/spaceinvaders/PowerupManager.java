package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/8/15.
 */
public class PowerupManager {
    private static final char[][] levels = {
            {'A','S','D','F'},
            {'A','S','D','F','Q','W','E','R'},
            {'A','S','D','F','Q','W','E','R','Z','X','C','V'},
            {'A','S','D','F','Q','W','E','R','Z','X','C','V','T','G','B'}
    };
    private static final Color[] pageColors = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.RED,
            Color.MAGENTA,
            new Color(0x7F00FF)
    };
    private static final double powerLeakage = 1.0/(8 * 60);

    private Game game;

    private int numPages;
    private int levelNumber = 0;

    private int pageNumber = 0;
    private double pageScore = 0;
    private char target;

    public PowerupManager(Game game, int numPages) {
        this.game = game;
        assert(numPages >= 1);
        assert(numPages <= pageColors.length);
        this.numPages = numPages;
        setTargetCharacter();
    }

    public void setNumPages(int numPages) {
        assert(numPages >= 1);
        assert(numPages <= pageColors.length);
        this.numPages = numPages;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
        setTargetCharacter();
    }

    private void setTargetCharacter() {
        char[] level = levels[levelNumber];
        char oldTarget = target;
        // Make sure we don't get the same character twice in a row
        do {
            target = level[(int) (Math.random() * level.length)];
        } while (target == oldTarget);
    }

    public void draw(Graphics2D g) {
        pageScore -= powerLeakage;
        if (pageScore < 0.0) {
            retreatPage();
        }

        g.setColor(Color.WHITE);
        g.drawRoundRect(5, 5, 20, 20, 5, 5);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(""+target, 5 + 20/2 - fm.charWidth(target)/2, 5 + 20/2 + fm.getAscent()/2);

        int width = game.getWidth() - 5 - 10 - 20 - 5;
        if (pageNumber == 0) {
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(5 + 20 + 10, 5, width, 20, 5, 5);
        } else {
            g.setColor(pageColors[pageNumber-1]);
            g.fillRoundRect(5 + 20 + 10, 5, width, 20, 5, 5);
        }
        g.setColor(pageColors[pageNumber]);
        g.fillRoundRect(5 + 20 + 10, 5, (int)(pageScore * width), 20, 5, 5);
    }

    public boolean tryCharacter(char key) {
        char upperKey = Character.toUpperCase(key);
        if (Character.isUpperCase(upperKey)) {
            if (upperKey == target){
                onCorrectCharacter();
            }else{
                onIncorrectCharacter();
            }
            return true;
        }
        return false;
    }

    private void onIncorrectCharacter() {
        game.onIncorrectCharacter();
    }

    private void onCorrectCharacter() {
        double charScore = 1.0 / getDifficulty();
        // TODO: multiplier?
        pageScore += charScore;
        if (pageScore >= 1.0) {
            advancePage();
        }

        game.onCorrectCharacter();
        setTargetCharacter();
    }

    private double getDifficulty() {
        return 3 + pageNumber;
    }

    private void advancePage() {
        if (pageNumber == numPages - 1) {
            // don't overflow pageNumber
            pageScore = 1.0;
        } else {
            // Reset pageScore, then scale remainder to new difficulty.
            pageScore -= 1.0;
            pageScore *= getDifficulty();
            pageNumber++;
            pageScore /= getDifficulty();
        }
    }

    private void retreatPage() {
        if (pageNumber == 0) {
            // don't underflow pageNumber
            pageScore = 0.0;
        } else {
            // reset pageScore
            pageNumber--;
            pageScore = 1.0;
        }
    }
}
