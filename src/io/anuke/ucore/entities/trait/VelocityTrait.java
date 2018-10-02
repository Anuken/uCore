package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.core.Timers;

public interface VelocityTrait extends MoveTrait{

    Vector2 getVelocity();

    default void applyImpulse(float x, float y){
        getVelocity().x += x / getMass();
        getVelocity().y += y / getMass();
    }

    default float getMaxVelocity(){
        return Float.MAX_VALUE;
    }

    default float getMass(){
        return 1f;
    }

    default float getDrag(){
        return 0f;
    }

    default void updateVelocity(){
        getVelocity().scl(1f - getDrag() * Timers.delta());

        if(this instanceof SolidTrait){
            ((SolidTrait) this).move(getVelocity().x * Timers.delta(), getVelocity().y * Timers.delta());
        }else{
            moveBy(getVelocity().x * Timers.delta(), getVelocity().y * Timers.delta());
        }
    }
}
