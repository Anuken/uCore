package io.anuke.ucore.drawsort;

import com.badlogic.gdx.utils.Array;

public class DrawPointerList{
	public Array<DrawPointer> list = new Array<DrawPointer>();
	
	public void add(float layer, Drawer draw){
		DrawPointer p = DrawHandler.obtain();
		p.layer = layer;
		p.drawable = draw;
		p.add();
		list.add(p);
	}
	
	public void add(float layer, DrawLayer dl, Drawer draw){
		DrawPointer p = DrawHandler.obtain();
		p.layer = layer;
		p.sorter = dl;
		p.drawable = draw;
		p.add();
		list.add(p);
	}
	
	public void free(){
		for(DrawPointer p : list)
			DrawHandler.instance().remove(p);
		list.clear();
	}
}
