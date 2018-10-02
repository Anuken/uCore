package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import io.anuke.ucore.entities.trait.Entity;
import io.anuke.ucore.entities.trait.SolidTrait;
import io.anuke.ucore.function.TileCollider;
import io.anuke.ucore.function.TileHitboxProvider;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Physics;
import io.anuke.ucore.util.QuadTree;

import static io.anuke.ucore.entities.Entities.entityLock;

public class EntityCollisions{
    //range for tile collision scanning
    private static final int r = 2;
    //move in 1-unit chunks
    private static final float seg = 1f;

    //tile collisions
    private float tilesize;
    private Rectangle tmp = new Rectangle();
    private TileCollider collider;
    private TileHitboxProvider hitboxProvider;
    private Vector2 vector = new Vector2(), vec1 = new Vector2(), vec2 = new Vector2();
    private Vector2 l1 = new Vector2();
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    //entity collisions
    private IntSet collided = new IntSet();
    private Array<SolidTrait> arrOut = new Array<>();

    public void setCollider(float tilesize, TileCollider collider, TileHitboxProvider hitbox){
        this.tilesize = tilesize;
        this.collider = collider;
        this.hitboxProvider = hitbox;
    }

    public void setCollider(float tilesize, TileCollider collider){
        setCollider(tilesize, collider, (x, y, out) -> out.setSize(tilesize).setCenter(x * tilesize, y * tilesize));
    }

    public void move(SolidTrait entity, float deltax, float deltay){

        boolean movedx = false;

        while(Math.abs(deltax) > 0 || !movedx){
            movedx = true;
            moveInternal(entity, Math.min(Math.abs(deltax), seg) * Mathf.sign(deltax), 0, true);

            if(Math.abs(deltax) >= seg){
                deltax -= seg * Mathf.sign(deltax);
            }else{
                deltax = 0f;
            }
        }

        boolean movedy = false;

        while(Math.abs(deltay) > 0 || !movedy){
            movedy = true;
            moveInternal(entity, 0, Math.min(Math.abs(deltay), seg) * Mathf.sign(deltay), false);

            if(Math.abs(deltay) >= seg){
                deltay -= seg * Mathf.sign(deltay);
            }else{
                deltay = 0f;
            }
        }
    }

    public void moveInternal(SolidTrait entity, float deltax, float deltay, boolean x){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        Rectangle rect = r1;
        entity.getHitboxTile(rect);
        entity.getHitboxTile(r2);
        rect.x += deltax;
        rect.y += deltay;

        int tilex = Mathf.scl2(rect.x + rect.width / 2, tilesize), tiley = Mathf.scl2(rect.y + rect.height / 2, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx + tilex, wy = dy + tiley;
                if(collider.solid(wx, wy)){

                    hitboxProvider.getHitbox(wx, wy, tmp);

                    if(tmp.overlaps(rect)){
                        Vector2 v = Physics.overlap(rect, tmp, x);
                        rect.x += v.x;
                        rect.y += v.y;
                    }
                }
            }
        }

