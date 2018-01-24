package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;

import java.util.Iterator;

//TODO change to a hashmap?
public class TraitList implements Iterable<Trait>{
	private DelayedRemovalArray<Trait> traits;
	
	public TraitList(Trait... array){
		traits = new DelayedRemovalArray<>(array);
	}
	
	public Array<Trait> asArray(){
		return traits;
	}
	
	public TraitList override(Class<? extends Trait> type, Trait replace){
		for(int i = 0; i < traits.size; i ++){
			if(traits.get(i).getClass() == type){
				traits.set(i, replace);
			}
		}
		return this;
	}
	
	public TraitList with(Trait... array){
		for(Trait t : array)
			traits.add(t);
		return this;
	}
	
	public TraitList exclude(Class<? extends Trait>...classes){
		traits.begin();
		for(Class<? extends Trait> c : classes){
			for(Trait t : traits){
				if(t.getClass() == c)
					traits.removeValue(t, true);
			}
		}
		traits.end();
		return this;
	}
	
	public <T extends Trait> T get(Class<T> type){
		for(Trait t : traits){
			if(t.getClass() == type)
				return (T)t;
		}
		
		return null;
	}
	
	public boolean contains(Class<? extends Trait> type){
		for(int i = 0; i < traits.size; i ++){
			if(traits.get(i).getClass() == type)
				return true;
		}
		return false;
	}

	@Override
	public Iterator<Trait> iterator(){
		return traits.iterator();
	}
}
