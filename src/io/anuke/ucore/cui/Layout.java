package io.anuke.ucore.cui;

public abstract class Layout extends Section{
	
	public abstract void layout();
	
	@Override
	public void update(){
		//TODO only layout when needed
		layout();
		
		super.update();
	}
}
