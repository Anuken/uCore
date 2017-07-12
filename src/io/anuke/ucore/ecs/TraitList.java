package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class TraitList{
	private DelayedRemovalArray<Trait> traits = new DelayedRemovalArray<>();
	
	public TraitList(Trait... array){
		traits = new DelayedRemovalArray<>(array);
	}
	
	public Array<Trait> copyArray(){
		Array<Trait> arr = new Array<Trait>(traits);
		for(int i = 0; i < arr.size; i ++){
			arr.set(i, arr.get(i).copy());
		}
		return arr;
	}
	
	public TraitList with(Trait... array){
		for(Trait t : array)
			traits.add(t);
		return this;
	}
	
	public TraitList exclude(Class<Trait>...classes){
		traits.begin();
		for(Class<Trait> c : classes){
			for(Trait t : traits){
				if(t.getClass() == c)
					traits.removeValue(t, true);
			}
		}
		traits.end();
		return this;
	}
}
