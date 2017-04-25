package io.anuke.ucore.scene.builders;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.scene.ui.Label;

public class label extends builder<label, Label>{
	
	public label(String text){
		element = new Label(text);
		cell = context().add(element);
	}
	
	public void color(Color color){
		element.setColor(Color.WHITE);
	}
}
