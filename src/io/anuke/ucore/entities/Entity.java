package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity{
	private static long lastid;
	public static SpriteBatch batch;
	public static float delta;
	
	public final long id;
	public float x,y;
	
	public abstract void update();
	public abstract void draw();
	public void init(){}
	
	public Entity(){
		id = lastid++;
		init();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> T add(){
		EntityController.instance().entitiesToAdd.add(this);
		return (T)this;
	}
	
	public Entity remove(){
		EntityController.instance().entitiesToRemove.add(id);
		return this;
	}
}
