package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Trait;

public class ColliderTrait extends Trait{
	public float width, height;
	
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
}
