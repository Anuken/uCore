package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import io.anuke.ucore.entities.EntityPhysics;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public interface SolidTrait extends QuadTreeObject, PosTrait, MutPosTrait, RotationTrait, Entity{

    /** Last position is a Vector3 to support rotation. Z component is rotation. */
    Vector3 lastPosition();

    long lastUpdated();

    void setLastUpdated(long l);

    long updateSpacing();

    void setUpdateSpacing(long l);

    default void move(float x, float y){
        EntityPhysics.collisions().move(this, x, y);
    }

    /** Sets up hitbox data. */
    default void getHitbox(Rectangle rectangle){
        rectangle.setSize(0f).setCenter(getX(), getY());
    }

    /** Sets up tile hitbox data. */
    default void getHitboxTile(Rectangle rectangle){
        rectangle.setSize(0f).setCenter(getX(), getY());
    }

    default boolean collides(SolidTrait other){
        return true;
    }

    default void collision(SolidTrait other, float x, float y){
    }

    //no-op implementations of rotation accessors

    default float getRotation(){
        return 0;
    }

    default void setRotation(float rotation){

    }
}
