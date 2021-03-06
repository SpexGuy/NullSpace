package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * The PowerupManager manages the powerup bar and key presses to maintain it.
 * It is in charge of activating and deactivating powerups.
 */
public class PowerupManager {
    private static final int flashDuration = 200;
    private static final char[][] levels = {
            {'A','S','D','F'},
            {'A','S','D','F','W','E'},
            {'A','S','D','F','W','E','R'},
            {'A','S','D','F','W','E','R','G'}
    };
    private static final double powerLeakage = 1.0 / 4000;
    private static final Color backgroundColor = new Color(0, 0, 0, 0x8F);

    private Game game;
    private Powerup[] powerups;
    private Font backgroundFont;
    private Font multiplierFont;

    private int numPages;
    private int levelNumber = 0;
    private int flashCounter = 0;

    private int pageNumber = 0;
    private double pageScore = 0;
    private int streakCount = 0;
    private char target;
    private Powerup activePowerup = null;
    private int activePowerupTime = 0;

    public PowerupManager(Game game, int numPages) {
        this.game = game;
        assert(numPages >= 1);
        assert(numPages <= powerups.length);
        this.numPages = numPages;
        this.powerups = new Powerup[] {
                new WeaponPowerup(game, Color.BLUE, "Fast Reload", new ProjectileWeapon(game, 250), 1/3.0, 5000, 4000),
                new DoubleScorePowerup(game, Color.CYAN, 1/3.33, 10000, 10000),
                new WingmanPowerup(game, Color.GREEN, 1/3.66, 10000, 10000),
                new PausePowerup(game, Color.YELLOW, 1/4.0, 6000, 4000),
                new WeaponPowerup(game, Color.ORANGE, "Piercing Shots", new PiercingWeapon(game, 500), 1/4.33, 4000, 4000),
                new BackInTimePowerup(game, Color.RED, 1/4.66, 6000, 3000),
                new WeaponPowerup(game, Color.MAGENTA, "Laser", new LaserWeapon(game, 500), 1/5.0, 5000, 6000),
                new BreakoutPowerup(game, new Color(0x7F00FF), 1/5.33, 20000, 15000)
        };
        setTargetCharacter();
    }

    public void setNumPages(int numPages) {
        assert(numPages >= 1);
        assert(numPages <= powerups.length);
        if (numPages < this.numPages) {
            reset();
        }
        this.numPages = numPages;
    }

    private void reset() {
        pageNumber = 0;
        pageScore = 0;
        streakCount = 0;
        if (activePowerup != null) {
            activePowerup.deactivate();
            activePowerup = null;
        }
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
        activePowerupTime += dt;
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

    public void drawBackground(Graphics2D g) {
        Font oldFont = g.getFont();
        if (backgroundFont == null) {
            backgroundFont = new Font(oldFont.getName(), oldFont.getStyle(), 300);
            multiplierFont = new Font(oldFont.getName(), oldFont.getStyle(), 30);
        }

        // Draw the target character key
        double z = (double) flashCounter / flashDuration;
        int r  = (int) (0x3F * (1-z)  + 0xFF * z);
        int gb = (int) (0x3F * (1-z));
        g.setColor(new Color(r, gb, gb));
        g.setFont(backgroundFont);
        g.drawRoundRect(game.getWidth()/2 - 400/2, game.getHeight()/2 - 400/2 + 50, 400, 400, 100, 100);
        FontMetrics bigFM = g.getFontMetrics();
        g.drawString(""+target, game.getWidth()/2 - bigFM.charWidth(target)/2, game.getHeight()/2 + 40 + bigFM.getHeight()/2 - bigFM.getDescent());

        // Draw the multiplier
        g.setFont(multiplierFont);
        FontMetrics lilFM = g.getFontMetrics();
        String multiplierStr = String.format("(x%.2f)", getMultiplier());
        g.drawString(multiplierStr, game.getWidth()/2 - lilFM.stringWidth(multiplierStr)/2, game.getHeight()/2 + 40 + bigFM.getHeight()/2);

        g.setFont(oldFont);
    }

    public void draw(Graphics2D g) {
        // Draw a background to cover high aliens and projectiles
        g.setColor(backgroundColor);
        g.fillRect(0, 0, game.getWidth(), 25 + 25 + 25 + 5);

        // Draw the power bar
        int width = game.getWidth() - 5 - 5;
        if (activePowerup == null && pageNumber > 0) {
            g.setColor(powerups[pageNumber-1].getColor());
            g.fillRoundRect(5, 5, width, 20, 5, 5);
        }
        Color powerColor = powerups[pageNumber].getColor();
        g.setColor(powerColor);
        g.fillRoundRect(5, 5, (int)(pageScore * width), 20, 5, 5);
        g.setColor(Color.GRAY);
        g.drawRoundRect(5, 5, width, 20, 5, 5);

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
            FontMetrics fm = g.getFontMetrics();
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
        activePowerupTime = 0;
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
        double charScore = getMultiplier() * getPower();
        streakCount++;
        pageScore += charScore;
        if (pageScore >= 1.0) {
            advancePage();
        }
        setTargetCharacter();
    }

    private double getPower() {
        if (activePowerup != null)
            return activePowerup.getPower(activePowerupTime);
        return 1.0 / (3 + 0.33 * pageNumber);
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
            pageScore /= getPower();
            pageNumber++;
            pageScore *= getPower();
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
