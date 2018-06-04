package io.anuke.ucore.util;

public interface MutablePosition extends Position{

    void setX(float x);
    void setY(float y);

    default void set(float x, float y){
        setX(x);
        setY(y);
    }
}
