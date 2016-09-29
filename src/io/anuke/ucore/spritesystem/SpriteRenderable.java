package io.anuke.ucore.spritesystem;

import io.anuke.ucore.graphics.Atlas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SpriteRenderable extends Renderable{
	public final Sprite sprite;
	public boolean layerSet;
	public float layer;
	
	public SpriteRenderable(TextureRegion region){
		sprite = new Sprite(region);
	}
	
	public SpriteRenderable setPosition(float x, float y){
		if(!MathUtils.isEqual(y, sprite.getY(), 0.001f))
			RenderableHandler.getInstance().requestSort();
		
		sprite.setPosition(x, y);
		return this;
	}
	
	public SpriteRenderable setPosition(float x, float y, boolean center){
		sprite.setPosition(x, y);
		if(center) sprite.translate(-sprite.getWidth()/2, -sprite.getHeight()/2);
		return this;
	}
	
	public SpriteRenderable centerX(){
		sprite.translateX(-sprite.getWidth()/2);
		return this;
	}
	
	public SpriteRenderable centerY(){
		sprite.translateY(-sprite.getHeight()/2);
		return this;
	}
	
	public SpriteRenderable center(){
		return this.centerX().centerY();
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
	
	public SpriteRenderable generateShadow(Atlas atlas){
		return new SpriteRenderable(atlas.findRegion("shadow" + (int)(sprite.getRegionWidth() * 0.9f / 2f) * 2))
		.setPosition(sprite.getX() + sprite.getWidth()/2, sprite.getY(), true)
		.setColor(new Color(0,0,0,1f))
		.setLayer(-999999);
	}
	
	public SpriteRenderable addShadow(RenderableGroup group, Atlas atlas){
		group.add("shadow", generateShadow(atlas));
		return this;
	}
	
	public SpriteRenderable addShadow(RenderableList list, Atlas atlas){
		list.add(generateShadow(atlas));
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
}
