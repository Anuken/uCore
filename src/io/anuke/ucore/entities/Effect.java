package io.anuke.ucore.entities;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

public class Effect extends TimedEntity{
	static ObjectMap<String, EffectDraw> draws = new ObjectMap<>();
	
	public Consumer<Effect> renderer;
	public Color color = Color.WHITE;
	
	public static void addDraw(String name, float life, Consumer<Effect> cons){
		draws.put(name, new EffectDraw(cons, life));
	}
	
	public Effect(float lifetime, Consumer<Effect> rend){
		renderer = rend;
		this.lifetime = lifetime;
	}
	
	public Effect(String name){
		if(!draws.containsKey(name)) throw new IllegalArgumentException("The effect draw call " + name + " does not exist!");
		renderer = draws.get(name).draw;
		lifetime = draws.get(name).lifetime;
	}
	
	@Override
	public void draw(){
		renderer.accept(this);
	}
	
	static class EffectDraw{
		Consumer<Effect> draw;
		float lifetime;
		
		public EffectDraw(Consumer<Effect> draw, float life){
			this.lifetime = life;
			this.draw = draw;
		}
	}
}
