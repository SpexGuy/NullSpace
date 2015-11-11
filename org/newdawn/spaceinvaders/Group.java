package org.newdawn.spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Group is an iterable object which offers safe removal* (via remove()) during iteration.
 *
 * *items are not actually removed until completeFrame() is called.
 */
public class Group<T> implements Iterable<T> {
    protected List<T> entities = new ArrayList<>();
    protected List<T> toRemove = new ArrayList<>();

    public void add(T entity) {
        entities.add(entity);
    }

    public void remove(T entity) {
        toRemove.add(entity);
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

    public T getRandom() {
        return entities.get((int)(Math.random() * entities.size()));
    }
}
