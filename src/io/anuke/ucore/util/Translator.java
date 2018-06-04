package io.anuke.ucore.util;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.entities.trait.PosTrait;

public class Translator extends Vector2 implements PosTrait {

    public Translator(){}

    public Translator(float x, float y){
        super(x, y);
    }

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

    public Translator set(PosTrait p){
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
