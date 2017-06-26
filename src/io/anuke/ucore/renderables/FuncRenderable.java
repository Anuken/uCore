package io.anuke.ucore.renderables;

public class FuncRenderable extends Renderable{
	public float layer = 0f;
	DrawFunc drawable;
	
	public FuncRenderable(float layer, Sorter sort, DrawFunc draw){
		this.layer = layer;
		this.drawable = draw;
		this.sort(sort);
	}
	
	public FuncRenderable(DrawFunc draw){
		this.drawable = draw;
		sort(Sorter.object);
	}
	
	
	public FuncRenderable(){
		
	}
	
	@Override
	public void reset(){
		
	}

	@Override
	public void draw(){
		RenderableHandler.instance().requestSort();
		
		if(drawable != null) drawable.draw(this);
	}

	@Override
	public Renderable set(float x, float y){
		return this;
	}

	@Override
	public float getLayer(){
		return layer;
	}
	
	public static interface DrawFunc{
		public void draw(FuncRenderable l);
	}
}
