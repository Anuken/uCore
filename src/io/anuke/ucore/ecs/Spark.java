package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.ecs.extend.traits.PosTrait;

public class Spark{
	private static int lastid;
	
	private int id;
	private Basis basis;
	
	private final Prototype type;
	private Array<Trait> traitlist;
	private ObjectMap<Class<? extends Trait>, Trait> traitmap = new ObjectMap<>();
	
	//cached pos trait, since it's used so much anyway
	private PosTrait pos;
	
	public Spark(Prototype type){
		this.type = type;
		
		id = lastid++;
		
		TraitList list = type.traits();
		
		traitlist = list.asArray();
		for(Trait t : traitlist){
			t.init(this);
			traitmap.put(t.getClass(), t);
		}
	}
	
	public <T extends Trait> boolean has(Class<T> c){
		return traitmap.containsKey(c);
	}
	
	public <T extends Trait> T get(Class<T> c){
		T t = (T)traitmap.get(c);
		if(t == null) throw new IllegalArgumentException("Trait type not found in spark: \"" + c + "\"");
		return t;
	}
	
	public Array<Trait> getTraits(){
		return traitlist;
	}
	
	public Spark add(){
		Basis.instance().addSpark(this);
		return this;
	}
	
	public void remove(){
		basis.removeSpark(this);
	}
	
	public int getID(){
		return id;
	}
	
	protected void setBasis(Basis basis){
		this.basis = basis;
	}
	
	public Prototype getType(){
		return type;
	}
	
	//shortcut trait methods...
	
	public PosTrait pos(){
		if(pos == null)
			pos = get(PosTrait.class);
		return pos;
	}
}
