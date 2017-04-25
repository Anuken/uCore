package io.anuke.ucore.scene.builders;

import java.util.function.Consumer;

import io.anuke.ucore.scene.ui.CheckBox;

public class checkbox extends builder<checkbox, CheckBox>{
	
	public checkbox(String text){
		this(text, null);
	}
	
	public checkbox(String text, Consumer<Boolean> listener){
		element = new CheckBox(text);
		if(listener != null)
		element.changed(()->{
			listener.accept(element.isChecked());
		});
		
		cell = context().add(element);
	}
	
	public checkbox changed(Consumer<Boolean> listener){
		element.changed(()->{
			listener.accept(element.isChecked());
		});
		return this;
	}
}
