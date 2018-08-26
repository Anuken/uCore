package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ReflectionPool;
import io.anuke.ucore.function.Supplier;

/** A thread-safe wrapper for pools. */
public class Pooling{
    private static boolean reflectionless;

    public static synchronized void free(Object object){
        Pools.free(object);
    }

    public static synchronized <T> T obtain(Class<T> type){
        if(reflectionless && Pools.get(type) instanceof ReflectionPool){
            throw new IllegalArgumentException("Can't instantiate type with reflection: " + type);
        }
        return Pools.obtain(type);
    }

    public static void setReflectionless(boolean on){
        reflectionless = on;
    }

    public static synchronized <T> void registerType(Class<T> type, Supplier<T> constructor){
        Pools.set(type, new Pool<T>(){
            @Override
            protected T newObject(){
                return constructor.get();
            }
        });
    }

}
