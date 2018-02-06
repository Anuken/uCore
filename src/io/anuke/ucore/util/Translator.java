package io.anuke.ucore.util;

import com.badlogic.gdx.math.Vector2;

public class Translator {
    private final Vector2 vec = new Vector2();
    public float x, y;

    public Translator trns(float angle, float amount){
        if(amount < 0) angle += 180f;
        vec.set(amount, 0).rotate(angle);
        this.x = vec.x;
        this.y = vec.y;

        return this;
    }

    public Translator trns(float angle, float x, float y){
        vec.set(x, y).rotate(angle);
        this.x = vec.x;
        this.y = vec.y;

        return this;
    }

    public Translator rnd(float length){
        vec.setToRandomDirection().scl(length);
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

}
