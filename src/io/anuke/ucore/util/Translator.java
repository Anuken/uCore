package io.anuke.ucore.util;

import com.badlogic.gdx.math.Vector2;

public class Translator extends Vector2 implements Position{

    public Translator trns(float angle, float amount){
        set(amount, 0).rotate(angle);

        return this;
    }

    public Translator trns(float angle, float x, float y){
        set(x, y).rotate(angle);

        return this;
    }

    public Translator rnd(float length){
        setToRandomDirection().scl(length);
        return this;
    }

    public Translator set(Position p){
        set(p.getX(), p.getY());
        return this;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
