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
		@Override
		public float inPixels(float amount){
			//TODO rollback
			float scl = ((Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.WebGL) 
					? 1f : Mathf.round2(Gdx.graphics.getDensity() / 1.5f, 0.5f));
			return amount*scl;
		}
	};
	
	public abstract float inPixels(float amount);
}
