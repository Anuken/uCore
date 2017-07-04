package io.anuke.ucore.aabb;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Collider{
	private static int lastID = 0;
	
	/**These are default values used for colliders upon creation.*/
	public static float defaultRestitution = 0f;
	public static float defaultDrag = 0.1f;
	public static float defaultStaticFriction = 1f;
	public static float defaultDynamicFriction = 0.3f;
	public static float defaultMass = 1f;
	
	/**Unique collider ID.*/
	public transient final int id;
	/**Position and size of the collider.*/
	public float x, y, w, h;
	/** Collider bouncyness. */
	public float restitution;
	/** The friction this collider has to overcome to start moving. */
	public float staticFriction;
	/** The friction this collider has when colliding with another collider. */
	public float dynamicFriction;
	/**The fraction of velocity that is lost every step.*/
	public float drag;
	/**Mass of the collider.*/
	public float mass;
	/**Maximum collider velocity. Set to any value < 0 to disable.*/
	public float maxVelocity = -1;
	/**Whether or not this collider will physically move other objects. Collisions events will still fire. */
	public boolean trigger = false;
	/**If set to true, this object will be immovable and not be affected by gravity.*/
	public boolean kinematic = false;
	/**User data.*/
	public transient Object data;
	
	protected Vector2 velocity = new Vector2(); 
	protected transient Vector2 force = new Vector2();
	protected transient Vector2 max = new Vector2(), min = new Vector2();
	protected float im; //inverse mass
	
	public Collider(){
		this(0, 0, 10, 10, defaultMass, defaultDrag, defaultRestitution);
	}
	
	public Collider(float size){
		this(0, 0, size, size, defaultMass, defaultDrag, defaultRestitution);
	}
	
	public Collider(float w, float h){
		this(0, 0, w, h, defaultMass, defaultDrag, defaultRestitution);
	}
	
	public Collider(float x, float y, float size){
		this(x, y, size, size, defaultMass, defaultDrag, defaultRestitution);
	}
	
	public Collider(float x, float y, float w, float h){
		this(x, y, w, h, defaultMass, defaultDrag, defaultRestitution);
	}
	
	public Collider(float x, float y, float w, float h, float mass){
		this(x, y, w, h, mass, defaultDrag, defaultRestitution);
	}

	public Collider(float x, float y, float w, float h, float mass, float drag, float restitution) {
		this.im = 1f / mass;

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.mass = mass;
		this.drag = drag;
		this.restitution = restitution;
		this.staticFriction = defaultStaticFriction;
		this.dynamicFriction = defaultDynamicFriction;
		this.id = lastID++;
	}
	
	/**Returns the bounds of this collider. Note that {@link Rectangle#tmp} is used and no objects are created.*/
	public Rectangle getBounds(){
		return Rectangle.tmp.set(x - w / 2, y - h/2, w, h);
	}
	
	public void setSize(float w, float h){
		this.w = w;
		this.h = h;
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

	public Vector2 getForce(){
		return force;
	}

	public Vector2 getVelocity(){
		return velocity;
	}

	public void setVelocity(float x, float y){
		velocity.set(x, y);
	}

	public boolean isOverlapping(Collider other){
		if(max.x < other.min.x || min.x > other.max.x){
			return false;

		}else if(max.y < other.min.y || min.y > other.max.y){
			return false;

		}else{
			return true;
		}
	}

	public void updateBounds(){
		min.x = x - w / 2;
		max.x = x + w / 2;
		min.y = y - h / 2;
		max.y = y + h / 2;
		im = kinematic ? 0 : 1f/mass;
	}

	/** Integrate velocity and gravity into the AABB's position.*/
	public void integrateForces(float gravityx, float gravityy, float delta){
		if(!kinematic){
			x += velocity.x * delta * 60f;
			y += velocity.y * delta * 60f;
			
			if(maxVelocity > 0)
			velocity.limit(maxVelocity);
			
			velocity.x += (force.x * im * delta * 60f + gravityx * delta * 60) / 2;
			velocity.y += (force.y * im * delta * 60f + gravityy * delta * 60) / 2;
			
			velocity.scl(1f - drag * delta * 60f);
		}
	}

	public void applyImpulse(float x, float y){
		velocity.x += im * x;
		velocity.y += im * y;
	}

	public void applyForce(float x, float y){
		force.x += x;
		force.y += y;
	}

	public void clearForces(){
		force.x = 0;
		force.y = 0;
	}
	
	public String toString(){
		return "Collider " + id;
	}

}
