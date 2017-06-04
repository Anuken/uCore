package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;

import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public abstract class SolidEntity extends Entity implements QuadTreeObject{
	public float hitsize = 10;
	float tilehitwidth = 4, tilehitheight = 4, tilehoffsetx, tilehoffsety;
	
	public void move(float x, float y){
		Entities.moveTiled(this, tilehitwidth, tilehitheight, x, y);
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
