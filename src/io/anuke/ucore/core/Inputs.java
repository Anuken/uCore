package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class Inputs{
	private static boolean[] buttons = new boolean[5];
	private static InputMultiplexer plex = new InputMultiplexer();
	
	public static void update(){
		for(int i = 0; i < buttons.length; i ++){
			buttons[i] = Gdx.input.isButtonPressed(i);
		}
	}
	
	/**Adds another input processor to the chain.*/
	public static void addProcessor(InputProcessor listener){
		plex.addProcessor(listener);
		Gdx.input.setInputProcessor(plex);
	}
	
	public static void flipProcessors(){
		plex.getProcessors().reverse();
	}
	
	public static boolean keyDown(int key){
		return Gdx.input.isKeyPressed(key);
	}
	
	public static boolean keyUp(int key){
		return Gdx.input.isKeyJustPressed(key);
	}
	
	public static boolean keyDown(String name){
		return keyDown(KeyBinds.get(name));
	}
	
	public static boolean keyUp(String name){
		return keyUp(KeyBinds.get(name));
	}
	
	public static boolean buttonDown(int button){
		return Gdx.input.isButtonPressed(button);
	}
	
	public static boolean buttonUp(int button){
		return Gdx.input.isButtonPressed(button) && !buttons[button];
	}
}
