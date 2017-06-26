package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public abstract class SolidEntity extends Entity implements QuadTreeObject{
	private static Vector2 mvector = new Vector2();
	public float hitsize = 10, hitoffsetx, hitoffsety;
	float tilehitwidth = 4, tilehitheight = 4, tilehoffsetx, tilehoffsety;
	
	public void move(float x, float y){
		vector.set(x, y);
		
		float segment = 2f;
		
		if(vector.len() > segment){
			mvector.set(vector).setLength(segment);
			while(vector.len() > segment){
				Entities.moveTiled(this, tilehitwidth, tilehitheight, mvector.x, mvector.y);
				vector.setLength(vector.len()-segment);
			}
			Entities.moveTiled(this, tilehitwidth, tilehitheight, vector.x, vector.y);
		}else{
			Entities.moveTiled(this, tilehitwidth, tilehitheight, x, y);
		}
	}
	
	public void move(float x, float y, float hitsize){
		Entities.moveTiled(this, hitsize, hitsize, x, y);
	}
	
	public boolean collidesTile(){
		return Entities.overlapsTile(Rectangle.tmp.setSize(tilehitwidth, tilehitheight).setCenter(x, y));
	}
	
	public boolean collides(SolidEntity other){
		return true;
	}
	
	public void collision(SolidEntity other){}

	@Override
	public void getBoundingBox(Rectangle out){
		out.setSize(hitsize);
		out.setCenter(x, y);
	}
}
