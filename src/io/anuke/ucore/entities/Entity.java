package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity{
	private static int lastid;
	//TODO remove?
	protected static Vector2 vector = new Vector2();
	
	protected transient EntityGroup<?> group;
	
	public final int id;
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
	
	public <T extends Entity> T add(EntityGroup group){
		this.group = group;
		group.add(this);
		return (T)this;
	}
	
	public <T extends Entity> T add(){
		return (T) add(Entities.getGroup(Entity.class));
	}
	
	public Entity remove(){
		if(group != null)
			((EntityGroup)group).remove(this);
		removed();
		return this;
	}
	
	public float angleTo(Entity other){
		return vector.set(other.x - x, other.y - y).angle();
	}
	
	public float angleTo(Entity other, float yoffset){
		return vector.set(other.x - x, other.y - (y+yoffset)).angle();
	}
	
	public float angleTo(float ox, float oy){
		return vector.set(ox - x, oy - y).angle();
	}
	
	public float angleTo(Entity other, float xoffset, float yoffset){
		return vector.set(other.x - (x+xoffset), other.y - (y+yoffset)).angle();
	}
	
	public float distanceTo(Entity other){
		return Vector2.dst(other.x, other.y, x, y);
	}
	
	public float distanceTo(float ox, float oy){
		return Vector2.dst(ox, oy, x, y);
	}
	
	public float drawSize(){
		return 20;
	}
	
	public String toString(){
		return getClass() + " " + id;
	}
}
