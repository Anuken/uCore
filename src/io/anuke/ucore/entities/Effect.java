package io.anuke.ucore.entities;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

public class Effect extends TimedEntity{
	static ObjectMap<String, Consumer<Effect>> draws = new ObjectMap<>();
	
	public Consumer<Effect> renderer;
	public Color color = Color.WHITE;
	
	public static void addDraw(String name, Consumer<Effect> cons){
		draws.put(name, cons);
	}
	
	public Effect(Consumer<Effect> rend){
		renderer = rend;
	}
	
	public Effect(String name){
		if(!draws.containsKey(name)) throw new IllegalArgumentException("The effect draw call " + name + " does not exist!");
		renderer = draws.get(name);
	}
	
	@Override
	public void draw(){
		renderer.accept(this);
	}
}
