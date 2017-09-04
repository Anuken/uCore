package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public class Facets{
	private static Facets instance;
	private Array<Facet> renderables = new Array<Facet>();
	private boolean updated;
	
	private FacetHandler manager = new FacetHandler(){
		public void drawRenderables(Array<Facet> renderables){
			for(Facet renderable : renderables){
				renderable.draw();
			}
		}
	};
	
	private Facets(){}

	public void renderAll(){
		if(updated){
			renderables.sort();
			updated = false;
		}

		manager.drawRenderables(renderables);
	}
	
	public void setLayerManager(FacetHandler manager){
		this.manager = manager;
	}

	public void add(Facet renderable){
		updated = true;
		renderables.add(renderable);
	}

	public void remove(Facet renderable){
		renderables.removeValue(renderable, true);
		renderable.onFree();
	}

	public void remove(Iterable<? extends Facet> list){
		for(Facet r : list)
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

	public static Facets instance(){
		if(instance == null) instance = new Facets();
		return instance;
	}
}
