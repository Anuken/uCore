package io.anuke.ucore.renderables;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Draw;

public abstract class DrawLayer{
	public final String name;
	public final float layer;
	public final int bind;

	DrawLayer(String name, float layer, int bind){
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
