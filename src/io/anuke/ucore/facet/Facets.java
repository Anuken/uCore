package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public class Facets{
	private static Facets instance;
	private Array<Facet> container = new Array<>();
	private boolean updated;
	
	private FacetHandler manager = new FacetHandler(){
		public void drawRenderables(Iterable<Facet> renderables){
			for(Facet renderable : renderables){
				renderable.draw();
			}
		}
	};
	
	private Facets(){}

	public void renderAll(){
		if(updated){
			container.sort();
			updated = false;
		}

		manager.drawRenderables(container);
	}
	
	public void forceSort(){
		container.sort();
		updated = false;
	}
	
	public void setLayerManager(FacetHandler manager){
		this.manager = manager;
	}

	public void add(Facet renderable){
		updated = true;
		container.add(renderable);
	}

	public void remove(Facet renderable){
		container.removeValue(renderable, true);
		renderable.onFree();
	}

	public void remove(Iterable<? extends Facet> list){
		for(Facet r : list){
			remove(r);
		}
	}
	
	public void clear(){
		container.clear();
	}
	
	/**Returns the current amount of renderables.*/
	public int getSize(){
		return container.size;
	}
	
	public Array<Facet> getFacets(){
		return container;
	}
	
	public void requestSort(){
		updated = true;
	}

	public static Facets instance(){
		if(instance == null) instance = new Facets();
		return instance;
	}
}
