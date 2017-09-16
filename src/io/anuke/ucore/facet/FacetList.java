package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.ucore.facet.BaseFacet.DrawFunc;

public class FacetList implements Poolable{
	public Array<Facet> renderables = new Array<Facet>();
	
	public void add(float layer, Sorter sort, DrawFunc draw){
		BaseFacet r = new BaseFacet(layer, sort, draw);
		r.add();
		renderables.add(r);
	}
	
	public Facet first(){
		return renderables.first();
	}
	
	public void add(Facet renderable){
		renderable.add();
		renderables.add(renderable);
	}
	
	public void setPosition(float x, float y){
		for(Facet r : renderables)
			r.set(x, y);
	}
	
	public void free(){
		Facets.instance().remove(renderables);
		renderables.clear();
	}

	@Override
	public void reset(){
		renderables.clear();
	}
}
