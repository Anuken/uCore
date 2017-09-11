package io.anuke.ucore.facet;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;

public abstract class FacetLayer{
	public final String name;
	public final float layer;
	public final int bind;

	FacetLayer(String name, float layer, int bind){
		this.name = name;
		this.layer = layer;
		this.bind = bind;
		
		Draw.addSurface(name, Core.cameraScale, bind);
	}

	public void end(){
		Draw.surface();
	}

	public void begin(){
		Draw.surface(name);
	}

	public boolean layerEquals(float f){
		return MathUtils.isEqual(f, layer, 1f);
	}
}
