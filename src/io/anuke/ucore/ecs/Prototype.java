package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;

public abstract class Prototype{
	private static int lastid;
	private static Array<Prototype> types = new Array<>();
	
	private final int id;
	private TraitList list;
	
	public Prototype(){
		id = lastid++;
		types.add(this);
	}
	
	public abstract TraitList traits();
	
	public TraitList createTraits(){
		if(list == null)
			list = traits();
		
		return list;
	}
	
	public int getTypeID(){
		return id;
	}
	
	public static Array<Prototype> getAllTypes(){
		return types;
	}
}
