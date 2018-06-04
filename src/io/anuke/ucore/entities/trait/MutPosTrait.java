package io.anuke.ucore.entities.trait;

public interface MutPosTrait extends PosTrait {

    void setX(float x);
    void setY(float y);

    default void moveBy(float x, float y){
        setX(getX() + x);
        setY(getY() + y);
    }

    default void set(float x, float y){
        setX(x);
        setY(y);
    }
}
