package org.newdawn.spaceinvaders;

/**
 * An EntityGroup is a Group of collision objects.
 *
 * EntityGroups can be used to speed up collision detection,
 * by only checking collisions between certain groups.
 */
public class EntityGroup<T extends Entity> extends Group<T> {
    public void checkCollisions(EntityGroup<? extends Entity> other) {
        if (other == this) {
            checkSelfCollisions();
        } else {
            for (Entity me : entities) {
                for (Entity him : other.entities) {
                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            }
        }
    }
    public void checkSelfCollisions() {
        for (int p = 0; p < entities.size(); p++) {
            for (int s = p + 1; s < entities.size(); s++) {
                Entity me = entities.get(p);
                Entity him = entities.get(s);

                if (me.collidesWith(him)) {
                    me.collidedWith(him);
                    him.collidedWith(me);
                }
            }
        }
    }
}
