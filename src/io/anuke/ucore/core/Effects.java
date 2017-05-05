package io.anuke.ucore.core;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.entities.Effect;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.modules.ModuleController;

public class Effects{
	
	public static void effect(String name, float x, float y){
		new Effect(name).set(x, y).add();
	}
	
	public static void effect(String name, Entity pos){
		effect(name, pos.x, pos.y);
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
