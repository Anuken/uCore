package io.anuke.ucore.layer3D;

import com.badlogic.gdx.utils.Array;

/** Used for easily storing and freeing a list of LayeredObjects. */
public class LayerList{
    public Array<LayeredObject> objects = new Array<LayeredObject>();
    public LayerRenderer renderer;

    public LayerList(LayerRenderer renderer){
        this.renderer = renderer;
    }

    public LayeredObject get(int index){
        return objects.get(index);
    }

    public void add(LayeredObject object){
        renderer.add(object);
        objects.add(object);
    }

    /** Removes all the LayeredObjects and clears the list. */
    public void free(){
        for(LayeredObject object : objects)
            renderer.remove(object);

        objects.clear();
    }
}
