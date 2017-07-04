package io.anuke.ucore.aabb;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.function.CollisionMap;


public class Manifold{
	public Collider a,b;
	public float e, sf, df, penetration;
	public Vector2 normal = new Vector2();
	
	public Manifold set(Collider a, Collider b){
		this.a = a;
		this.b = b;
        e = Math.min(a.restitution, b.restitution);
        sf = 0;
        df = 0;

        normal.set(0, 0);
        penetration = 0;
        
        return this;
	}
	
	public void init(float gravityx, float gravityy){

        sf = (float)Math.sqrt(a.staticFriction * b.staticFriction);
        df = (float)Math.sqrt(a.dynamicFriction * b.dynamicFriction);

        // Figure out whether this is a resting collision, if so do not apply
        // any restitution
        float rx = b.velocity.x - a.velocity.x,
            ry = b.velocity.y - a.velocity.y;

        if ((rx * rx + ry * ry) < (gravityx * gravityx + gravityy * gravityy) + CollisionEngine.EPSILON) {
            e = 0.0f;
        }

    }

    /**
      * Solve the SAT for two AABBs
      */
	public boolean solve(){
		
        // Vector from A to B
        float nx = a.x - b.x,
            ny = a.y - b.y;

        // Calculate half extends along x axis
        float aex = (a.max.x - a.min.x) / 2,
            bex = (b.max.x - b.min.x) / 2;

        // Overlap on x axis
        float xoverlap = aex + bex - Math.abs(nx);
        if (xoverlap > 0) {

            // Calculate half extends along y axis
            float aey = (a.max.y - a.min.y) / 2,
                bey = (b.max.y - b.min.y) / 2;

            // Overlap on x axis
            float yoverlap = aey + bey - Math.abs(ny);
            if (!MathUtils.isEqual(yoverlap, 0)) {

                // Find out which axis is the axis of least penetration
                if (xoverlap < yoverlap) {

                    // Point towards B knowing that n points from A to B
                    normal.x = nx < 0 ? 1 : -1;
                    normal.y = 0;
                    penetration = xoverlap;
                    return true;

                } else {

                    // Point towards B knowing that n points from A to B
                    normal.x = 0;
                    normal.y = ny < 0 ? 1 : -1;
                    penetration = yoverlap;
                    return true;

                }

            }

        }

        return false;

    }

    /**
      * Resolves a collision by applying a impulse to each of the AABB's
      * involved.
      */
	public void resolve(){

        // Relative velocity from a to b
        float rx = b.velocity.x - a.velocity.x,
            ry = b.velocity.y - a.velocity.y,
            velAlongNormal = rx * normal.x + ry * normal.y;

        // If the velocities are separating do nothing
        if (velAlongNormal > 0 ) {
            return;

        } else {

            // Correct penetration
            float j = -(1.0f + e) * velAlongNormal;
            j /= (a.im + b.im);

            // Apply the impulse each box gets a impulse based on its mass
            // ratio
            a.applyImpulse(-j * normal.x, -j * normal.y);
            b.applyImpulse(j * normal.x, j * normal.y);

            // Apply Friction
            float tx = rx - (normal.x * velAlongNormal),
                ty = ry - (normal.y * velAlongNormal),
                tl = (float)Math.sqrt(tx * tx + ty * ty);

            if (tl > CollisionEngine.EPSILON) {
                tx /= tl;
                ty /= tl;
            }

            float jt = -(rx * tx + ry * ty);
            jt /= (a.im + b.im);

            // Don't apply tiny friction impulses
            if (Math.abs(jt) < CollisionEngine.EPSILON) {
                return;
            }

            if (Math.abs(jt) < j * sf) {
                tx = tx * jt;
                ty = ty * jt;

            } else {
                tx = tx * -j * df;
                ty = ty * -j * df;
            }

            a.applyImpulse(-tx, -ty);
            b.applyImpulse(tx, ty);

        }

    }

    /**
      * This will prevent objects from sinking into each other when they're
      * resting.
      */
	public void positionalCorrection(CollisionMap v){

        float percent = 0.7f,
            slop = 0.05f,
            m = Math.max(penetration - slop, 0.0f) / (a.im + b.im);

        // Apply correctional impulse
        float cx = m * normal.x * percent,
            cy = m * normal.y * percent;
        
        if(!b.kinematic && (v == null || v.valid(b, b.x + cx*b.im, b.y + cy*b.im))){
        	b.x += cx * b.im;
        	b.y += cy * b.im;
        }
        
        if(!a.kinematic && (v == null || v.valid(a, a.x - cx*a.im, a.y - cy*a.im))){
        	a.x -= cx * a.im;
        	a.y -= cy * a.im;
        }

    }
}
