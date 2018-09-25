package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.math.Vector3;
import io.anuke.ucore.entities.trait.SolidTrait;

public abstract class SolidEntity extends BaseEntity implements SolidTrait{
    private transient Vector3 lastPosition = new Vector3(Float.NaN, Float.NaN, Float.NaN);
    private transient long lastUpdated = 0, updateSpacing;

    @Override
    public Vector3 lastPosition(){
        return lastPosition;
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
