package io.anuke.ucore.cui;

public abstract class Layout extends Section{
	
	public abstract void layout();
	
	@Override
	public void add(Section section){
		super.add(section);
		layout();
		updateSize();
	}
	
	@Override
	public void remove(Section section){
		super.remove(section);
		layout();
		updateSize();
	}
}
