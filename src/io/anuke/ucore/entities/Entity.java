package io.anuke.ucore.entities;

import com.badlogic.gdx.Gdx;

public abstract class Entity{
	private static long lastid;
	public static float delta;
	
	public final long id;
	public float x,y;
	
	public void update(){}
	public void draw(){}
	public void removed(){}
	public void added(){}
	public void init(){}
	
	public Entity(){
		id = lastid++;
		init();
	}
	
	protected float delta(){
		return Gdx.graphics.getDeltaTime()*60;
	}
	
	public <T extends Entity> T set(float x, float y){
		this.x = x;
		this.y = y;
		return (T)this;
	}
	
	public <T extends Entity> T add(){
		Entities.entitiesToAdd.add(this);
		return (T)this;
	}
	
	public Entity remove(){
		Entities.entitiesToRemove.add(id);
		removed();
		return this;
	}
}
