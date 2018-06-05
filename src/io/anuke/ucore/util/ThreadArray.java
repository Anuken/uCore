package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ThreadArray<T> extends Array<T> {
    private ThreadLocal<ArrayIterable<T>> threaditer = new ThreadLocal<>();

    @Override
    public Iterator<T> iterator() {
        if(threaditer.get() == null){
            threaditer.set(new ArrayIterable<>(this));
        }
        return threaditer.get().iterator();
    }
}
