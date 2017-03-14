package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class RenderableGroup{
	public ObjectMap<String, Renderable> map = new ObjectMap<String, Renderable>();
	
	public Iterable<? extends Renderable> list(){
		return map.values();
	}
	
	public Renderable first(){
		Color color = map.values().next().sprite().sprite.getColor();
		if(color.r + color.g + color.b <= 0.0001f){
			Values<Renderable> val = map.values().iterator();
			val.next();
			return val.next();
		}
		return map.values().next();
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
			r.set(x, y);
	}
	
	public void free(){
		RenderableHandler.instance().remove(map.values());
	}
}
