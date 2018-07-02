package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Queue;

import java.util.Iterator;

public class ThreadQueue<T> extends Queue<T> {
    private ThreadLocal<QueueIterable<T>> threaditer = new ThreadLocal<>();

    @Override
    public Iterator<T> iterator() {
        if(threaditer.get() == null){
            threaditer.set(new QueueIterable<>(this));
        }
        return threaditer.get().iterator();
    }

    @Override
    public synchronized T first() {
        return super.first();
    }

    @Override
    public synchronized T last() {
        return super.last();
    }

    @Override
    public synchronized T removeFirst() {
        return super.removeFirst();
    }

    @Override
    public synchronized T removeLast() {
        return super.removeLast();
    }

    @Override
    public synchronized void addFirst(T object) {
        super.addFirst(object);
    }

    @Override
    public synchronized T removeIndex(int index) {
        return super.removeIndex(index);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized void addLast(T object) {
        super.addLast(object);
    }

    @Override
    protected synchronized void resize(int newSize) {
        super.resize(newSize);
    }

    @Override
    public synchronized int indexOf(T value, boolean identity) {
        return super.indexOf(value, identity);
    }

    @Override
    public synchronized boolean removeValue(T value, boolean identity) {
        return super.removeValue(value, identity);
    }

    @Override
    public synchronized T get(int index) {
        return super.get(index);
    }
}
