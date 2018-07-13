package io.anuke.ucore.entities.trait;

import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Mathf;

public interface TimeTrait extends ScaleTrait, Entity{

    float lifetime();

    void time(float time);

    float time();

    default void updateTime(){
        time(Mathf.clamp(time() + Timers.delta(), 0, lifetime()));

        if(time() >= lifetime()){
            remove();
        }
    }

    @Override
    default float fin(){
        return time() / lifetime();
    }
}
