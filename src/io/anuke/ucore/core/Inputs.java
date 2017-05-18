package io.anuke.ucore.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class Inputs{
	private static boolean[] buttons = new boolean[5];
	private static InputMultiplexer plex = new InputMultiplexer();
	private static int scroll = 0;
	private static Array<InputDevice> devices = new Array<>();
	private static InputProcessor listen = new InputAdapter(){
		@Override
		public boolean scrolled(int amount){
			scroll = -amount;
			return false;
		}
	};
	
	static{
		plex.addProcessor(listen);
		
		devices.add(new InputDevice(DeviceType.keyboard, "Keyboard"));
		
		int i = 0;
		for(Controller c : Controllers.getControllers()){
			devices.add(new InputDevice(DeviceType.controller, "Controller " + (1+i++), c));
		}
		
		Controllers.addListener(new ControllerAdapter(){
			public void connected(Controller controller){
				devices.add(new InputDevice(DeviceType.controller, "Controller " + Controllers.getControllers().size, controller));
			}

			public void disconnected(Controller controller){
				for(InputDevice d : devices){
					if(d.controller == controller){
						devices.removeValue(d, true);
						break;
					}
						
				}
			}
		});
	}
	
	public static Array<InputDevice> getDevices(){
		return devices;
	}
	
	public static void update(){
		for(int i = 0; i < buttons.length; i ++){
			buttons[i] = Gdx.input.isButtonPressed(i);
		}
		scroll = 0;
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
	
	public static boolean keyDown(String section, String name){
		return keyDown(KeyBinds.get(section, name));
	}
	
	public static boolean keyUp(String section, String name){
		return keyUp(KeyBinds.get(section, name));
	}
	
	public static boolean buttonDown(int button){
		return Gdx.input.isButtonPressed(button);
	}
	
	public static boolean buttonUp(int button){
		return Gdx.input.isButtonPressed(button) && !buttons[button];
	}
	
	public static boolean buttonRelease(int button){
		return !Gdx.input.isButtonPressed(button) && buttons[button];
	}
	
	public static int scroll(){
		return scroll;
	}
	
	public static boolean scrolled(){
		return Math.abs(scroll) > 0;
	}
	
	public static class InputDevice{
		public final DeviceType type;
		public final String name;
		public final Controller controller;
		
		public InputDevice(DeviceType type, String name){
			this(type, name, null);
		}
		
		public InputDevice(DeviceType type, String name, Controller controller){
			this.type = type;
			this.name = name;
			this.controller = null;
		}
	}
	
	public static enum DeviceType{
		keyboard, controller
	}
}
