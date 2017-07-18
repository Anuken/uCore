package io.anuke.ucore.ecs.extend.traits;

import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Trait;

@Require(PosTrait.class)
public class ColliderTrait extends Trait{
	public float width, height;
	public float offsetx, offsety;
	
	public ColliderTrait(){
		this(10, 10);
	}
	
	public ColliderTrait(float size){
		this(size, size);
	}
	
	public ColliderTrait(float w, float h){
		width = w;
		height = h;
	}
	
	public void setSize(float size){
		width = height = size;
	}
}
