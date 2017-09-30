package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.ucore.facet.BaseFacet.DrawFunc;

public class FacetList implements Poolable{
	public Array<Facet> facets = new Array<Facet>();
	
	public void add(float layer, Sorter sort, DrawFunc draw){
		BaseFacet r = new BaseFacet(layer, sort, draw);
		r.add();
		facets.add(r);
	}
	
	public Facet first(){
		return facets.first();
	}
	
	public void add(Facet renderable){
		renderable.add();
		facets.add(renderable);
	}
	
	public void setPosition(float x, float y){
		for(Facet r : facets)
			r.set(x, y);
	}
	
	public void free(){
		Facets.instance().remove(facets);
		facets.clear();
	}

	@Override
	public void reset(){
		facets.clear();
	}
}
