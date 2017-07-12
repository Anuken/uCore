package io.anuke.ucore.ecs;

public class UpdateProcessor extends TraitProcessor{

	@Override
	public void update(Spark spark){
		for(Trait trait : spark.getTraits()){
			trait.update(spark);
		}
	}

}
