package io.anuke.ucore.spritesort;

public class DrawPointer implements Comparable<DrawPointer>{
	public Drawable drawable;
	public float layer;
	public DrawLayer sorter = DrawLayer.object;
	
	public DrawPointer(){
		
	}
	
	public DrawPointer(Drawable draw, float layer){
		this.drawable = draw;
		this.layer = layer;
	}
	
	public DrawPointer(Drawable draw, DrawLayer dl, float layer){
		this.drawable = draw;
		this.layer = layer;
		this.sorter = dl;
	}

	@Override
	public int compareTo(DrawPointer o){
		return sorter.compare(sorter, layer, o.sorter, o.layer);
	}
}
