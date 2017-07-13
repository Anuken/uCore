package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.function.Consumer;

public class DrawTrait extends Trait{
	public Consumer<Spark> drawer;
	
	public DrawTrait(Consumer<Spark> drawer){
		this.drawer = drawer;
	}
	
	//no-arg constructor for things like Kryo/JSON
	private DrawTrait(){}
}
