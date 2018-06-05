package io.anuke.ucore.entities.impl;

import io.anuke.ucore.entities.EntityGroup;
import io.anuke.ucore.entities.trait.Entity;

public abstract class BaseEntity implements Entity {
	private static int lastid;
	
	protected transient EntityGroup group;

	/**Do not modify. Used for network operations and mapping.*/
	public int id;
	public float x,y;
	
	public BaseEntity(){
		id = lastid++;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void resetID(int id) {
		this.id = id;
	}

	@Override
	public EntityGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(EntityGroup group) {
		this.group = group;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
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
