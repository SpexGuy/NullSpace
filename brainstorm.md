# Brainstorming:
## Goals:
 - The game progresses for 4 rounds.
    - Each round should be harder than the previous.
    - If you die you restart the round you are currently on.
    - If you restart the game you start back at the first round.

 - A score is displayed.
    - The score is increased by ?? for every space invader you kill.

 - at least 3 upgrades to your ship/weapons.
   - One upgrade is automatically unlocked after each round.
 - fun to play!

## Problem: Space Invaders isn't really fun.
I need to change the game somewhat. But how? Powerups alone won't do it.

### Why is it boring?
 - invaders move slowly
 - no buildup - goes from 0 to dead in 0.016 seconds

### Solutions:
 - more concrete goal
 - more difficult
 - more tension
 - more enemy types (well, duh, but that's not enough)
 - extreme focus on score
 - new dimension of play

### Best solution seems to be to focus on score. How do we change?
 - optional enemies (flybys)
 - hit streaks
 - award stars based on score
 - other awards based on performance
 - make it frantic
 - context-switch required to unlock powerups

## Powerup Ideas:
 - shield (1)
 - fast shot (2)
 - double score (3)
 - back-in-time (move invaders back) (4)
 - wingman (5)
 - piercing shots (6)
 - laser (instant shot) (7)
 - breakout (convert game to breakout) (8)

## How are powerups obtained?
 - kill special enemies
 - shoot certain spot
 - always have random powerup, may be good or bad, can't see
 - killing sequence of enemies in correct order (shown to user)
 - type words (scribblenauts style)
 - key commands? (konami code will do things)
 - click on things?
 - directed typing?
 - navigate a maze using same buttons? (that could be fun xD)
 - rhythm game?
 - voluntary, pay points to unlock
### Ok, here's the plan:
 - At the bottom of the screen is a down counter and a character prompt.
 - Type the character to add to the counter.
 - Completing a page in the counter upgrades your current powerup.
 - The higher your page, the faster the counter decreases

## Rounds
### First round:
 - standard space invaders, basically as received
 - base powerups: 

### Second round:
 - larger, slower, 2-hit space invaders.
 - multiple waves, starting offscreen
 - unlock back-in-time powerup

### Third round:
 - lots of small space invaders
 - shoot at player
 - unlock shield powerup

### Fourth round:
 - Final Boss!!!
 - What attacks?
#### Final boss attacks
 - shoot at player
 - spawn shield of invaders
 - laser (rainbow barf?)

