package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity{
	private static long lastid;
	public final long id;
	public static SpriteBatch batch;
	public static float delta;
	
	public abstract void update();
	public abstract void draw();
	public void init(){}
	
	public Entity(){
		id = lastid++;
		init();
	}
	
	public Entity add(){
		EntityController.instance().entitiesToAdd.add(this);
		return this;
	}
	
	public Entity remove(){
		EntityController.instance().entitiesToRemove.add(id);
		return this;
	}
}
