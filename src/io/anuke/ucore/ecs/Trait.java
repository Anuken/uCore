package io.anuke.ucore.ecs;

public abstract class Trait{
	
	//just event methods
	
	/**Called each frame, from the UpdateProcessor.*/
	public void update(Spark spark){}
	/**Called when the spark is created.*/
	public void init(Spark spark){}
	/**Called when the spark is added to the basis.*/
	public void added(Spark spark){}
	/**Called when the spark is removed from the basis.*/
	public void removed(Spark spark){}
	
	/**Register type-specific events. This not called for every Trait instance! Use it as you would a static method.*/
	public void registerEvents(Prototype type){}
}
