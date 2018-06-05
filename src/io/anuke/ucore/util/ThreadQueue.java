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
}
