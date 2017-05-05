package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;

import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public abstract class SolidEntity extends Entity implements QuadTreeObject{
	public float hitsize = 10;
	
	public void move(float x, float y){
		Entities.move(this, Entities.tilesize-2, x, y);
	}
	
	public void move(float x, float y, float hitsize){
		Entities.move(this, hitsize, x, y);
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
