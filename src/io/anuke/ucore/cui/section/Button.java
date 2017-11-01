package io.anuke.ucore.cui.section;

import com.badlogic.gdx.Input.Buttons;

import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.cui.Stateful;
import io.anuke.ucore.cui.layout.HorizontalLayout;

public class Button extends HorizontalLayout implements Stateful{
	
	public Button(){
		state = State.up;
	}
	
	@Override
	public void update(){
		State lastState = (State)targetState;
		
		if(hasMouse()){
			if(Inputs.buttonDown(Buttons.LEFT)){
				targetState = State.down;
			}else{
				targetState = State.over;
			}
		}else{
			targetState = State.up;
		}
		
		if(targetState != lastState){
			stateTime = 0f;
		}
		
		updateState();
	}
	
	@Override
	public Enum<?>[] stateValues(){
		return State.values();
	}
	
	@Override
	public Enum<?> defaultState(){
		return State.up;
	}
	
	enum State{
		up, over, down;
	}

}
