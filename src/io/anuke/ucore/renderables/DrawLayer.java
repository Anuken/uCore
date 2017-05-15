package io.anuke.ucore.renderables;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Draw;

public enum DrawLayer{
	shadow("shadow", Sorter.shadow, 0){
		public void end(){
			Draw.color(0,0,0,0.1f);
			Draw.flushSurface();
			Draw.color();
		}
	}, 
	light("light", Sorter.light, 6){

	},
	darkness("darkness", Sorter.dark, 7){
		
	};

	public final String name;
	public final float layer;
	public final int bind;

	private DrawLayer(String name, float layer, int bind){
		this.name = name;
		this.layer = layer;
		this.bind = bind;
		
		Draw.addSurface(name, 1, bind);
	}

	public void end(){
		Draw.surface();
	}

	public void begin(){
		Draw.surface(name);
	}

	public boolean layerEquals(float f){
		return MathUtils.isEqual(f, layer, 0.01f);
	}
}
