package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.anuke.ucore.entities.trait.Entity;
import io.anuke.ucore.entities.trait.SolidTrait;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;
import io.anuke.ucore.util.ThreadArray;

import static io.anuke.ucore.entities.Entities.defaultGroup;
import static io.anuke.ucore.entities.Entities.entityLock;

public class EntityQuery{
    private static final EntityCollisions collisions = new EntityCollisions();
    private static final ThreadArray<SolidTrait> array = new ThreadArray<>();
    private static final Rectangle r1 = new Rectangle();

    public static EntityCollisions collisions(){
        return collisions;
    }

    public static void init(float x, float y, float w, float h){
        for(EntityGroup group : Entities.getAllGroups()){
            if(group.useTree){
                group.setTree(x, y, w, h);
            }
        }
    }

    public static void init(){
        init(0, 0, 0, 0);
    }

    public static void resizeTree(float x, float y, float w, float h){
        init(x, y, w, h);
    }

    public static void getNearby(EntityGroup<?> group, Rectangle rect, Consumer<SolidTrait> out){
        synchronized(entityLock){
            if(!group.useTree)
                throw new RuntimeException("This group does not support quadtrees! Enable quadtrees when creating it.");
            group.tree().getIntersect(out, rect);
        }
    }

    public static Array<SolidTrait> getNearby(EntityGroup<?> group, Rectangle rect){
        synchronized(entityLock){
            array.clear();
            if(!group.useTree)
                throw new RuntimeException("This group does not support quadtrees! Enable quadtrees when creating it.");
            group.tree().getIntersect(array, rect);
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

    public static <T extends Entity> T getClosest(EntityGroup<T> group, float x, float y, float range, Predicate<T> pred){
        synchronized(entityLock){
            T closest = null;
            float cdist = 0f;
            Array<SolidTrait> entities = getNearby(group, x, y, range * 2f);
            for(int i = 0; i < entities.size; i++){
                T e = (T) entities.get(i);
                if(!pred.test(e))
                    continue;

                float dist = Vector2.dst(e.getX(), e.getY(), x, y);
                if(dist < range)
                    if(closest == null || dist < cdist){
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
