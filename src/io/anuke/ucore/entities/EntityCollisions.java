package io.anuke.ucore.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import io.anuke.ucore.function.TileCollider;
import io.anuke.ucore.function.TileHitboxProvider;
import io.anuke.ucore.jbump.JBWorld;
import io.anuke.ucore.jbump.JBWorld.JBItem;
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
    private JBWorld world = new JBWorld();
    private Array<JBItem> items = new Array<>();
    private Rectangle tmp = new Rectangle();
    private TileCollider collider;
    private TileHitboxProvider hitboxProvider;
    private GridPoint2 point = new GridPoint2();
    private Vector2 vector = new Vector2();

    //entity collisions
    private IntSet collided = new IntSet();

    public void setCollider(float tilesize, TileCollider collider, TileHitboxProvider hitbox){
        this.tilesize = tilesize;
        this.collider = collider;
        this.hitboxProvider = hitbox;
    }

    public void setCollider(float tilesize, TileCollider collider){
        setCollider(tilesize, collider, (x, y, out) -> {
            out.setSize(tilesize).setCenter(x*tilesize, y*tilesize);
        });
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

        entity.x = rect.x + box.width / 2 - box.offsetx*2;
        entity.y = rect.y + box.height / 2 - box.offsety*2;

        /*
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        Hitbox hitbox = entity.hitboxTile;
        items.clear();
        float x = entity.x + hitbox.offsetx, y = entity.y  + hitbox.offsety;

        //TODO very inefficient

        JBItem is = new JBItem();
        items.add(is);
        world.add(is, x-hitbox.width/2, y-hitbox.height/2, hitbox.width, hitbox.height);

        int tilex = Mathf.scl2(x, tilesize), tiley = Mathf.scl2(y, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx+tilex, wy = dy+tiley;
                if(collider.solid(wx, wy)){

                    hitboxProvider.getHitbox(wx, wy, tmp);
                    JBItem tile = new JBItem();
                    world.add(tile, tmp.x, tmp.y, tmp.width, tmp.height);
                    items.add(tile);
                }
            }
        }

        Result result = world.move(is, x + deltax-hitbox.width/2, y + deltay-hitbox.height/2, CollisionFilter.defaultFilter);
        entity.set(result.goalX - hitbox.offsetx+hitbox.width/2, result.goalY - hitbox.offsety+hitbox.height/2);

        for(JBItem item : items){
            world.remove(item);
        }
        */
    }

    public boolean overlapsTile(Rectangle rect){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        rect.getCenter(vector);
        int r = 1;

        //assumes tilesize is centered
        int tilex = Mathf.scl2(vector.x, tilesize);
        int tiley = Mathf.scl2(vector.y, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx + tilex, wy = dy + tiley;
                if(collider.solid(wx, wy)){
                    hitboxProvider.getHitbox(wx, wy, Rectangle.tmp2);

                    if(Rectangle.tmp2.overlaps(rect)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void updatePhysics(EntityGroup<?> group){
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

        Rectangle r1 = a.hitbox.getRect(Rectangle.tmp, a.x, a.y);
        Rectangle r2 = b.hitbox.getRect(Rectangle.tmp2, b.x, b.y);

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

            ((QuadTreeObject) entity).getBoundingBox(Rectangle.tmp2);

            groupb.tree().getIntersect(c -> {
                if(!collided.contains(c.id))
                    checkCollide(entity, c);
            }, Rectangle.tmp2);

            collided.add(entity.id);
        }
    }
}
