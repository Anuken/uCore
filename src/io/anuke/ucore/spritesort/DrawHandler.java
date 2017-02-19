package io.anuke.ucore.spritesort;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class DrawHandler{
	private static DrawHandler instance;
	
	private Array<DrawPointer> pointers = new Array<DrawPointer>();
	private PointerDrawer drawer = new PointerDrawer(){
		public void draw(Array<DrawPointer> pointers){
			for(DrawPointer pointer : pointers){
				pointer.drawable.draw(pointer);
			}
		}
	};
	
	public static DrawPointer obtain(){
		return Pools.obtain(DrawPointer.class);
	}
	
	public static DrawHandler instance(){
		if(instance == null) instance = new DrawHandler();
		return instance;
	}
	
	public void add(DrawPointer pointer){
		pointers.add(pointer);
		requestSort();
	}
	
	public void remove(DrawPointer pointer){
		pointers.removeValue(pointer, true);
		Pools.free(pointer);
	}
	
	public void requestSort(){
		pointers.sort();
	}
	
	public void draw(){
		//requestSort();
		drawer.draw(pointers);
	}
	
	public void setDrawer(PointerDrawer draw){
		this.drawer = draw;
	}
	
	public static interface PointerDrawer{
		public void draw(Array<DrawPointer> pointers);
	}
}
