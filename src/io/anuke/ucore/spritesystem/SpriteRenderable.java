package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.graphics.Atlas;

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

	public SpriteRenderable setRegion(TextureRegion region){
		sprite.setRegion(region);
		sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
		return this;
	}
	
	public SpriteRenderable scaleBy(float x, float y){
		sprite.setSize(sprite.getWidth() + x, sprite.getHeight()+y);
		return this;
	}

	public SpriteRenderable setPosition(float x, float y){
		if(!MathUtils.isEqual(y, sprite.getY(), 0.001f))
			RenderableHandler.instance().requestSort();

		sprite.setPosition(x, y);
		return this;
	}
	
	public SpriteRenderable setSize(float x, float y){
		sprite.setSize(x, y);
		return this;
	}

	public SpriteRenderable setPosition(float x, float y, boolean center){
		return setPosition(x, y, center, false);
	}

	public SpriteRenderable setPosition(float x, float y, boolean center, boolean block){
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

	public SpriteRenderable translate(float x, float y){
		sprite.translate(x, y);
		return this;
	}

	public SpriteRenderable setLayer(float layer){
		layerSet = true;
		this.layer = layer;
		return this;
	}

	public SpriteRenderable setColor(Color color){
		sprite.setColor(color);
		return this;
	}

	public SpriteRenderable setColor(float r, float g, float b){
		sprite.setColor(r, g, b, 1f);
		return this;
	}
	
	public SpriteRenderable setAlpha(float a){
		sprite.setAlpha(a);
		return this;
	}

	public SpriteRenderable generateShadow(Atlas atlas){
		return new SpriteRenderable(atlas.findRegion("shadow"
				+ (int) (sprite.getRegionWidth() * 0.8f / 2f + Math.pow(sprite.getRegionWidth(), 1.5f) / 200f) * 2))
						.setPosition(sprite.getX() + (int) (sprite.getWidth() / 2), sprite.getY(), true, true)
						.setColor(new Color(0, 0, 0, 1f)).setLayer(-999999);
	}

	public SpriteRenderable setAsShadow(){
		return setLayer(-999999);
	}
	
	public SpriteRenderable setLight(){
		return setLayer(999999);
	}
	
	public SpriteRenderable setDark(){
		return setLayer(999999+1);
	}

	public SpriteRenderable addShadow(RenderableGroup group, Atlas atlas){
		group.add("shadow", generateShadow(atlas));
		return this;
	}

	public SpriteRenderable addShadow(RenderableList list, Atlas atlas){
		list.add(generateShadow(atlas));
		return this;
	}

	public SpriteRenderable addShadow(RenderableList list, Atlas atlas, float offset){
		list.add(generateShadow(atlas).translate(0, offset));
		return this;
	}
	
	public SpriteRenderable addShadow(RenderableGroup list, Atlas atlas, float offset){
		list.add("shadow", generateShadow(atlas).translate(0, offset));
		return this;
	}

	@Override
	public void draw(Batch batch){
		sprite.draw(batch);
	}

	@Override
	public float layer(){
		return layerSet ? layer : sprite.getY();
	}
	
	@Override
	public void reset(){
		provider = SortProviders.tile;
		sprite.setColor(Color.WHITE);
		sprite.setRotation(0);
		layerSet = false;
		layer = 0;
	}
}
