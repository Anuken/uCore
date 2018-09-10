package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ReflectionPool;
import io.anuke.ucore.function.Supplier;

/** A thread-safe wrapper for Pooling. */
public class Pooling{
    public static synchronized void free(Object object){
        Pools.free(object);
    }

    public static synchronized <T> T obtain(Class<T> type, Supplier<T> sup){
        if(Pools.get(type) instanceof ReflectionPool){
            registerType(type, sup);
        }
        return Pools.obtain(type);
    }

    private static synchronized <T> void registerType(Class<T> type, Supplier<T> constructor){
        Pools.set(type, new Pool<T>(){
            @Override
            protected T newObject(){
                return constructor.get();
            }
        });
    }

}
