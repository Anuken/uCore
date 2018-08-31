package io.anuke.ucore.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Event;

public class Events{
    private static ObjectMap<Class<? extends Event>, Array<Consumer<? extends Event>>> events = new ObjectMap<>();

    public static <T extends Event> void on(Class<T> type, Consumer<T> listener){
        if(events.get(type) == null)
            events.put(type, new Array<>());

        events.get(type).add(listener);
    }

    public static <T extends Event> void fire(T type){
        if(events.get(type.getClass()) == null)
            return;

        for(Consumer<? extends Event> event : events.get(type.getClass())){
            ((Consumer<T>)event).accept(type);
        }
    }

}
