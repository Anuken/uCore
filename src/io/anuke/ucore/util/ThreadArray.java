package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ThreadArray<T> extends Array<T>{
    private ThreadLocal<ArrayIterable<T>> threaditer = new ThreadLocal<>();

    public ThreadArray(){

    }

    public ThreadArray(boolean ordered){
        super(ordered, 16);
    }

    public ThreadArray(int capacity){
        super(capacity);
    }

    @Override
    public Iterator<T> iterator(){
        if(threaditer.get() == null){
            threaditer.set(new ArrayIterable<>(this));
        }
        return threaditer.get().iterator();
    }
}
