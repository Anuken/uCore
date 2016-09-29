package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.utils.ObjectMap;

public class RenderableGroup{
	public ObjectMap<String, Renderable> map = new ObjectMap<String, Renderable>();
	
	public Iterable<? extends Renderable> list(){
		return map.values();
	}
	
	public Renderable get(String name){
		return map.get(name);
	}
	
	public void add(String name, Renderable renderable){
		renderable.add();
		map.put(name, renderable);
	}
	
	public void setPosition(float x, float y){
		for(Renderable r : map.values())
			r.setPosition(x, y);
	}
	
	public void free(){
		RenderableHandler.getInstance().remove(map.values());
	}
}
