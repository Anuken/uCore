package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;

import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public abstract class PhysicsEntity extends Entity implements QuadTreeObject{
	public float hitsize = 10;
	
	public abstract boolean collides(PhysicsEntity other);
	public void collision(PhysicsEntity other){}

	@Override
	public void getBoundingBox(Rectangle out){
		out.setSize(hitsize);
		out.setCenter(x, y);
	}
}
