package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.renderables.RenderableList;

public class RenderableTrait extends Trait{
	private Consumer<Spark> drawer;
	private RenderableList list = new RenderableList();
	
	public RenderableTrait(Consumer<Spark> drawer){
		this.drawer = drawer;
	}
	
	private RenderableTrait(){}

}
