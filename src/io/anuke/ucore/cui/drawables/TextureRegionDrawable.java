package io.anuke.ucore.cui.drawables;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.cui.Drawable;

public class TextureRegionDrawable extends Drawable{
	public TextureRegion region;
	
	public TextureRegionDrawable(TextureRegion region){
		this.region = region;
	}

	@Override
	public void draw(float x, float y, float width, float height, float scale){
		Core.batch.draw(region, x, y, width, height);
	}

}
