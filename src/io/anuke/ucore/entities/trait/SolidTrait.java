package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.entities.EntityQuery;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public interface SolidTrait extends QuadTreeObject, PosTrait, MoveTrait, VelocityTrait, Entity{
    void getHitbox(Rectangle rectangle);

    void getHitboxTile(Rectangle rectangle);

    default boolean collides(SolidTrait other){
        return true;
    }

    default void collision(SolidTrait other, float x, float y){}

    default void move(float x, float y){
        EntityQuery.collisions().move(this, x, y);
    }
}
