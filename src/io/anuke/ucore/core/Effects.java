package io.anuke.ucore.core;

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
	
	public static void shake(float intensity, float duration){
		ModuleController.renderer().shake(intensity, duration);
	}
}
