package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import io.anuke.ucore.entities.trait.SolidTrait;

public abstract class SolidEntity extends BaseEntity implements SolidTrait{
    public transient Rectangle hitbox = new Rectangle(), hitboxTile = new Rectangle();
    private transient Vector3 lastPosition = new Vector3(Float.NaN, Float.NaN, Float.NaN);
    private transient long lastUpdated = 0, updateSpacing;

    @Override
    public Vector3 lastPosition(){
        return lastPosition;
    }

    @Override
    public void getHitbox(Rectangle rectangle){
        rectangle.set(hitbox).setCenter(x + hitbox.x, y + hitbox.y);
    }

    @Override
    public void getHitboxTile(Rectangle rectangle){
        rectangle.set(hitboxTile).setCenter(x + hitboxTile.x, y + hitboxTile.y);
    }

    @Override
    public long lastUpdated(){
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(long l){
        this.lastUpdated = l;
    }

    @Override
    public long updateSpacing(){
        return updateSpacing;
    }

    @Override
    public void setUpdateSpacing(long l){
        this.updateSpacing = l;
    }


}
