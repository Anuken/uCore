package io.anuke.ucore.renderables;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Draw;

public enum DrawLayer{
	shadow("shadow", Sorter.shadow){
		public void end(){
			Draw.color(0,0,0,0.1f);
			Draw.flushSurface();
			Draw.color();
		}
	}, 
	light("light", Sorter.light){
		{
			bind = 6;
		}
	};

	public final String name;
	public final float layer;
	public int bind = 0;

	private DrawLayer(String name, float layer){
		this.name = name;
		this.layer = layer;
		
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
