package io.anuke.ucore.entities.trait;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.util.Mathf;

public interface PosTrait{
    float getX();
    float getY();

    default float angleTo(PosTrait other){
        return Mathf.atan2(other.getX() - getX(), other.getY() - getY());
    }

    default float angleTo(PosTrait other, float yoffset){
        return Mathf.atan2(other.getX() - getX(), other.getY() - (getY() + yoffset));
    }

    default float angleTo(float ox, float oy){
        return Mathf.atan2(ox - getX(), oy - getY());
    }

    default float angleTo(PosTrait other, float xoffset, float yoffset){
        return Mathf.atan2(other.getX() - (getX() + xoffset), other.getY() - (getY() + yoffset));
    }

    default float distanceTo(PosTrait other){
        return Vector2.dst(other.getX(), other.getY(), getX(), getY());
    }

    default float distanceTo(float ox, float oy){
        return Vector2.dst(ox, oy, getX(), getY());
    }
}
