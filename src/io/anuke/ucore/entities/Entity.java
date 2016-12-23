package io.anuke.ucore.entities;

public abstract class Entity{
	private static long lastid;
	public static float delta;
	
	public final long id;
	public float x,y;
	
	public void update(){}
	public void draw(){}
	public void removed(){}
	public void init(){}
	
	public Entity(){
		id = lastid++;
		init();
	}
	
	public <T extends Entity> T add(){
		EntityHandler.instance().entitiesToAdd.add(this);
		return (T)this;
	}
	
	public Entity remove(){
		EntityHandler.instance().entitiesToRemove.add(id);
		removed();
		return this;
	}
}
