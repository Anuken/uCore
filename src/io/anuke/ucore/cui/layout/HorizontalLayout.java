package io.anuke.ucore.cui.layout;

import com.badlogic.gdx.utils.Align;

import io.anuke.ucore.cui.Layout;
import io.anuke.ucore.cui.Section;

public class HorizontalLayout extends Layout{
	public int align = Align.top;
	
	public HorizontalLayout(){
		
	}
	
	public HorizontalLayout(int align){
		this.align = align;
	}

	@Override
	public void layout(){
		float currentX = x;
		
		//TODO proper layout, filling, etc
		for(Section child : children){
			if(child.isMode(Mode.fillY)){
				child.height = height;
			}
			
			if(align == Align.left){
				child.x = currentX;
				currentX += child.width;
			}else if(align == Align.right){
				currentX -= child.width;
				child.x = currentX;
			}
			
			child.y = y;
			
			
		}
	}

}
