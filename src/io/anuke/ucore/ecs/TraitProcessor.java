package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;

public abstract class TraitProcessor extends Processor{
	private Class<? extends Trait>[] traits;
	
	public TraitProcessor(Class<? extends Trait>... traits){
		this.traits = traits;
	}

	@Override
	public void update(Array<Spark> sparks){
		for(Spark s : sparks){
			if(traits.length == 0){
				update(s);
			}else{
				for(int i = 0; i < traits.length; i ++){
					if(s.has(traits[i])){
						update(s);
						break;
					}
				}
			}
		}
	}
	
	public abstract void update(Spark spark);

}
