package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;
import io.anuke.ucore.util.Tmp;

public abstract class SolidEntity extends Entity implements QuadTreeObject{
	public transient Hitbox hitbox = new Hitbox(10f);
	public transient Hitbox hitboxTile = new Hitbox(4f);
	
	public Vector2 move(float x, float y){
		Tmp.v3.set(this.x, this.y);
		vector.set(x, y);
		
		float segment = 2f;
		
		if(vector.len() > segment){
			Tmp.v3.set(vector).setLength(segment);
			while(vector.len() > segment){
				Entities.moveTiled(this, hitboxTile, Tmp.v3.x, Tmp.v3.y);
				vector.setLength(vector.len()-segment);
			}
			Entities.moveTiled(this, hitboxTile, vector.x, vector.y);
		}else{
			Entities.moveTiled(this, hitboxTile, x, y);
		}

		return Tmp.v3.sub(this.x, this.y);
	}
	
	public boolean collidesTile(){
		return Entities.overlapsTile(hitbox.getRect(x, y));
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
