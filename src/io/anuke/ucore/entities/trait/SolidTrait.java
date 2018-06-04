package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.entities.EntityPhysics;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public interface SolidTrait extends QuadTreeObject, PosTrait, MutPosTrait, Entity {

    Vector2 lastPosition();

    default void move(float x, float y){
        EntityPhysics.collisions().move(this, x, y);
    }

    /**Sets up hitbox data.*/
    default void getHitbox(Rectangle rectangle){
        rectangle.setSize(0f).setCenter(getX(), getY());
    }

    /**Sets up tile hitbox data.*/
    default void getHitboxTile(Rectangle rectangle){
        rectangle.setSize(0f).setCenter(getX(), getY());
    }

    default boolean collides(SolidTrait other){
        return true;
    }

    default void collision(SolidTrait other, float x, float y){}

    default boolean collidesOthers(){
        return false;
    }
}
