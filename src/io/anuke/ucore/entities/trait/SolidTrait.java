package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.entities.EntityQuery;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public interface SolidTrait extends QuadTreeObject, PosTrait, MoveTrait, VelocityTrait, Entity{

    void getHitbox(Rectangle rectangle);

    void getHitboxTile(Rectangle rectangle);

    Vector2 lastPosition();

    default float getDeltaX(){
        return getX() - lastPosition().x;
    }

    default float getDeltaY(){
        return getY() - lastPosition().y;
    }

    default boolean movable(){
        return false;
    }

    default boolean collides(SolidTrait other){
        return true;
    }

    default void collision(SolidTrait other, float x, float y){}

    default void move(float x, float y){
        EntityQuery.collisions().move(this, x, y);
    }
}
