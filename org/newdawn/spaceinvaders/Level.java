package org.newdawn.spaceinvaders;

/**
 * Created by martin on 11/11/15.
 */
public abstract class Level {
    private static final int yOffset = 50;
    protected Game game;
    private final int xOffset;
    private final int cellWidth;
    private final int cellHeight;

    public Level(Game game, int cellWidth, int cellHeight, int gridWidth) {
        this.game = game;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.xOffset = (800 - (gridWidth * cellWidth))/2;
    }

    protected void addAlien(int row, int col, int size) {
        game.getAliens().add(new AlienEntity(game, "sprites/alien.gif", xOffset + col * cellWidth, yOffset + row * cellHeight, size));
    }

    public abstract void initEntities();

    public abstract Message win();
}

class Level1 extends Level {
    public Level1(Game game) {
        super(game, 50, 30, 12);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                addAlien(row, x, 0);
            }
        }
    }

    @Override
    public Message win() {
        return new Message(game, "Congratulations! They're gone for now...");
    }
}

class Level2 extends Level {
    public Level2(Game game) {
        super(game, 45, 30, 13);
    }
    @Override
    public void initEntities() {
        for (int row=0;row<6;row++) {
            for (int x=0;x<13;x++) {
                if (x%3 == 0 || row%3 == 2) {
                    addAlien(row, x, 0);
                } else if (x%3 == 1 && row%3 == 0) {
                    addAlien(row, x, 1);
                }
            }
        }
    }
    @Override
    public Message win() {
        return new Message(game, "Congratulations! They're gone for now...");
    }
}

class Level3 extends Level {
    public Level3(Game game) {
        super(game, 50, 30, 12);
    }

    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        for (int row=0;row<3;row++) {
            for (int x=0;x<12;x++) {
                if (row == 0 || (x >= 4 && x < 8)) {
                    addAlien(row, x, 0);
                } else if (row == 1 && x%2 == 0) {
                    addAlien(row, x, 1);
                }
            }
        }
        for (int x = 0; x < 12; x += 4) {
            addAlien(3, x, 2);
        }
    }
    @Override
    public Message win() {
        return new Message(game, "Congratulations! They're gone for now...");
    }
}

class Level4 extends Level {
    public Level4(Game game) {
        super(game, 43, 29, 14);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        for (int row=0;row<8;row++) {
            addAlien(row, 0, 0);
            addAlien(row, 13, 0);
        }
        for (int col = 0; col < 14; col++) {
            addAlien(8, col, 0);
        }
        for (int row = 0; row < 8; row += 2) {
            addAlien(row, 1, 1);
            addAlien(row, 11, 1);
        }
        addAlien(0, 3, 3);
    }
    @Override
    public Message win() {
        return new Message(game, "That was the last of them! Play again?");
    }
}
