package io.anuke.ucore.util;

import com.badlogic.gdx.math.Vector2;

public interface Position {
    float getX();
    float getY();

    public default float angleTo(Position other){
        return Mathf.atan2(other.getX() - getX(), other.getY() - getY());
    }

    public default float angleTo(Position other, float yoffset){
        return Mathf.atan2(other.getX() - getX(), other.getY() - (getY()+yoffset));
    }

    public default float angleTo(float ox, float oy){
        return Mathf.atan2(ox - getX(), oy - getY());
    }

    public default float angleTo(Position other, float xoffset, float yoffset){
        return Mathf.atan2(other.getX() - (getX()+xoffset), other.getY() - (getY()+yoffset));
    }

    public default float distanceTo(Position other){
        return Vector2.dst(other.getX(), other.getY(), getX(), getY());
    }

    public default float distanceTo(float ox, float oy){
        return Vector2.dst(ox, oy, getX(), getY());
    }
}
