package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class FacetContainers{
	
	public static final FacetContainer
	array = new FacetContainer(){
		Array<Facet> facets = new Array<Facet>();

		@Override
		public Iterable<Facet> getFacets(){
			return facets;
		}

		@Override
		public void removeFacet(Facet facet){
			facets.removeValue(facet, true);
		}

		@Override
		public void addFacet(Facet facet){
			facets.add(facet);
		}

		@Override
		public void sort(){
			facets.sort();
		}

		@Override
		public int size(){
			return facets.size;
		}

		@Override
		public void clear(){
			facets.clear();
		}
	},
	unsortedSet = new FacetContainer(){
		ObjectSet<Facet> facets = new ObjectSet<Facet>();
		Array<Facet> array = new Array<>();

		@Override
		public Iterable<Facet> getFacets(){
			return facets;
		}

		@Override
		public void removeFacet(Facet facet){
			facets.remove(facet);
		}

		@Override
		public void addFacet(Facet facet){
			if(facet.provider == Sorter.tile) return;
			facets.add(facet);
		}

		@Override
		public void sort(){
			//hahahahahaha
			
		}

		@Override
		public int size(){
			return facets.size;
		}

		@Override
		public void clear(){
			facets.clear();
		}
	};
}
