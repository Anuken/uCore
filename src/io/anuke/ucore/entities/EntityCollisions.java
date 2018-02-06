package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import io.anuke.ucore.function.TileCollider;
import io.anuke.ucore.function.TileHitboxProvider;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Physics;
import io.anuke.ucore.util.QuadTree;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class EntityCollisions {
    //range for tile collision scanning
    private static final int r = 2;
    //move in 1-unit chunks
    private static final float seg = 1f;

    //tile collisions
    private float tilesize;
    private Rectangle tmp = new Rectangle();
    private TileCollider collider;
    private TileHitboxProvider hitboxProvider;
    private Vector2 vector = new Vector2();
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    //entity collisions
    private IntSet collided = new IntSet();

    public void setCollider(float tilesize, TileCollider collider, TileHitboxProvider hitbox){
        this.tilesize = tilesize;
        this.collider = collider;
        this.hitboxProvider = hitbox;
    }

    public void setCollider(float tilesize, TileCollider collider){
        setCollider(tilesize, collider, (x, y, out) -> out.setSize(tilesize).setCenter(x*tilesize, y*tilesize));
    }

    public void move(SolidEntity entity, float deltax, float deltay){

        while(Math.abs(deltax) > 0){
            moveInternal(entity, Math.min(Math.abs(deltax), seg) * Mathf.sign(deltax), 0, true);

            if(Math.abs(deltax) >= seg) {
                deltax -= seg * Mathf.sign(deltax);
            }else{
                deltax = 0f;
            }
        }

        while(Math.abs(deltay) > 0){
            moveInternal(entity, 0, Math.min(Math.abs(deltay), seg) * Mathf.sign(deltay), false);

            if(Math.abs(deltay) >= seg) {
                deltay -= seg * Mathf.sign(deltay);
            }else{
                deltay = 0f;
            }
        }
    }

    public void moveInternal(SolidEntity entity, float deltax, float deltay, boolean x){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        Hitbox box = entity.hitboxTile;
        Rectangle rect = box.getRect(entity.x + deltax, entity.y + deltay);

        int tilex = Mathf.scl2(rect.x + rect.width/2, tilesize), tiley = Mathf.scl2(rect.y + rect.height/2, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx+tilex, wy = dy+tiley;
                if(collider.solid(wx, wy)){

                    hitboxProvider.getHitbox(wx, wy, tmp);

                    if(tmp.overlaps(rect)){
                        Vector2 v = Physics.overlap(rect, tmp);
                        if(x) rect.x += v.x;
                        if(!x) rect.y += v.y;
                    }
                }
            }
        }

        entity.x = rect.x + box.width / 2 - box.offsetx;
        entity.y = rect.y + box.height / 2 - box.offsety;
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

    public void updatePhysics(EntityGroup<?> group){
        collided.clear();

        QuadTree<SolidEntity> tree = group.tree();

        tree.clear();

        for(Entity entity : group.all()){
            if(entity instanceof SolidEntity){
                tree.insert((SolidEntity) entity);
            }
        }
    }

    private boolean checkCollide(Entity entity, Entity other){
        SolidEntity a = (SolidEntity) entity;
        SolidEntity b = (SolidEntity) other;

        Rectangle r1 = a.hitbox.getRect(this.r1, a.x, a.y);
        Rectangle r2 = b.hitbox.getRect(this.r2, b.x, b.y);

        if(a != b && a.collides(b) && b.collides(a) && r1.overlaps(r2)){
            a.collision(b);
            b.collision(a);
            return true;
        }

        return false;
    }

    public void collideGroups(EntityGroup<?> groupa, EntityGroup<?> groupb){
        collided.clear();

        for(Entity entity : groupa.all()){
            if(!(entity instanceof SolidEntity))
                continue;
            if(collided.contains(entity.id))
                continue;

            ((QuadTreeObject) entity).getBoundingBox(r2);

            groupb.tree().getIntersect(c -> {
                if(!collided.contains(c.id))
                    checkCollide(entity, c);
            }, r2);

            collided.add(entity.id);
        }
    }
}
