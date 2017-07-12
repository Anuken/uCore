package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.TraitProcessor;

public class DrawProcessor extends TraitProcessor{
	
	public DrawProcessor(){
		super(DrawTrait.class);
	}

	@Override
	public void update(Spark spark){
		spark.get(DrawTrait.class).drawer.accept(spark);
	}
}
