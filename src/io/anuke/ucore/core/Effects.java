package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.entities.Effect;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.modules.ModuleController;

public class Effects{
	
	public static Effect effect(String name, float x, float y){
		return new Effect(name).set(x, y).add();
	}
	
	public static Effect effect(String name, Entity pos){
		return effect(name, pos.x, pos.y);
	}
	
	public static Effect effect(String name, Color color, float x, float y){
		Effect e = effect(name, x, y);
		e.color = color;
		return e;
	}
	
	public static Effect effect(String name, Color color, Entity entity){
		Effect e = effect(name, entity);
		e.color = color;
		return e;
	}
	
	public static void sound(String name){
		Sounds.play(name);
	}
	
	/**Plays a sound, with distance and falloff calulated relative to camera position.*/
	public static void sound(String name, Entity position){
		sound(name, position.x, position.y);
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
		ModuleController.renderer().shake(intensity, duration);
	}
}
