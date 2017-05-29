package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

public class Effect extends TimedEntity{
	static ObjectMap<String, EffectDraw> draws = new ObjectMap<>();
	
	public EffectRenderer renderer;
	public Color color = Color.WHITE;
	
	public static void create(String name, float life, EffectRenderer cons){
		draws.put(name, new EffectDraw(cons, life));
	}
	
	public Effect(float lifetime, EffectRenderer rend){
		renderer = rend;
		this.lifetime = lifetime;
	}
	
	public Effect(String name){
		if(!draws.containsKey(name)) throw new IllegalArgumentException("The effect draw call \"" + name + "\" does not exist!");
		renderer = draws.get(name).draw;
		lifetime = draws.get(name).lifetime;
	}
	
	@Override
	public void drawOver(){
		renderer.render(this);
	}
	
	static class EffectDraw{
		EffectRenderer draw;
		float lifetime;
		
		public EffectDraw(EffectRenderer draw, float life){
			this.lifetime = life;
			this.draw = draw;
		}
	}
	
	static public interface EffectRenderer{
		public void render(Effect effect);
	}
}
