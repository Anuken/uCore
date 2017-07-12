package io.anuke.ucore.ecs;

public abstract class Trait<T extends Trait>{
	
	//event methods
	public void update(Spark spark){}
	public void init(Spark spark){}
	public void removed(Spark spark){}
	
	public abstract T copy();
	
}
