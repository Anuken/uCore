package io.anuke.ucore.entities;

import io.anuke.ucore.util.Position;

public abstract class Entity implements Position{
	private static int lastid;
	
	protected transient EntityGroup<?> group;

	/**Do not modify. Used for network operations.*/
	public int id;
	public float x,y;
	
	public void update(){}
	public void draw(){}
	public void removed(){}
	public void added(){}
	
	public Entity(){
		id = lastid++;
	}
	
	public <T extends Entity> T set(float x, float y){
		this.x = x;
		this.y = y;
		return (T)this;
	}
	
	public <T extends Entity> T add(EntityGroup group){
		group.add(this);
		return (T)this;
	}
	
	public <T extends Entity> T add(){
		return (T) add(Entities.defaultGroup());
	}
	
	public Entity remove(){
		if(group != null)
			((EntityGroup)group).remove(this);
		group = null;
		return this;
	}

	public EntityGroup<?> getGroup() {
		return group;
	}
	
	public float drawSize(){
		return 20;
	}
	
	public boolean isAdded(){
		return group != null;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public String toString(){
		return getClass() + " " + id;
	}
}
