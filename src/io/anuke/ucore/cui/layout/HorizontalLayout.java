package io.anuke.ucore.cui.layout;

import io.anuke.ucore.cui.Layout;
import io.anuke.ucore.cui.Section;

public class HorizontalLayout extends Layout{
	public boolean left = true;
	
	public HorizontalLayout(){
		
	}
	
	public HorizontalLayout(boolean left){
		this.left = left;
	}

	@Override
	public void layout(){
		float currentX = style.padLeft;
		prefWidth = 0;
		prefHeight = 0;
		
		//TODO proper layout, filling, etc
		for(Section child : children){
			if(child.style.fillY){
				child.height = height - style.padBottom - style.padTop - child.style.spaceTop - child.style.spaceBottom;
			}
			
			if(left){
				child.x = currentX + child.style.spaceBottom;
				currentX += child.width + child.style.spaceLeft + child.style.spaceRight;
			}else{
				currentX -= child.width;
				child.x = currentX;
			}
			
			prefHeight = Math.max(prefHeight, child.height + child.style.spaceTop + child.style.spaceBottom);
			
			child.y = style.padBottom + child.style.spaceBottom;
		}
		
		prefWidth = currentX - style.padRight;
		
		//TODO
		if(parent == null){
			//prefWidth = Gdx.graphics.getWidth();
			//prefHeight = Gdx.graphics.getHeight();
		}
	}
}
