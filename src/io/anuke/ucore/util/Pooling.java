package io.anuke.ucore.util;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import io.anuke.ucore.function.Supplier;

/** A thread-safe wrapper for Pooling. */
public class Pooling{
    static private final int max = 100;
    static private final ObjectMap<Class, Pool> typePools = new ObjectMap();

    public static void free (Object object) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null.");
        Pool pool = typePools.get(object.getClass());
        if (pool == null) return; // Ignore freeing an object that was never retained.
        pool.free(object);
    }

    public static synchronized <T> T obtain(Class<T> type, Supplier<T> sup){
        return get(type, sup).obtain();
    }

    public static <T> Pool<T> get(Class<T> type, Supplier<T> cons){
        Pool<T> pool = typePools.get(type);
        if (pool == null) {
            pool = new Pool<T>(4, max){
                @Override
                protected T newObject(){
                    return cons.get();
                }
            };
            typePools.put(type, pool);
        }
        return pool;
    }

}
