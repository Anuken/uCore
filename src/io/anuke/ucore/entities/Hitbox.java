package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;

public class Hitbox{
	public float offsetx, offsety;
	public float width, height;

	public Hitbox(float size) {
		width = height = size;
	}

	public Hitbox(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public Hitbox() {

	}

	public void bounds(float offsetx, float offsety, float width, float height){
		this.offsety = offsety;
		this.offsetx = offsetx;
		this.width = width;
		this.height = height;
	}

	public Rectangle getRect(float x, float y){
		return getRect(Rectangle.tmp, x, y);
	}
	
	public Rectangle getRect(Rectangle rect, float x, float y){
		return rect.setSize(width, height).setCenter(x + offsetx, y + offsety);
	}

	public void setSize(float size){
		width = height = size;
	}

	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}
}
