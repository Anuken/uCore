package io.anuke.ucore.aabb;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;

import io.anuke.ucore.function.CollisionMap;
import io.anuke.ucore.function.ContactFilter;
import io.anuke.ucore.function.ContactListener;

public class CollisionEngine{
	public static final float EPSILON = 0.0001f;
	public static final int maxCells = 3000;

	/** Number of physics iterations per call to update(). */
	public int iterations = 10;
	public Vector2 gravity = new Vector2();

	/** The cell size of the spatial hash. */
	private float cellsize = 100;
	private Array<Manifold> contacts = new Array<Manifold>();
	private Array<Collider> colliders = new Array<Collider>();
	private ContactListener listener;
	private ContactFilter filter;
	/**Used in specific places like maps - verifies if an object can be moved into a certain location.*/
	private CollisionMap cmap;
	/** Note that ArrayLists are used due to Arrays only having one iterator. */
	private ColliderPartition grid = new ColliderPartition();
	private ObjectSet<Collider> temp = new ObjectSet<Collider>();
	private IntSet collided = new IntSet();

	/** Get all the colliders in the list. The returned list can be modified.*/
	public Array<Collider> getAllColliders(){
		return colliders;
	}

	/** Add a collider to the list. */
	public void addCollider(Collider c){
		colliders.add(c);
	}
	
	/** Removes all colliders.*/
	public void clearColliders(){
		colliders.clear();
	}

	/** Removes a collider from the list. O(N) complexity, use with care. */
	public void removeCollider(Collider c){
		colliders.removeValue(c, true);
	}

	public void setContactListener(ContactListener listener){
		this.listener = listener;
	}

	public void setContactFilter(ContactFilter filter){
		this.filter = filter;
	}
	
	public void setCollisionMap(CollisionMap v){
		this.cmap = v;
	}

	public void setCellSize(float size){
		this.cellsize = size;
	}

	public float getCellSize(){
		return cellsize;
	}
	
	/**Find collisions and update spatial grid.*/
	public void updateCollisions(){
		
		//clear contacts
		Pools.freeAll(contacts);

		contacts.clear();

		//clear cells
		for(ArrayList<Collider> set : grid.cells()){
			set.clear();
		}

		//if there are too many cells, clear it so we can stop iterating on them
		if(grid.size() > maxCells){
			grid.clear();
		}
		
		//setup spatial grid and find collisions
		setupCells();

		//initialize contacts
		for(int i = 0; i < contacts.size; i++){
			contacts.get(i).init(gravity.x, gravity.y);
		}

		//resolve collisions by adding velocity
		for(int i = 0; i < iterations; i++){
			for(int c = 0; c < contacts.size; c++){
				contacts.get(c).resolve();
			}
		}
	}
	
	public void updateCollisionMap(float delta){
		for(Collider c : colliders){
			if(cmap != null)
				checkTileCollisions(c, delta);
		}
	}
	
	/**Apply force on objects.*/
	public void updateForces(float delta){
		//update forces and bounds
		for(Collider c : colliders){
			c.integrateForces(gravity.x, gravity.y, delta);
			c.clearForces();
			c.updateBounds();
		}

		//correct positions
		for(int i = 0; i < contacts.size; i++){
			contacts.get(i).positionalCorrection(cmap);
		}
	}
	
	/**Updates collisions and applies forces.*/
	public void update(float delta){
		updateCollisions();
		updateCollisionMap(delta);
		updateForces(delta);
	}
	
	/**Adds cells into spatial grid, updates collisions*/
	private void setupCells(){
		for(Collider c : colliders){
			c.updateBounds();
			
			collided.clear();

			Vector2 min = c.min;
			Vector2 max = c.max;

			int minx = (int) (min.x / cellsize), miny = (int) (min.y / cellsize);
			int maxx = (int) (max.x / cellsize), maxy = (int) (max.y / cellsize);

			for(int x = minx; x <= maxx; x++){
				for(int y = miny; y <= maxy; y++){
					ArrayList<Collider> set = grid.get(x, y);

					if(set == null){
						grid.putCell(x, y, (set = new ArrayList<Collider>()));
					}else{
						//check for collisions
						for(Collider other : set){
							if(other != c && !collided.contains(other.id)){
								checkCollisions(c, other);
							}
							collided.add(other.id);
						}
					}
					
					//add collider into cell
					set.add(c);
				}
			}
		}
	}
	
	/** Gets colliders that may be within a certain range. */
	public void getNearby(float cx, float cy, float w, float h, ObjectSet<Collider> out){
		int maxx = (int)((cx + w)/ cellsize), maxy = (int)((cy + h)/ cellsize), minx = (int)((cx - w)/ cellsize), miny = (int)((cy - h)/ cellsize);

		for(int x = minx; x <= maxx; x++){
			for(int y = miny; y <= maxy; y++){
				ArrayList<Collider> set = grid.get(x, y);
				if(set != null){
					for(Collider c : set){
						out.add(c);
					}
				}
			}
		}
	}
	
	private void checkTileCollisions(Collider collider, float delta){
		float newx = collider.x + collider.getVelocity().x * delta;
		float newy = collider.y + collider.getVelocity().y * delta;
		
		if(cmap.valid(collider, newx, collider.y)){
			collider.getVelocity().scl(-1f * collider.restitution, 1f);
		}
		
		if(cmap.valid(collider, collider.x, newy)){
			collider.getVelocity().scl(1f, -1f * collider.restitution);
		}
	}

	/** Check for collisions between two colliders */
	private boolean checkCollisions(Collider a, Collider b){

		// Ignore collisions between objects with infinite mass
		if(MathUtils.isEqual(a.im, 0) && MathUtils.isEqual(b.im, 0)){
			return false;

		}else if((filter == null || filter.collide(a, b)) && a.isOverlapping(b)){

			if(listener != null){
				listener.onContact(a, b);
				listener.onContact(b, a);
			}

			//don't add a contact for triggers
			if(a.trigger || b.trigger){
				return true;
			}else{
				//Get a manifold from the pool and add it
				Manifold c = Pools.obtain(Manifold.class).set(a, b);
				if(c.solve()){
					contacts.add(c);
					return true;
				}
			}
		}

		return false;
	}
}
