package io.anuke.ucore.engine;

import com.badlogic.gdx.Gdx;

public class Input{
	private static boolean[] buttons = new boolean[5];
	
	protected static void update(){
		for(int i = 0; i < buttons.length; i ++){
			buttons[i] = Gdx.input.isButtonPressed(i);
		}
	}
	
	public static boolean keyDown(int key){
		return Gdx.input.isKeyPressed(key);
	}
	
	public static boolean keyUp(int key){
		return Gdx.input.isKeyJustPressed(key);
	}
	
	public static boolean keyDown(String name){
		return keyDown(KeyBindings.getKey(name));
	}
	
	public static boolean keyUp(String name){
		return keyUp(KeyBindings.getKey(name));
	}
	
	public static boolean buttonDown(int button){
		return Gdx.input.isButtonPressed(button);
	}
	
	public static boolean buttonUp(int button){
		return Gdx.input.isButtonPressed(button) && !buttons[button];
	}
}
