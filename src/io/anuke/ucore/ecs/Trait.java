package io.anuke.ucore.ecs;

public abstract class Trait{
	
	//event methods
	public void update(Spark spark){}
	public void init(Spark spark){}
	public void removed(Spark spark){}
	
	/**Register type-specific events. This not called for every Trait instance! Use it as you would a static method.*/
	public void registerEvents(Prototype type){}
}
