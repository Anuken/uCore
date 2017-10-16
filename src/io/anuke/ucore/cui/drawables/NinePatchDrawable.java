package io.anuke.ucore.cui.drawables;

import com.badlogic.gdx.graphics.g2d.NinePatch;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.cui.Drawable;

public class NinePatchDrawable extends Drawable{
	public NinePatch patch;
	
	public NinePatchDrawable(NinePatch patch){
		this.patch = patch;
	}

	@Override
	public void draw(float x, float y, float width, float height, float scale){
		patch.draw(Core.batch, x, y, 0, 0, width/scale, height/scale, scale, scale, 0);
	}

}
