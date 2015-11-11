package org.newdawn.spaceinvaders;

/**
 * Created by martin on 11/11/15.
 */
public abstract class Level {
    protected Game game;

    public Level(Game game) {
        this.game = game;
    }

    public abstract void initEntities();

    public abstract Message win();
}

class Level1 extends Level {
    public Level1(Game game) {
        super(game);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        EntityGroup<AlienEntity> aliens = game.getAliens();
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                AlienEntity alien = new AlienEntity(game,"sprites/alien.gif",100+(x*50),(50)+row*30);
                aliens.add(alien);
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
        super(game);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        EntityGroup<AlienEntity> aliens = game.getAliens();
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                AlienEntity alien = new AlienEntity(game,"sprites/alien.gif",100+(x*50),(50)+row*30);
                aliens.add(alien);
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
        super(game);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        EntityGroup<AlienEntity> aliens = game.getAliens();
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                AlienEntity alien = new AlienEntity(game,"sprites/alien.gif",100+(x*50),(50)+row*30);
                aliens.add(alien);
            }
        }
    }
    @Override
    public Message win() {
        return new Message(game, "Congratulations! They're gone for now...");
    }
}

class Level4 extends Level {
    public Level4(Game game) {
        super(game);
    }
    @Override
    public void initEntities() {
        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        EntityGroup<AlienEntity> aliens = game.getAliens();
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                AlienEntity alien = new AlienEntity(game,"sprites/alien.gif",100+(x*50),(50)+row*30);
                aliens.add(alien);
            }
        }
    }
    @Override
    public Message win() {
        return new Message(game, "That was the last of them! Play again?");
    }
}
