package io.anuke.ucore.engine;

import com.badlogic.gdx.graphics.Camera;

import io.anuke.ucore.modules.ModuleController;

public abstract class Main<T extends ModuleController<T>> extends ModuleController<T>{
	
	@Override
	public void update(){
		Input.update();
	}
	
	public static Camera getViewport(){
		//TODO
		return null;
	}
}
