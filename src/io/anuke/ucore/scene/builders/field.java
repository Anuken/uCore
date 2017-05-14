package io.anuke.ucore.scene.builders;

import java.util.function.Consumer;

import io.anuke.ucore.scene.ui.TextField;

public class field extends builder<field, TextField>{
	
	public field(String text, Consumer<String> listener){
		element = new TextField(text);
		element.changed(()->{
			listener.accept(element.getText());
		});
		cell = context().add(element);
	}
	
	public field(Consumer<String> listener){
		this("", listener);
	}
}
