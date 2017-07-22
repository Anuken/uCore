package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.entities.Effect;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.function.EffectProvider;
import io.anuke.ucore.function.EffectRenderer;
import io.anuke.ucore.modules.ModuleController;

public class Effects{
	private static final ObjectMap<String, EffectDraw> draws = new ObjectMap<>();
	private static EffectProvider provider = (name, color, x, y)-> new Effect(name, color).set(x, y);
	private static final EffectContainer container = new EffectContainer();
	
	public static void setEffectProvider(EffectProvider prov){
		provider = prov;
	}
	
	public static void renderEffect(EffectDraw render, Color color, float life, float x, float y){
		container.set(color, life, render.lifetime, x, y);
		render.draw.render(container);
	}
	
	public static EffectDraw getEffect(String name){
		if(!draws.containsKey(name)) throw new IllegalArgumentException("The effect draw call \"" + name + "\" does not exist!");
		return draws.get(name);
	}
	
	public static void create(String name, float life, EffectRenderer cons){
		draws.put(name, new EffectDraw(cons, life));
	}
	
	public static void effect(String name, float x, float y){
		provider.createEffect(name, Color.WHITE, x, y);;
	}
	
	public static void effect(String name, Entity pos){
		effect(name, pos.x, pos.y);
	}
	
	public static void effect(String name, Spark pos){
		effect(name, pos.pos().x, pos.pos().y);
	}
	
	public static void effect(String name, Color color, float x, float y){
		provider.createEffect(name, color, x, y);
	}
	
	public static void effect(String name, Color color, Entity entity){
		effect(name, color, entity.x, entity.y);
	}
	
	public static void sound(String name){
		Sounds.play(name);
	}
	
	/**Plays a sound, with distance and falloff calulated relative to camera position.*/
	public static void sound(String name, Entity position){
		sound(name, position.x, position.y);
	}
	
	/**Plays a sound, with distance and falloff calulated relative to camera position.*/
	public static void sound(String name, Spark position){
		sound(name, position.pos().x, position.pos().y);
	}
	
	/**Plays a sound, with distance and falloff calulated relative to camera position.*/
	public static void sound(String name, float x, float y){
		Sounds.playDistance(name, Vector2.dst(DrawContext.camera.position.x, DrawContext.camera.position.y, x, y));
	}
	
	public static void shake(float intensity, float duration){
		ModuleController.renderer().shake(intensity, duration);
	}
	
	//TODO
	public static void shake(float intensity, float duration, float x, float y){
		ModuleController.renderer().shake(intensity, duration);
	}
	
	//TODO
	public static void shake(float intensity, float duration, Entity loc){
		shake(intensity, duration, loc.x, loc.y);
	}
	
	public static void shake(float intensity, float duration, Spark loc){
		shake(intensity, duration, loc.pos().x, loc.pos().y);
	}
	
	public static class EffectDraw{
		public EffectRenderer draw;
		public float lifetime;
		
		public EffectDraw(EffectRenderer draw, float life){
			this.lifetime = life;
			this.draw = draw;
		}
	}
	
	public static class EffectContainer{
		public float x, y, time, lifetime;
		public Color color;
		
		public void set(Color color, float life, float lifetime, float x, float y){
			this.x = x; this.y = y; this.color = color; this.time = life; this.lifetime = lifetime;
		}
		
		public float fract(){
			return 1f-time/lifetime;
		}
		
		public float ifract(){
			return time/lifetime;
		}
		
		public float sfract(){
			return (0.5f-Math.abs(time/lifetime-0.5f))*2f;
		}
	}
	
}
