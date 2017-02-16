package io.anuke.ucore.spritesort;

import com.badlogic.gdx.utils.Array;

public class DrawPointerList{
	public Array<DrawPointer> list = new Array<DrawPointer>();
	
	public void add(float layer, Drawable draw){
		list.add(new DrawPointer(draw, layer));
	}
	
	public void add(float layer, DrawLayer dl, Drawable draw){
		list.add(new DrawPointer(draw, dl, layer));
	}
	
	public void clear(){
		for(DrawPointer p : list)
			DrawHandler.instance().remove(p);
	}
}
