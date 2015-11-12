package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * The PowerupManager manages the powerup bar and key presses to maintain it.
 * It is in charge of activating and deactivating powerups.
 */
public class PowerupManager {
    private static final int flashDuration = 200;
    private static final int flashIntensity = 0x7F;
    private static final char[][] levels = {
            {'A','S','D','F'},
            {'A','S','D','F','W','E'},
            {'A','S','D','F','W','E','X','C'},
            {'A','S','D','F','W','E','X','C'}
    };
    private static final double powerLeakage = 1.0/(10 * 8 * 60);
    private static final Color backgroundColor = new Color(0, 0, 0, 0x8F);

    private Game game;
    private Powerup[] powerups;

    private int numPages;
    private int levelNumber = 0;
    private int flashCounter = 0;

    private int pageNumber = 0;
    private double pageScore = 0;
    private int streakCount = 0;
    private char target;
    private Powerup activePowerup = null;

    public PowerupManager(Game game, int numPages) {
        this.game = game;
        assert(numPages >= 1);
        assert(numPages <= powerups.length);
        this.numPages = numPages;
        this.powerups = new Powerup[] {
                new WeaponPowerup(game, Color.BLUE, powerLeakage, new ProjectileWeapon(game, 250), "Fast Reload"),
                new DoubleScorePowerup(game, Color.CYAN, powerLeakage),
                new WingmanPowerup(game, Color.GREEN, powerLeakage),
                new PausePowerup(game, Color.YELLOW, powerLeakage),
                new WeaponPowerup(game, Color.ORANGE, powerLeakage, new PiercingWeapon(game, 500), "Piercing Shots"),
                new BackInTimePowerup(game, Color.RED, powerLeakage),
                new WeaponPowerup(game, Color.MAGENTA, powerLeakage, new LaserWeapon(game, 500), "Laser"),
                new BreakoutPowerup(game, new Color(0x7F00FF), powerLeakage)
        };
        setTargetCharacter();
    }

    public void setNumPages(int numPages) {
        assert(numPages >= 1);
        assert(numPages <= powerups.length);
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

    public void update(int dt) {
        flashCounter = Math.max(0, flashCounter - dt);
        pageScore -= getPowerLeakage() * dt;
        if (pageScore < 0.0) {
            retreatPage();
        }
    }

    private double getPowerLeakage() {
        if (activePowerup != null)
            return activePowerup.getLeakageRate();
        return powerLeakage;
    }

    public void draw(Graphics2D g) {
        // Draw a background to cover high aliens and projectiles
        g.setColor(backgroundColor);
        g.fillRect(0, 0, game.getWidth(), 25 + 25 + 25 + 5);

        // Draw the target character key
        int gb = 0xFF - (0xFF * flashCounter / flashDuration);
        g.setColor(new Color(0xFF, gb, gb));
        g.drawRoundRect(5, 5, 20, 20, 5, 5);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(""+target, 5 + 20/2 - fm.charWidth(target)/2, 5 + 20/2 + fm.getHeight()/2 - fm.getDescent());

        // Draw the power bar
        int barLeft = 5 + 20 + 5;
        int width = game.getWidth() - 5 - barLeft;
        if (activePowerup == null && pageNumber > 0) {
            g.setColor(powerups[pageNumber-1].getColor());
            g.fillRoundRect(barLeft, 5, width, 20, 5, 5);
        }
        Color powerColor = powerups[pageNumber].getColor();
        g.setColor(powerColor);
        g.fillRoundRect(barLeft, 5, (int)(pageScore * width), 20, 5, 5);
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(barLeft, 5, width, 20, 5, 5);

        // Draw the multiplier

        g.setColor(Color.GRAY);
        String multiplierStr = String.format("(x%.2f)", getMultiplier());
        g.drawString(multiplierStr, barLeft + 5, 5 + 20/2 + fm.getHeight()/2 - fm.getDescent());

        // Draw the powerup tags
        for (int c = 0; c < numPages; c++) {
            int x = 5 + ((game.getWidth() - 10) * (c%4))/5;
            int y = 25 * (1 + c/4) + 5;
            if (activePowerup != null) {
                if (activePowerup == powerups[c]) {
                    g.setColor(powerups[c].getColor());
                    g.fillRoundRect(x, y, 20, 20, 5, 5);
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
            } else if (powerupAvailable(c)) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawRoundRect(x, y, 20, 20, 5, 5);
            g.drawString(""+(c+1), x + 20/2 - fm.charWidth((char)(c+'1'))/2, y + 20/2 + fm.getHeight()/2 - fm.getDescent());
            g.drawString(powerups[c].getName(), x + 20 + 5, y + 20/2 + fm.getHeight()/2 - fm.getDescent());
        }
    }

    public boolean tryCharacter(char key) {
        char upperKey = Character.toUpperCase(key);
        if (Character.isUpperCase(upperKey)) {
            if (upperKey == target) {
                onCorrectCharacter();
            } else {
                onIncorrectCharacter();
            }
            return true;
        } else if (activePowerup == null && Character.isDigit(key)) {
            int index = key - '1';
            if (powerupAvailable(index)) {
                activatePowerup(index);
            }
            return true;
        }
        return false;
    }

    private boolean powerupAvailable(int index) {
        return index < pageNumber || (index == pageNumber && pageScore >= 0.5);
    }

    private void activatePowerup(int index) {
        activePowerup = powerups[index];
        activePowerup.activate();
        if (index < pageNumber) {
            pageScore = 1.0;
        }
        pageNumber = index;
    }

    private void deactivatePowerup() {
        activePowerup.deactivate();
        activePowerup = null;
        pageNumber = 0;
    }

    private void onIncorrectCharacter() {
        flashCounter = flashDuration;
        streakCount = 0;
    }

    private void onCorrectCharacter() {
        double charScore = getMultiplier() / getDifficulty();
        streakCount++;
        pageScore += charScore;
        if (pageScore >= 1.0) {
            advancePage();
        }
        setTargetCharacter();
    }

    private double getDifficulty() {
        return 3 + pageNumber;
    }

    private double getMultiplier() {
        return 1.0 + 0.05 * streakCount;
    }

    private void advancePage() {
        if (activePowerup != null || pageNumber == numPages - 1) {
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
        if (activePowerup != null) {
            deactivatePowerup();
        }
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
