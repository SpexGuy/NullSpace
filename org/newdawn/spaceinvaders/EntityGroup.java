package org.newdawn.spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by martin on 11/9/15.
 */
public class EntityGroup<T extends Entity> implements Iterable<T> {
    private List<T> entities = new ArrayList<>();
    private List<T> toRemove = new ArrayList<>();

    public void add(T entity) {
        entities.add(entity);
    }

    public void remove(T entity) {
        toRemove.add(entity);
    }

    public void checkCollisions(EntityGroup<? extends Entity> other) {
        assert(other != this); // use checkSelfCollisions instead for this
        for (Entity me : entities) {
            for (Entity him : other.entities) {
                if (me.collidesWith(him)) {
                    me.collidedWith(him);
                    him.collidedWith(me);
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

    public void completeFrame() {
        entities.removeAll(toRemove);
        toRemove.clear();
    }

    public int size() {
        return entities.size();
    }

    @Override
    public Iterator<T> iterator() {
        return entities.iterator();
    }

    public void clear() {
        entities.clear();
        toRemove.clear();
    }
}