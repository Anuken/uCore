package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

/**presumably you would extends BulletType and put in default bullet entity type.*/
public abstract class BaseBulletType<T extends BulletEntity>{
	public float lifetime = 100;
	public float speed = 1f;
	public int damage = 1;
	public float hitsize = 4;
	
	protected Vector2 vector = new Vector2();
	
	public abstract void draw(T b);
	public void update(T b){}
	public void removed(T b){}
}
