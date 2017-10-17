package io.anuke.ucore.cui.layout;

import io.anuke.ucore.cui.Layout;
import io.anuke.ucore.cui.Section;

public class VerticalLayout extends Layout{
	public boolean top = true;
	
	public VerticalLayout(){
		
	}
	
	public VerticalLayout(boolean top){
		this.top = top;
	}

	@Override
	public void layout(){
		float currentY = style.padBottom;
		
		prefWidth = 0;
		prefHeight = 0;
		
		//TODO proper layout, filling, etc
		for(Section child : children){
			child.y = currentY;
			child.x = style.padLeft;
			if(child.style.fillX){
				child.width = width - style.padLeft - style.padRight;
			}
			currentY += child.height;
			prefWidth = Math.max(prefWidth, child.width);
		}
		
		prefHeight = currentY + style.padTop;
	}
}
