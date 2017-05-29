package io.anuke.ucore.scene.builders;

import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.utils.function.Listenable;

public class imagebutton extends builder<imagebutton, ImageButton>{
	
	public imagebutton(String image, Listenable listener){
		element = new ImageButton(image);
		cell = context().add(element);
		element.clicked(listener);
	}
	
	public imagebutton imageSize(float size){
		element.resizeImage(size);
		return this;
	}
}
