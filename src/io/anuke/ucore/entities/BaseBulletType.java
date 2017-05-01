package io.anuke.ucore.entities;

/**presumably you would extends BulletType and put in default bullet entity type.*/
public abstract class BaseBulletType<T extends BulletEntity>{
	public float lifetime = 100;
	public float speed = 1f;
	public int damage = 1;
	
	public abstract void draw(T b);
	public void update(T b){}
	public void removed(T b){}
}
