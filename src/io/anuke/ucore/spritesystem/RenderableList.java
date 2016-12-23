package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.utils.Array;


public class RenderableList{
	public Array<Renderable> renderables = new Array<Renderable>();
	
	public void add(Renderable renderable){
		renderable.add();
		renderables.add(renderable);
	}
	
	public void setPosition(float x, float y){
		for(Renderable r : renderables)
			r.setPosition(x, y);
	}
	
	public void free(){
		RenderableHandler.instance().remove(renderables);
		renderables.clear();
	}
}
