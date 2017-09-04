package io.anuke.ucore.facet;

import io.anuke.ucore.core.Draw;

public class FacetLayers{
	public static final FacetLayer 
	
	shadow = new FacetLayer("shadow", Sorter.shadow, 0){
		public void end(){
			Draw.color(0,0,0,0.1f);
			Draw.flushSurface();
			Draw.color();
		}
	}, 
	light = new FacetLayer("light", Sorter.light, 6){

	},
	darkness = new FacetLayer("darkness", Sorter.dark, 7){
		
	};
}
