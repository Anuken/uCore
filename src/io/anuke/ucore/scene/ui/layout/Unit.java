package io.anuke.ucore.scene.ui.layout;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

import io.anuke.ucore.util.Mathf;

public enum Unit{
	px{
		@Override
		public float inPixels(float amount){
			return amount;
		}
	},
	dp{
		float scl = ((Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.WebGL) 
				? 1f : Mathf.round2(Gdx.graphics.getDensity() / 1.5f + addition, 0.5f));
		
		@Override
		public float inPixels(float amount){
			return amount * scl;
		}
	};
	public float addition = 0f;
	
	public abstract float inPixels(float amount);
}
