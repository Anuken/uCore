package io.anuke.ucore.facet;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.graphics.Surface;

public abstract class FacetLayer{
	public final String name;
	public final float layer;
	public final int bind;
	public final Surface surface;

	public FacetLayer(String name, float layer, int bind){
		this.name = name;
		this.layer = layer;
		this.bind = bind;
		
		surface = Graphics.createSurface(Core.cameraScale, bind);
	}

	public void end(){
		Graphics.surface();
	}

	public void begin(){
		Graphics.surface(surface);
	}
	
	public boolean acceptFacet(Facet facet){
		return layerEquals(facet.getLayer());
	}

	public boolean layerEquals(float f){
		return MathUtils.isEqual(f, layer, 1f);
	}
}
