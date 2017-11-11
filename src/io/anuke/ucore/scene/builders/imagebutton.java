package io.anuke.ucore.scene.builders;

import io.anuke.ucore.function.Listenable;
import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.ui.Label;
import io.anuke.ucore.scene.ui.layout.Cell;

public class imagebutton extends builder<imagebutton, ImageButton>{
	
	public imagebutton(String image, Listenable listener){
		element = new ImageButton(image);
		cell = context().add(element);
		element.clicked(listener);
	}
	
	public imagebutton(String image, float isize, Listenable listener){
		element = new ImageButton(image);
		cell = context().add(element);
		element.clicked(listener);
		element.resizeImage(isize);
	}
	
	public imagebutton(String image, String style, float isize, Listenable listener){
		element = new ImageButton(image, style);
		cell = context().add(element);
		element.clicked(listener);
		element.resizeImage(isize);
	}
	
	public Cell<Label> text(String text){
		element.row();
		return element.add(text);
	}
	
	public imagebutton imageSize(float size){
		element.resizeImage(size);
		return this;
	}
}
