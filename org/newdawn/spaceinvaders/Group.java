package org.newdawn.spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Group is an iterable object which offers safe add and remove* (via add() and remove()) during iteration.
 *
 * *items are not actually added or removed until completeFrame() is called.
 * ** completeFrame() cannot be called during iteration.
 */
public class Group<T> implements Iterable<T> {
    protected List<T> entities = new ArrayList<>();
    protected List<T> toRemove = new ArrayList<>();
    protected List<T> toAdd = new ArrayList<>();

    public void add(T entity) {
        toAdd.add(entity);
    }

    public void remove(T entity) {
        toRemove.add(entity);
    }

    public void completeFrame() {
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
        toRemove.clear();
        toAdd.clear();
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
        toAdd.clear();
    }

    public T getRandom() {
        return entities.get((int)(Math.random() * entities.size()));
    }
}
