package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public class Facets{
	private static Facets instance;
	private FacetContainer container = FacetContainers.array;
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

		manager.drawRenderables(container.getFacets());
	}
	
	public void forceSort(){
		container.sort();
		updated = false;
	}
	
	public void setLayerManager(FacetHandler manager){
		this.manager = manager;
	}
	
	public void setFacetContainer(FacetContainer cont){
		this.container = cont;
	}

	public void add(Facet renderable){
		updated = true;
		container.addFacet(renderable);
	}

	public void remove(Facet renderable){
		container.removeFacet(renderable);
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
		return container.size();
	}
	
	public Iterable<Facet> getFacets(){
		return container.getFacets();
	}
	
	public Array<Facet> getFacetArray(){
		return container.getFacetArray();
	}
	
	public void requestSort(){
		updated = true;
	}

	public static Facets instance(){
		if(instance == null) instance = new Facets();
		return instance;
	}
}
