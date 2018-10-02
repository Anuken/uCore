package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.entities.trait.SolidTrait;
import io.anuke.ucore.util.Translator;

public abstract class SolidEntity extends BaseEntity implements SolidTrait{
    protected transient Vector2 velocity = new Translator(0f, 0.0001f);

    @Override
    public Vector2 getVelocity(){
        return velocity;
    }
}
