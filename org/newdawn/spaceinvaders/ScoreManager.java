package org.newdawn.spaceinvaders;

import java.awt.*;

/**
 * Created by martin on 11/10/15.
 */
public class ScoreManager {
    private Game game;
    private int score = 0;
    private Font scoreFont = null;

    public ScoreManager(Game game) {
        this.game = game;
    }

    public void alienKilled(int yPos) {
        // Add 500 to 1000 points, based on y position of alien
        addScore(10 + (game.getHeight() - yPos) * 10 / game.getHeight());
    }

    private void addScore(int change) {
        // TODO: killstreak multiplier
        score += change;
    }

    public void draw(Graphics2D g) {
        Font oldFont = g.getFont();
        if (scoreFont == null) {
            scoreFont = new Font(oldFont.getName(), oldFont.getStyle(), 40);
        }
        g.setFont(scoreFont);
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();
        String scoreStr = ""+score;
        g.drawString(scoreStr, game.getWidth() - 5 - fm.stringWidth(scoreStr), 25 + 5 + fm.getAscent());

        // restore the original font
        g.setFont(oldFont);
    }
}
