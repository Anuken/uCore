package io.anuke.ucore.facet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class FacetMap{
	public ObjectMap<String, Facet> map = new ObjectMap<String, Facet>();
	
	public Iterable<? extends Facet> list(){
		return map.values();
	}
	
	public Facet first(){
		Color color = map.values().next().sprite().sprite.getColor();
		if(color.r + color.g + color.b <= 0.0001f){
			Values<Facet> val = map.values().iterator();
			val.next();
			return val.next();
		}
		return map.values().next();
	}
	
	public Facet get(String name){
		return map.get(name);
	}
	
	public void add(String name, Facet renderable){
		renderable.add();
		map.put(name, renderable);
	}
	
	public void setPosition(float x, float y){
		for(Facet r : map.values())
			r.set(x, y);
	}
	
	public void free(){
		Facets.instance().remove(map.values());
	}
}
