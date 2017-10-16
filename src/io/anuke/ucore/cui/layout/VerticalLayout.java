package io.anuke.ucore.cui.layout;

import com.badlogic.gdx.utils.Align;

import io.anuke.ucore.cui.Layout;
import io.anuke.ucore.cui.Section;

public class VerticalLayout extends Layout{
	public int align = Align.top;
	
	public VerticalLayout(){
		
	}
	
	public VerticalLayout(int align){
		this.align = align;
	}

	@Override
	public void layout(){
		float currentY = y;
		
		//TODO proper layout, filling, etc
		for(Section child : children){
			child.y = currentY;
			child.x = x;
			if(child.isMode(Mode.fillX)){
				child.width = width;
			}
			currentY += child.height;
		}
	}
}
