package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class SafeArray<T> extends Array<T>{
    @Override
    public Iterator<T> iterator(){
        return new ArrayIterator<>(this);
    }
}
