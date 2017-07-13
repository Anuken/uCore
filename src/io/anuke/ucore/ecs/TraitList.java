package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class TraitList{
	private DelayedRemovalArray<Trait> traits = new DelayedRemovalArray<>();
	
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
}
