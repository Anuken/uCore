package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import io.anuke.ucore.entities.trait.Entity;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;
import io.anuke.ucore.threading.Threads;
import io.anuke.ucore.util.QuadTree;

public class EntityGroup<T extends Entity>{
    private static int lastid;
    private final boolean useTree;
    private final int id;
    private final Class<T> type;
    private final Array<T> entityWriteBackArray = new Array<>(false, 16);
    private final Array<T> entityArray = new Array<>(false, 16);
    private final Array<T> entitiesToRemove = new Array<>(false, 16);
    private final Array<T> entitiesToAdd = new Array<>(false, 16);
    private IntMap<T> map;
    private QuadTree<T> tree;
    private Consumer<T> removeListener;
    private Consumer<T> addListener;

    public EntityGroup(Class<T> type, boolean useTree){
        this.useTree = useTree;
        this.id = lastid++;
        this.type = type;
    }

    public boolean useTree(){
        return useTree;
    }

    public void setRemoveListener(Consumer<T> removeListener){
        this.removeListener = removeListener;
    }

    public void setAddListener(Consumer<T> addListener){
        this.addListener = addListener;
    }

    public EntityGroup<T> enableMapping(){
        map = new IntMap<>();
        return this;
    }

    public boolean mappingEnabled(){
        return map != null;
    }

    public Class<T> getType(){
        return type;
    }

    public int getID(){
        return id;
    }

    public synchronized void updateEvents(){
        Threads.assertLogic();

        for(T e : entitiesToAdd){
            if(e == null)
                continue;
            entityArray.add(e);
            e.added();

            if(map != null){
                map.put(e.getID(), e);
            }
        }

        entitiesToAdd.clear();

        for(T e : entitiesToRemove){
            entityArray.removeValue(e, true);
            if(map != null){
                map.remove(e.getID());
            }
            e.removed();
        }

        entitiesToRemove.clear();

        synchronized(entityWriteBackArray){
            entityWriteBackArray.clear();
            entityWriteBackArray.addAll(entityArray);
        }
    }

    public synchronized T getByID(int id){
        if(map == null) throw new RuntimeException("Mapping is not enabled for group " + id + "!");
        return map.get(id);
    }

    public synchronized void removeByID(int id){
        if(map == null) throw new RuntimeException("Mapping is not enabled for group " + id + "!");
        T t = map.get(id);
        if(t != null){ //remove if present in map already
            remove(t);
        }else{ //maybe it's being queued?
            for(T check : entitiesToAdd){
                if(check.getID() == id){ //if it is indeed queued, remove it
                    entitiesToAdd.removeValue(check, true);
                    if(removeListener != null){
                        removeListener.accept(check);
                    }
                    break;
                }
            }
        }
    }

    public QuadTree tree(){
        return tree;
    }

    public void setTree(float x, float y, float w, float h){
        Threads.assertLogic();

        tree = new QuadTree<>(Entities.maxLeafObjects, new Rectangle(x, y, w, h));
    }

    public boolean isEmpty(){
        return entityArray.size == 0;
    }

    public int size(){
        return entityArray.size;
    }

    public int count(Predicate<T> pred){

        if(Threads.isLogic()){
            int count = 0;
            for(T t : entityArray){
                if(pred.test(t)) count++;
            }
            return count;
        }else{
            synchronized(entityWriteBackArray){
                int count = 0;

                for(T t : entityWriteBackArray){
                    if(pred.test(t)) count++;
                }

                return count;
            }
        }
    }

    public synchronized void add(T type){
        if(type == null) throw new RuntimeException("Cannot add a null entity!");
        if(type.getGroup() != null) return;
        type.setGroup(this);
        entitiesToAdd.add(type);

        if(mappingEnabled()){
            map.put(type.getID(), type);
        }

        if(addListener != null){
            addListener.accept(type);
        }
    }

    public synchronized void remove(T type){
        if(type == null) throw new RuntimeException("Cannot remove a null entity!");
        type.setGroup(null);
        entitiesToRemove.add(type);

        if(removeListener != null){
            removeListener.accept(type);
        }
    }

    public synchronized void clear(){
        for(T entity : entityArray)
            entity.setGroup(null);

        for(T entity : entitiesToAdd)
            entity.setGroup(null);

        for(T entity : entitiesToRemove)
            entity.setGroup(null);

        entitiesToAdd.clear();
        entitiesToRemove.clear();
        entityArray.clear();
        if(map != null)
            map.clear();
    }

    /**Returns the logic-only array for iteration.*/
    public Array<T> all(){
        Threads.assertLogic();

        return entityArray;
    }

    /**Iterates through each entity in the writeback array.
     * This should be called on the graphics thread only.*/
    public void forEach(Consumer<T> cons){
        Threads.assertGraphics();

        synchronized(entityWriteBackArray){
            for(T t : entityWriteBackArray){
                cons.accept(t);
            }
        }
    }
}
