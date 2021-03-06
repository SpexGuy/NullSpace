package org.newdawn.spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alien killed, player died) and will take
 * appropriate game actions.
 *
 * @author Kevin Glass
 */
public class Game extends Canvas {
	/** The strategy that allows us to use accelerated page flipping */
	private BufferStrategy strategy;

	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	private Message currentMessage = null;
	private EntityGroup<AlienEntity> aliens = new EntityGroup<>();
	private EntityGroup<Entity> ships = new EntityGroup<>();
	private EntityGroup<VelocityEntity> projectiles = new EntityGroup<>();
	private Group<Laser> lasers = new Group<>();
	private int alienMultiplier = 1;
	/** The entity representing the player */
	private ShipEntity ship;
	private Wingman wingman;
	private ShipEntity paddle;
	/** The speed at which the player's ship should move (pixels/sec) */
	private static final double moveSpeed = 300;
	private final Weapon defaultWeapon = new ProjectileWeapon(this, 500);
	private Weapon weapon = defaultWeapon;
	private PowerupManager powerupManager = new PowerupManager(this, 2);
	private ScoreManager scorekeeper = new ScoreManager(this);
	private KonamiCode konami = new KonamiCode(this);
	private Cheater cheater = new Cheater(this);

	private Level[] levels = {
			new Level1(this),
			new Level2(this),
			new Level3(this),
			new Level4(this)
	};
	private int currentLevel = 0;

	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean aliensHitEdge = false;
	private int numSpeedupsNeeded = 0;
	private boolean breakoutMode = false;
	
	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		// create a frame to contain our game
		JFrame container = new JFrame("Null Space");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// initialise the entities in our game so there's something
		// to see at startup
		ship = new ShipEntity(this,"sprites/ship.gif",370,550);
		wingman = new Wingman(this, ship, "sprites/ship.gif");
		paddle = new ShipEntity(this, "sprites/paddle.png", 0, 0);
		// create the player ship and place it roughly in the center of the screen
		ships.add(ship);
		initEntities();
		currentMessage = new Message(this, "Welcome to NullSpace!",
						 new Message(this, "Hold the spacebar to fire, and use the arrow keys to move.",
						 new Message(this, "Press the key shown in the background to build up power.",
						 new Message(this, "For best results, put your left hand on asdf, with your thumb on the spacebar.",
						 new Message(this, "Once you have enough power, press a number key to activate a powerup!")))));
	}
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and initialise a new set
		aliens.clear();
		projectiles.clear();
		initEntities();
		scorekeeper.restore();
		powerupManager.setLevelNumber(currentLevel);
		powerupManager.setNumPages((currentLevel+1)*2);
		
		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
		aliensHitEdge = false;
		numSpeedupsNeeded = 0;
	}
	
	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entity will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		levels[currentLevel].initEntities();
		aliens.completeFrame();
		ships.completeFrame();
		projectiles.completeFrame();
		lasers.completeFrame();
	}
	
	/**
	 * Notification from an alien that the aliens
	 * should be moved down at the next opportunity
	 */
	public void alienHitEdge() {
		aliensHitEdge = true;
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		setMessage(new Message(this, "Oh no! They got you, try again?"));
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		scorekeeper.setRestorePoint();
		setMessage(levels[currentLevel].win());
		currentLevel = (currentLevel + 1) % levels.length;
		powerupManager.setNumPages((currentLevel+1)*2);
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled(AlienEntity killed) {
		scorekeeper.alienKilled(killed.getY());

		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		numSpeedupsNeeded++;
	}
	
	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			// It better not be over 2^31 ms.
			int delta = (int) (System.currentTimeMillis() - lastLoopTime);
			lastLoopTime = System.currentTimeMillis();

			// Get hold of a graphics context for the accelerated
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);

			if (currentMessage == null) {
				if (aliens.size() == 0) {
					notifyWin();
					continue;
				}

				// cycle round asking each entity to move itself
				if (!breakoutMode) {
					for (AlienEntity entity : aliens) {
						entity.update(alienMultiplier * delta);
					}
				}
				ship.update(delta);
				wingman.update(delta);
				paddle.moveTo(ship);
				for (VelocityEntity entity : projectiles) {
					entity.update(delta);
				}
			}
			cheater.update(delta);
			if (currentMessage == null) {
				for (Laser laser : lasers) {
					laser.update(delta);
				}
				powerupManager.update(delta);
				scorekeeper.update(delta);

				// brute force collisions, compare every entity against
				// every other entity. If any of them collide notify
				// both entities that the collision has occurred
				projectiles.checkCollisions(aliens);
				aliens.checkCollisions(ships);
				if (breakoutMode) {
					projectiles.checkCollisions(ships);
				}

				// remove any entity that has been marked for clear up
				aliens.completeFrame();
				ships.completeFrame();
				projectiles.completeFrame();
				lasers.completeFrame();
			} else {
				currentMessage.update(delta);
			}

			// cycle round drawing all the entities we have in the game
			powerupManager.drawBackground(g);
			for (Entity entity : aliens) {
				entity.draw(g);
			}
			if (breakoutMode) {
				paddle.draw(g);
			} else {
				ship.draw(g);
			}
			wingman.draw(g);
			for (Entity entity : projectiles) {
				entity.draw(g);
			}
			cheater.draw(g);
			for (Laser l : lasers) {
				l.draw(g);
			}

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			if (aliensHitEdge) {
				for (AlienEntity alien : aliens) {
					alien.moveDown(alienMultiplier);
				}
				aliensHitEdge = false;
			}
			while (numSpeedupsNeeded > 0) {
				for (AlienEntity alien : aliens) {
					alien.setHorizontalMovement(alien.getHorizontalMovement() * 1.02);
				}
				numSpeedupsNeeded--;
			}

			powerupManager.draw(g);
			scorekeeper.draw(g);

			// if we're waiting for an "any key" press then draw the 
			// current message
			if (currentMessage != null) {
				currentMessage.draw(g);
			}
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();
			
			// resolve the movement of the ship. First assume the ship 
			// isn't moving. If either cursor key is pressed then
			// update the movement appropriately
			ship.setHorizontalMovement(0);
			
			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);
			}
			
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				weapon.tryToFire();
			}
			
			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try { Thread.sleep(10); } catch (Exception ignored) {}
		}
		System.exit(0);
	}

	public void setMessage(Message nextMessage) {
		nextMessage.reset();
		currentMessage = nextMessage;
	}

	public void messageFinished() {
		currentMessage = null;
		startGame();
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public void setDefaultWeapon() {
		this.weapon = defaultWeapon;
	}

	public void startDoubleScore() {
		scorekeeper.setPowerupMultiplier(2);
	}
	public void stopDoubleScore() {
		scorekeeper.setPowerupMultiplier(1);
	}

	public void addWingman() {
		wingman.enter();
		ships.add(wingman);
	}
	public void removeWingman() {
		ships.remove(wingman);
		wingman.leave();
	}

	public void reverseInvaders() {
		alienMultiplier = -1;
	}
	public void pauseInvaders() {
		alienMultiplier = 0;
	}
	public void forwardInvaders() {
		alienMultiplier = 1;
	}

	public void startBreakout() {
		projectiles.clear();
		projectiles.add(new BreakoutBall(this, "sprites/ball.png", ship.getX() + 5, ship.getY() - 10));
		breakoutMode = true;
		ships.clear();
		ships.add(paddle);
	}
	public void stopBreakout() {
		projectiles.clear();
		breakoutMode = false;
		ships.clear();
		ships.add(ship);
	}
	public void breakoutBallOut(BreakoutBall ball) {
		// just relaunch
		projectiles.remove(ball);
		projectiles.add(new BreakoutBall(this, "sprites/ball.png", paddle.getX() + 50, paddle.getY() - 10));
	}

	public void acceptKonamiCode() {
		cheater.activate();
	}

	public EntityGroup<AlienEntity> getAliens() {
		return aliens;
	}
	public EntityGroup<Entity> getShips() {
		return ships;
	}
	public EntityGroup<VelocityEntity> getProjectiles() {
		return projectiles;
	}

	public void addLaser(Laser laser) {
		lasers.add(laser);
	}
	public void removeLaser(Laser laser) {
		lasers.remove(laser);
	}

	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habit then anything else. Its perfectly normal to implement
	 * this as separate class if slight less convenient.
	 * 
	 * @author Kevin Glass
	 */
	private class KeyInputHandler extends KeyAdapter {
		
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. That's where keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed 
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "press"
			if (currentMessage != null) {
				return;
			}


			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}

			if (!konami.acceptCharacter(e.getKeyCode())) {
				powerupManager.tryCharacter(e.getKeyChar());
			}
		} 
		
		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released 
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "released"
			if (currentMessage != null) {
				return;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notification from AWT that a key has been typed. Note that
		 * typing a key means to both press and then release it.
		 *
		 * @param e The details of the key that was typed. 
		 */
		public void keyTyped(KeyEvent e) {

			// if we hit enter, dismiss the message.
			if (currentMessage != null && e.getKeyChar() == '\n') {
				currentMessage.dismiss();
			}

			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				gameRunning = false;
			}
		}
	}
	
	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 * 
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		Game g =new Game();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		g.gameLoop();
	}
}
