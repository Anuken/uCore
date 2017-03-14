package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SpriteRenderable extends Renderable{
	public Sprite sprite;
	public boolean layerSet;
	public float layer;
	
	protected SpriteRenderable(){
		sprite = new Sprite();
	}

	public SpriteRenderable(TextureRegion region) {
		sprite = new Sprite(region);
	}

	public SpriteRenderable region(TextureRegion region){
		sprite.setRegion(region);
		sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
		return this;
	}
	
	public SpriteRenderable scale(float x, float y){
		sprite.setSize(sprite.getWidth() + x, sprite.getHeight()+y);
		return this;
	}

	public SpriteRenderable set(float x, float y){
		if(!MathUtils.isEqual(y, sprite.getY(), 0.001f))
			RenderableHandler.instance().requestSort();

		sprite.setPosition(x, y);
		return this;
	}
	
	public SpriteRenderable size(float x, float y){
		sprite.setSize(x, y);
		return this;
	}

	public SpriteRenderable set(float x, float y, boolean center){
		return set(x, y, center, false);
	}

	public SpriteRenderable set(float x, float y, boolean center, boolean block){
		sprite.setPosition(x, y);
		if(center){
			if(block){
				sprite.translate((int)( -sprite.getWidth() / 2), (int) (-sprite.getHeight() / 2));
			}else{
				sprite.translate((-sprite.getWidth() / 2), (-sprite.getHeight() / 2));
			}
		}
		return this;
	}

	public SpriteRenderable centerX(){
		sprite.translateX((int) (-sprite.getWidth() / 2));
		return this;
	}

	public SpriteRenderable centerY(){
		sprite.translateY((int) (-sprite.getHeight() / 2));
		return this;
	}

	public SpriteRenderable center(){
		return this.centerX().centerY();
	}

	public SpriteRenderable add(float x, float y){
		sprite.translate(x, y);
		return this;
	}

	public SpriteRenderable layer(float layer){
		layerSet = true;
		this.layer = layer;
		return this;
	}

	public SpriteRenderable color(Color color){
		sprite.setColor(color);
		return this;
	}

	public SpriteRenderable color(float r, float g, float b){
		sprite.setColor(r, g, b, 1f);
		return this;
	}
	
	public SpriteRenderable alpha(float a){
		sprite.setAlpha(a);
		return this;
	}

	@Override
	public void draw(Batch batch){
		sprite.draw(batch);
	}

	@Override
	public float getLayer(){
		return layerSet ? layer : sprite.getY();
	}
	
	@Override
	public void reset(){
		provider = Sorter.tile;
		sprite.setColor(Color.WHITE);
		sprite.setRotation(0);
		layerSet = false;
		layer = 0;
	}
}
