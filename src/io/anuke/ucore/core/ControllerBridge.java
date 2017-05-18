package io.anuke.ucore.core;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

import io.anuke.ucore.core.Inputs.DeviceType;
import io.anuke.ucore.core.Inputs.InputDevice;
import io.anuke.ucore.scene.ui.KeybindDialog;

public class ControllerBridge{
	
	protected static void load(){
		int i = 0;
		for(Controller c : Controllers.getControllers()){
			Inputs.getDevices().add(new InputDevice(DeviceType.controller, "Controller " + (1+i++), c));
		}
		
		Controllers.addListener(new ControllerAdapter(){
			public void connected(Controller controller){
				Inputs.getDevices().add(new InputDevice(DeviceType.controller, "Controller " + Controllers.getControllers().size, controller));
			}

			public void disconnected(Controller controller){
				for(InputDevice d : Inputs.getDevices()){
					if(d.controller == controller){
						Inputs.getDevices().removeValue(d, true);
						break;
					}
						
				}
			}
		});
	}
	
	protected static void explode(){
		//boom
	}
	
	public static void keybindListen(KeybindDialog dialog){
		Controllers.addListener(new ControllerAdapter(){
			public void connected(Controller controller){
				dialog.internalRefresh();
			}

			public void disconnected(Controller controller){
				dialog.internalRefresh();
			}
			
			public boolean buttonDown (Controller controller, int buttonIndex) {
				if(dialog.canRebindController()){
					
					dialog.rebind(buttonIndex);
					return true;
				}
				return false;
			}
		});
	}
}
