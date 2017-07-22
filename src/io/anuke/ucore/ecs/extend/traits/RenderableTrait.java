package io.anuke.ucore.ecs.extend.traits;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.function.BiConsumer;
import io.anuke.ucore.renderables.FuncRenderable;
import io.anuke.ucore.renderables.FuncRenderable.DrawFunc;
import io.anuke.ucore.renderables.RenderableList;
import io.anuke.ucore.renderables.Sorter;


public class RenderableTrait extends Trait{
	public RenderableList list = new RenderableList();
	
	private BiConsumer<RenderableTrait, Spark> drawer;
	private boolean drawn = false;
	
	public RenderableTrait(BiConsumer<RenderableTrait, Spark> drawer){
		this.drawer = drawer;
	}
	
	private RenderableTrait(){}
	
	@Override
	public void added(Spark spark){
		if(!drawn){
			drawer.accept(this, spark);
		}
		drawn = true;
	}
	
	@Override
	public void removed(Spark spark){
		list.free();
		drawn = false;
	}
	
	public void shadow(Spark spark, int size){
		drawShadow(spark, size, 0);
	}
	
	public void drawShadow(Spark spark, int size, float offsety){
		
		String shadow = "shadow"
				+ (int) (size * 0.8f / 2f + Math.pow(size, 1.5f) / 200f) * 2;
		
		draw(p->{
			Draw.color();
			p.provider = Sorter.tile;
			p.layer = Sorter.shadow;
			Draw.rect(shadow, spark.pos().x, spark.pos().y + offsety);
		});
	}
	
	public void draw(DrawFunc d){
		list.add(new FuncRenderable(0, Sorter.object, d));
	}
}
