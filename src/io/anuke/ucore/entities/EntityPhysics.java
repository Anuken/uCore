package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.anuke.ucore.entities.component.SolidTrait;
import io.anuke.ucore.entities.impl.BaseEntity;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;

import static io.anuke.ucore.entities.Entities.defaultGroup;
import static io.anuke.ucore.entities.Entities.entityLock;

public class EntityPhysics {
    private static final EntityCollisions collisions = new EntityCollisions();
    private static final Array<SolidTrait> array = new Array<>();
    private static final Rectangle r1 = new Rectangle();

    public static EntityCollisions collisions(){
        return collisions;
    }

    public static void initPhysics(float x, float y, float w, float h){
        for(EntityGroup group : Entities.getAllGroups()){
            if(group.useTree)
                group.setTree(x, y, w, h);
        }
    }

    public static void initPhysics(){
        initPhysics(0, 0, 0, 0);
    }

    public static void resizeTree(float x, float y, float w, float h){
        initPhysics(x, y, w, h);
    }

    public static void getNearby(EntityGroup<?> group, Rectangle rect, Consumer<SolidTrait> out){
        synchronized (entityLock) {
            if (!group.useTree)
                throw new RuntimeException("This group does not support quadtrees! Enable quadtrees when creating it.");
            group.tree().getIntersect(out, rect);
        }
    }

    public static Array<SolidTrait> getNearby(EntityGroup<?> group, Rectangle rect){
        synchronized (entityLock) {
            array.clear();
            getNearby(group, rect, array::add);
            return array;
        }
    }

    public static void getNearby(float x, float y, float size, Consumer<SolidTrait> out){
        getNearby(defaultGroup(), r1.setSize(size).setCenter(x, y), out);
    }

    public static void getNearby(EntityGroup<?> group, float x, float y, float size, Consumer<SolidTrait> out){
        getNearby(group, r1.setSize(size).setCenter(x, y), out);
    }

    public static Array<SolidTrait> getNearby(float x, float y, float size){
        return getNearby(defaultGroup(), r1.setSize(size).setCenter(x, y));
    }

    public static Array<SolidTrait> getNearby(EntityGroup<?> group, float x, float y, float size){
        return getNearby(group, r1.setSize(size).setCenter(x, y));
    }

    public static SolidTrait getClosest(EntityGroup<?> group, float x, float y, float range, Predicate<BaseEntity> pred){
        synchronized (entityLock) {
            SolidTrait closest = null;
            float cdist = 0f;
            Array<SolidTrait> entities = getNearby(group, x, y, range * 2f);
            for (int i = 0; i < entities.size; i++) {
                SolidTrait e = entities.get(i);
                if (!pred.test((BaseEntity) e))
                    continue;

                float dist = Vector2.dst(e.getX(), e.getY(), x, y);
                if (dist < range)
                    if (closest == null || dist < cdist) {
                        closest = e;
                        cdist = dist;
                    }
            }

            return closest;
        }
    }

    public static void collideGroups(EntityGroup<?> groupa, EntityGroup<?> groupb){
        collisions().collideGroups(groupa, groupb);
    }
}
