package io.anuke.ucore.renderables;

import io.anuke.ucore.core.Draw;

public class DrawLayers{
	public static final DrawLayer 
	
	shadow = new DrawLayer("shadow", Sorter.shadow, 0){
		public void end(){
			Draw.color(0,0,0,0.1f);
			Draw.flushSurface();
			Draw.color();
		}
	}, 
	light = new DrawLayer("light", Sorter.light, 6){

	},
	darkness = new DrawLayer("darkness", Sorter.dark, 7){
		
	};
}
