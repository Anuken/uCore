package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class RenderableHandler{
	private static RenderableHandler instance;
	private Array<Renderable> renderables = new Array<Renderable>();
	private boolean updated;
	private LayerManager manager = new LayerManager(){
		public void draw(Array<Renderable> renderables, Batch batch){
			for(Renderable renderable : renderables){
				renderable.draw(batch);
			}
		}
	};
	
	private RenderableHandler(){}

	public void renderAll(Batch batch){
		if(updated){
			renderables.sort();
			updated = false;
		}

		manager.draw(renderables, batch);
	}
	
	public void setLayerManager(LayerManager manager){
		this.manager = manager;
	}

	public void add(Renderable renderable){
		updated = true;
		renderables.add(renderable);
	}

	public void remove(Renderable renderable){
		renderables.removeValue(renderable, true);
		renderable.onFree();
	}

	public void remove(Iterable<? extends Renderable> list){
		for(Renderable r : list)
			remove(r);
	}
	
	public void clear(){
		renderables.clear();
	}
	
	/**Returns the current amount of renderables.*/
	public int getSize(){
		return renderables.size;
	}
	
	public void requestSort(){
		updated = true;
	}

	public static RenderableHandler instance(){
		if(instance == null) instance = new RenderableHandler();
		return instance;
	}
}
