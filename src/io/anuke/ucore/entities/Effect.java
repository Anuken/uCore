package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.EffectDraw;

public class Effect extends TimedEntity{
	public EffectDraw renderer;
	public Color color = Color.WHITE;
	public float rotation = 0f;
	
	public Effect(float lifetime, EffectDraw rend){
		renderer = rend;
		this.lifetime = lifetime;
	}
	
	public Effect(String name){
		renderer = Effects.getEffect(name);
		lifetime = Effects.getEffect(name).lifetime;
	}
	
	public Effect(String name, Color color, float rotation){
		this(name);
		this.color = color;
		this.rotation = rotation;
	}
	
	@Override
	public void drawOver(){
		Effects.renderEffect(id, renderer, color, time, rotation, x, y);
	}
	
}
