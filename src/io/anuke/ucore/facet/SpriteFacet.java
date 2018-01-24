package io.anuke.ucore.facet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.util.Atlas;

public class SpriteFacet extends Facet{
	public Sprite sprite;
	public boolean layerSet;
	public float layer;
	
	protected SpriteFacet(){
		sprite = new Sprite();
	}

	public SpriteFacet(TextureRegion region) {
		sprite = new Sprite(region);
	}
	
	public SpriteFacet(String region) {
		sprite = new Sprite(Draw.region(region));
	}
	
	public SpriteFacet addShadow(FacetList list, float offset){
		list.add(generateShadow().add(0, offset));
		return this;
	}
	
	public SpriteFacet addShadow(FacetList list, String region, float offset){
		list.add(generateShadow(region).add(0, offset));
		return this;
	}
	
	public SpriteFacet addShadow(FacetMap g, Atlas atlas, float offset){
		g.add("shadow", generateShadow().add(0, offset));
		return this;
	}
	
	public SpriteFacet region(TextureRegion region){
		sprite.setRegion(region);
		sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
		return this;
	}
	
	public SpriteFacet generateShadow(){
		return generateShadow("shadow"
				+ (int) (sprite.getRegionWidth() * 0.8f / 2f + Math.pow(sprite.getRegionWidth(), 1.5f) / 200f) * 2);
	}
	
	public SpriteFacet generateShadow(String region){
		return new SpriteFacet(region)
						.set(sprite.getX() + (int) (sprite.getWidth() / 2), sprite.getY(), true, true)
						.color(new Color(0, 0, 0, 1f)).layer(-999999);
	}
	
	public SpriteFacet scale(float x, float y){
		sprite.setSize(sprite.getWidth() + x, sprite.getHeight()+y);
		return this;
	}

	public SpriteFacet set(float x, float y){
		if(!MathUtils.isEqual(y, sprite.getY(), 0.001f))
			Facets.instance().requestSort();

		sprite.setPosition(x, y);
		return this;
	}
	
	public SpriteFacet size(float x, float y){
		sprite.setSize(x, y);
		return this;
	}

	public SpriteFacet set(float x, float y, boolean center){
		return set(x, y, center, false);
	}

	public SpriteFacet set(float x, float y, boolean center, boolean block){
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
	
	public SpriteFacet skewX(float amount){
		sprite.getVertices()[SpriteBatch.X2] = sprite.getX() + amount;
		sprite.getVertices()[SpriteBatch.X3] = sprite.getX() + sprite.getWidth() + amount;
		return this;
	}

	public SpriteFacet centerX(){
		sprite.translateX((int) (-sprite.getWidth() / 2));
		return this;
	}

	public SpriteFacet centerY(){
		sprite.translateY((int) (-sprite.getHeight() / 2));
		return this;
	}

	public SpriteFacet center(){
		return this.centerX().centerY();
	}

	public SpriteFacet add(float x, float y){
		sprite.translate(x, y);
		return this;
	}

	public SpriteFacet layer(float layer){
		layerSet = true;
		this.layer = layer;
		return this;
	}

	public SpriteFacet color(Color color){
		sprite.setColor(color);
		return this;
	}

	public SpriteFacet color(float r, float g, float b){
		sprite.setColor(r, g, b, 1f);
		return this;
	}
	
	public SpriteFacet alpha(float a){
		sprite.setAlpha(a);
		return this;
	}
	
	public Facet shadow(){
		return layer(Sorter.shadow);
	}

	@Override
	public void draw(){
		sprite.draw(Core.batch);
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
