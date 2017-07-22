package io.anuke.ucore.scene.builders;

import io.anuke.ucore.function.Listenable;
import io.anuke.ucore.scene.ui.ImageButton;

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
	
	public imagebutton imageSize(float size){
		element.resizeImage(size);
		return this;
	}
}
