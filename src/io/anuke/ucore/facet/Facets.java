package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public class Facets{
	private static Facets instance;
	private Array<Facet> facets = new Array<Facet>();
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
			facets.sort();
			updated = false;
		}

		manager.drawRenderables(facets);
	}
	
	public void forceSort(){
		facets.sort();
		updated = false;
	}
	
	public void setLayerManager(FacetHandler manager){
		this.manager = manager;
	}

	public void add(Facet renderable){
		updated = true;
		facets.add(renderable);
	}

	public void remove(Facet renderable){
		facets.removeValue(renderable, true);
		renderable.onFree();
	}

	public void remove(Iterable<? extends Facet> list){
		for(Facet r : list){
			remove(r);
		}
	}
	
	public void clear(){
		facets.clear();
	}
	
	/**Returns the current amount of renderables.*/
	public int getSize(){
		return facets.size;
	}
	
	public Array<Facet> getFacets(){
		return facets;
	}
	
	public void requestSort(){
		updated = true;
	}

	public static Facets instance(){
		if(instance == null) instance = new Facets();
		return instance;
	}
}
