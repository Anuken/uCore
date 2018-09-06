package io.anuke.ucore.entities.impl;

import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;

/** Presumably you would extends BulletType and put in the default bullet entity type. */
public interface BaseBulletType<T extends BulletEntity>{
    /*
    public float lifetime = 100;
    public float speed = 1f;
    public float damage = 1;
    public float hitsize = 4;
    public float drawSize = 20f;
    public float drag = 0f;
    public boolean pierce;
    public Effect hiteffect = null, despawneffect = null;*/

    float drawSize();
    float lifetime();
    float speed();
    float damage();
    float hitSize();
    float drag();
    boolean pierce();
    Effect hitEffect();
    Effect despawnEffect();

    default void draw(T b){
    }

    default void init(T b){
    }

    default void update(T b){
    }

    default void hit(T b, float hitx, float hity){
        if(hitEffect() != null)
            Effects.effect(hitEffect(), b.x, b.y, b.angle());
    }

    default void hit(T b){
        hit(b, b.x, b.y);
    }

    default void despawned(T b){
        if(despawnEffect() != null)
            Effects.effect(despawnEffect(), b.x, b.y, b.angle());
    }
}
