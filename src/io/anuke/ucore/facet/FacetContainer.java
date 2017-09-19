package io.anuke.ucore.facet;

public interface FacetContainer{
	
	public Iterable<Facet> getFacets();
	public void removeFacet(Facet facet);
	public void addFacet(Facet facet);
	public void clear();
	public void sort();
	public int size();
}