        entity.setX(entity.getX() + rect.x - r2.x);
        entity.setY(entity.getY() + rect.y - r2.y);
    }

    public boolean overlapsTile(Rectangle rect){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        rect.getCenter(vector);
        int r = 1;

        //assumes tiles are centered
        int tilex = Mathf.scl2(vector.x, tilesize);
        int tiley = Mathf.scl2(vector.y, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx + tilex, wy = dy + tiley;
                if(collider.solid(wx, wy)){
                    hitboxProvider.getHitbox(wx, wy, r2);

                    if(r2.overlaps(rect)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public <T extends Entity> void updatePhysics(EntityGroup<T> group){
        collided.clear();

        QuadTree tree = group.tree();

        synchronized(entityLock){
            tree.clear();
        }

        for(Entity entity : group.all()){
            if(entity instanceof SolidTrait){
                SolidTrait s = (SolidTrait) entity;
                s.lastPosition().set(s.getX(), s.getY());

                synchronized(entityLock){
                    tree.insert(s);
                }
            }
        }

    }

    private Vector2 cline(float lx1, float ly1, float lx2, float ly2, float x0, float y0){
        float A1 = ly2 - ly1;
        float B1 = lx1 - lx2;
        float C1 = (ly2 - ly1)*lx1 + (lx1 - lx2)*ly1;
        float C2 = -B1*x0 + A1*y0;
        float det = A1*A1 - -B1*B1;
        float cx, cy;
        if(det != 0){
            cx = ((A1*C1 - B1*C2)/det);
            cy = ((A1*C2 - -B1*C1)/det);
        }else{
            cx = x0;
            cy = y0;
        }
        return vector.set(cx, cy);
    }

    private void checkCollide(Entity entity, Entity other){

        SolidTrait a = (SolidTrait) entity;
        SolidTrait b = (SolidTrait) other;

        a.getHitbox(this.r1);
        b.getHitbox(this.r2);

        r1.x += (a.getVelocity().x);
        r1.y += (a.getVelocity().y);
        r2.x += (b.getVelocity().x);
        r2.y += (b.getVelocity().y);

        float vax = a.getX() + a.getVelocity().x;
        float vay = a.getY() + a.getVelocity().y;
        float vbx = b.getX() + b.getVelocity().x;
        float vby = b.getY() + b.getVelocity().y;

        if(a != b && a.collides(b) && b.collides(a)){
            l1.set(a.getX(), a.getY());
            boolean collide = r1.overlaps(r2) || collide(r1.x, r1.y, r1.width, r1.height, vax, vay,
            r2.x, r2.y, r2.width, r2.height, vbx, vby, l1);
            if(collide){
                a.collision(b, l1.x, l1.y);
                b.collision(a, l1.x, l1.y);

                if(a.movable() && b.movable()){
                    Vector2 vec = Physics.overlap(r1, r2, true);
                    float msum = a.getMass() + b.getMass();
                    a.moveBy(vec.x * (1f - a.getMass() / msum), vec.y * (1f - a.getMass() / msum));
                    b.moveBy(-vec.x * (1f - b.getMass() / msum), -vec.y * (1f - b.getMass() / msum));
                    a.applyImpulse(vec.x, vec.y);
                    b.applyImpulse(-vec.x, -vec.y);
                }
            }
        }

/*
        SolidTrait circle1 = (SolidTrait) entity;
        SolidTrait circle2 = (SolidTrait) other;

        circle1.getHitbox(this.r1);
        circle2.getHitbox(this.r2);

        if(circle1 == circle2 || !circle1.collides(circle2) || !circle2.collides(circle1)) return;

        float rad1 = r1.width, rad2 = r2.width;
        float vx = circle1.getVelocity().x - circle2.getVelocity().y, vy = circle1.getVelocity().y - circle2.getVelocity().y;

        Vector2 d = cline(circle1.getX(), circle1.getY(),
        circle1.getX() + vx, circle1.getY() + vy, circle2.getX(), circle2.getY());
        float closestdistsq = Mathf.sqr(circle2.getX() - d.x) + Mathf.sqr(circle2.getY() - d.y);

        if(closestdistsq <= Mathf.sqr(rad1 + rad2)){
            // a collision has occurred
            float backdist = Mathf.sqrt(Mathf.sqr(rad1 + rad2) - closestdistsq);
            float movementvectorlength = Mathf.sqrt(Mathf.sqr(vx) + Mathf.sqr(vy));
            //collision x/y
            float c_x = d.x - backdist * (vx / movementvectorlength);
            float c_y = d.y - backdist * (vy / movementvectorlength);

            float collisiondist = Mathf.sqrt(Mathf.sqr(circle2.getX() - c_x) + Mathf.sqr(circle2.getY() - c_y));
            float n_x = (circle2.getX() - c_x) / collisiondist;
            float n_y = (circle2.getY() - c_y) / collisiondist;
            float p = 2 * (vx * n_x + vy * n_y) / (circle1.getMass() + circle2.getMass());

            float vx1 = circle1.getVelocity().x - p * circle1.getMass() * n_x;
            float vy1 = circle1.getVelocity().y - p * circle1.getMass() * n_y;
            float vx2 = circle2.getVelocity().x + p * circle2.getMass() * n_x;
            float vy2 = circle2.getVelocity().y + p * circle2.getMass() * n_y;

            circle1.getVelocity().set(vx1, vy1);
            circle2.getVelocity().set(vx2, vy2);

            circle1.collision(circle2, c_x, c_y);
            circle2.collision(circle1, c_x, c_y);
        }

        /*
        float v1x = a.getVelocity().x, v1y = a.getVelocity().y, v2x = b.getVelocity().x, v2y = b.getVelocity().y;
        float cx1 = a.getX(), cy1 = a.getY(), cx2 = b.getX(), cy2 = b.getY();

        float d = Mathf.sqrt(Mathf.sqr(cx1 - cx2) + Mathf.sqr(cy1 - cy2));
        float nx = (cx2 - cx1) / d;
        float ny = (cy2 - cy1) / d;
        float p = 2 * (v1x * nx + v1y * n_y - v2x * nx - v2y * n_y) / (a.getMass() + b.getMass());
        vx1 = circle1.vx - p * circle1.mass * n_x;
        vy1 = circle1.vy - p * circle1.mass * n_y;
        vx2 = circle2.vx + p * circle2.mass * n_x;
        vy2 = circle2.vy + p * circle2.mass * n_y;

        /*

        r1.x += (a.lastPosition().x - a.getX());
        r1.y += (a.lastPosition().y - a.getY());
        r2.x += (b.lastPosition().x - b.getX());
        r2.y += (b.lastPosition().y - b.getY());

        float vax = a.getX() - a.lastPosition().x;
        float vay = a.getY() - a.lastPosition().y;
        float vbx = b.getX() - b.lastPosition().x;
        float vby = b.getY() - b.lastPosition().y;

        if(a != b && a.collides(b) && b.collides(a)){
            l1.set(a.getX(), a.getY());
            boolean collide = r1.overlaps(r2) || collide(r1.x, r1.y, r1.width, r1.height, vax, vay,
                    r2.x, r2.y, r2.width, r2.height, vbx, vby, l1);
            if(collide){
                a.collision(b, l1.x, l1.y);
                b.collision(a, l1.x, l1.y);
            }
        }*/
    }

    private boolean collide(float x1, float y1, float w1, float h1, float vx1, float vy1,
                            float x2, float y2, float w2, float h2, float vx2, float vy2, Vector2 out){
        float px = vx1, py = vy1;

        vx1 -= vx2;
        vy1 -= vy2;

        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        if(vx1 > 0.0f){
            xInvEntry = x2 - (x1 + w1);
            xInvExit = (x2 + w2) - x1;
        }else{
            xInvEntry = (x2 + w2) - x1;
            xInvExit = x2 - (x1 + w1);
        }

        if(vy1 > 0.0f){
            yInvEntry = y2 - (y1 + h1);
            yInvExit = (y2 + h2) - y1;
        }else{
            yInvEntry = (y2 + h2) - y1;
            yInvExit = y2 - (y1 + h1);
        }

        float xEntry, yEntry;
        float xExit, yExit;

        xEntry = xInvEntry / vx1;
        xExit = xInvExit / vx1;

        yEntry = yInvEntry / vy1;
        yExit = yInvExit / vy1;

        float entryTime = Math.max(xEntry, yEntry);
        float exitTime = Math.min(xExit, yExit);

        if(entryTime > exitTime || xExit < 0.0f || yExit < 0.0f || xEntry > 1.0f || yEntry > 1.0f){
            return false;
        }else{
            float dx = x1 + w1 / 2f + px * entryTime;
            float dy = y1 + h1 / 2f + py * entryTime;

            out.set(dx, dy);

            return true;
        }
    }

    public void collideGroups(EntityGroup<?> groupa, EntityGroup<?> groupb){
        collided.clear();

        for(Entity entity : groupa.all()){
            if(!(entity instanceof SolidTrait) || collided.contains(entity.getID()))
                continue;

            SolidTrait solid = (SolidTrait) entity;

            solid.getHitbox(r1);
            r1.x += (solid.getVelocity().x + solid.getX());
            r1.y += (solid.getVelocity().y + solid.getY());

            solid.getHitbox(r2);
            r2.merge(r1);

            synchronized(Entities.entityLock){

                arrOut.clear();
                groupb.tree().getIntersect(arrOut, r2);

                for(SolidTrait sc : arrOut){
                    if(!collided.contains(sc.getID())){
                        checkCollide(entity, sc);
                    }
                }
            }

            collided.add(entity.getID());
        }
    }
}
