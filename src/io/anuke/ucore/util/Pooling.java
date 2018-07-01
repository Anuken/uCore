package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Pools;

/**A thread-safe wrapper for pools.*/
public class Pooling {

    static synchronized public void free (Object object) {
        Pools.free(object);
    }

    static synchronized public <T> T obtain (Class<T> type){
        return Pools.obtain(type);
    }

}
