package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;

public interface FacetContainer{
	
	public Iterable<Facet> getFacets();
	public Array<Facet> getFacetArray();
	public void removeFacet(Facet facet);
	public void addFacet(Facet facet);
	public void clear();
	public void sort();
	public int size();
}
