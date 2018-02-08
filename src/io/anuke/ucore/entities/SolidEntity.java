package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public abstract class SolidEntity extends Entity implements QuadTreeObject{
	public transient Hitbox hitbox = new Hitbox(10f);
	public transient Hitbox hitboxTile = new Hitbox(4f);
	
	public void move(float x, float y){
		Entities.collisions().move(this, x, y);
	}
	
	public boolean collidesTile(){
		return Entities.collisions().overlapsTile(hitbox.getRect(x, y));
	}
	
	public boolean collides(SolidEntity other){
		return true;
	}
	
	public void collision(SolidEntity other){}

	@Override
	public void getBoundingBox(Rectangle out){
		hitbox.getRect(out, x, y);
	}
}
