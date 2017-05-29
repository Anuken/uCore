package io.anuke.ucore.scene.builders;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.scene.ui.TextButton;
import io.anuke.ucore.scene.utils.function.Listenable;

public class button extends builder<button, TextButton>{
	
	public button(String text, Listenable listener){
		element = new TextButton(text);
		element.clicked(listener);
		cell = context().add(element);
	}
	
	public void textColor(Color color){
		element.getLabel().setColor(color);
	}
}
