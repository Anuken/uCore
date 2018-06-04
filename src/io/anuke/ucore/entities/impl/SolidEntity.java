package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.entities.component.SolidTrait;

public abstract class SolidEntity extends BaseEntity implements SolidTrait {
	private transient Vector2 lastPosition = new Vector2(Float.NaN, Float.NaN);

	public transient Rectangle hitbox = new Rectangle(), hitboxTile = new Rectangle();

	@Override
	public Vector2 lastPosition(){
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
}
