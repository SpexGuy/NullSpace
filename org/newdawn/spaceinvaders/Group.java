package org.newdawn.spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by martin on 11/10/15.
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
}
