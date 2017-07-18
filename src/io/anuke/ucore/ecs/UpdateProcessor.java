package io.anuke.ucore.ecs;

public class UpdateProcessor extends TraitProcessor{

	@Override
	public void update(Spark spark){
		for(int i = 0; i < spark.getTraits().size; i++){
			spark.getTraits().get(i).update(spark);
		}
	}

}
