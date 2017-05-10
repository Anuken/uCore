package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity{
	private static long lastid;
	protected static Vector2 vector = new Vector2();
	public static float delta;
	
	public final long id;
	public float x,y;
	
	public void update(){}
	public void draw(){}
	public void drawOver(){}
	public void removed(){}
	public void added(){}
	public void init(){}
	
	public Entity(){
		id = lastid++;
		init();
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
	
	public void move(float x, float y, float hitsize){
		Entities.moveTiled(this, hitsize, hitsize, x, y);
	}
	
	public float angleTo(Entity other){
		return vector.set(other.x - x, other.y - y).angle();
	}
	
	public float distanceTo(Entity other){
		return Vector2.dst(other.x, other.y, x, y);
	}
	
	public float drawSize(){
		return 20;
	}
	
	public Entity remove(){
		Entities.entitiesToRemove.add(id);
		removed();
		return this;
	}
	
	public String toString(){
		return getClass() + " " + id;
	}
}
