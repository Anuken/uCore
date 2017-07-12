package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.ecs.extend.PosTrait;

public class Spark{
	private static long lastid;
	
	private long id;
	private Basis basis;
	
	private Array<Trait> traitlist;
	private ObjectMap<Class<? extends Trait>, Trait> traitmap = new ObjectMap<>();
	
	public Spark(Prototype type){
		id = lastid++;
		
		TraitList list = type.createTraits();
		
		traitlist = list.copyArray();
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
	
	public long getID(){
		return id;
	}
	
	protected void setBasis(Basis basis){
		this.basis = basis;
	}
	
	//shortcut trait methods...
	
	public PosTrait pos(){
		return get(PosTrait.class);
	}
}
