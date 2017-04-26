package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.spritesystem.FuncRenderable.DrawFunc;

public class RenderableList{
	public Array<Renderable> renderables = new Array<Renderable>();
	
	public void add(float layer, Sorter sort, DrawFunc draw){
		FuncRenderable r = new FuncRenderable(layer, sort, draw);
		r.add();
		renderables.add(r);
	}
	
	public Renderable first(){
		return renderables.first();
	}
	
	public void add(Renderable renderable){
		renderable.add();
		renderables.add(renderable);
	}
	
	public void setPosition(float x, float y){
		for(Renderable r : renderables)
			r.set(x, y);
	}
	
	public void free(){
		RenderableHandler.instance().remove(renderables);
		renderables.clear();
	}
}
