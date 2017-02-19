package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.g2d.Batch;

public class LambdaRenderable extends Renderable{
	public float layer = 0f;
	Drawable drawable;
	
	public LambdaRenderable(float layer, Sorter sort, Drawable draw){
		this.layer = layer;
		this.drawable = draw;
		this.setProvider(sort);
	}
	
	public LambdaRenderable(Drawable draw){
		this.drawable = draw;
	}
	
	
	public LambdaRenderable(){
		
	}
	
	@Override
	public void reset(){
		
	}

	@Override
	public void draw(Batch batch){
		RenderableHandler.instance().requestSort();
		if(drawable != null) drawable.draw(this);
	}

	@Override
	public Renderable setPosition(float x, float y){
		return this;
	}

	@Override
	public float layer(){
		return layer;
	}
	
	public static interface Drawable{
		public void draw(LambdaRenderable l);
	}
}
