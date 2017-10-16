package io.anuke.ucore.cui;

public abstract class Drawable{
	
	public abstract void draw(float x, float y, float width, float height, float scale);
	
	public void draw(float x, float y, float width, float height){
		draw(x, y, width, height, 1f);
	}
}
