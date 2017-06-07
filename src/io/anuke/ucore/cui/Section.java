package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public abstract class Section{
	private Array<String> styles = new Array<String>();
	protected Style style = new Style();
	protected float x, y, width, height;
	
	public void draw(){
		
	}
	
	public void update(){
		
	}
	
	public String getTypeName(){
		return ClassReflection.getSimpleName(getClass());
	}
	
	public void addStyle(String newstyle){
		styles.add(newstyle);
		Canvas.instance().getStylesheet().getStyle(style, getTypeName(), styles);
	}
}
