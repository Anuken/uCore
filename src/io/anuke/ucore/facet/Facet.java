package io.anuke.ucore.facet;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Facet implements Comparable<Facet>, Poolable{
    public Sorter provider = Sorter.tile;

    public abstract void draw();

    public abstract Facet set(float x, float y);

    public abstract float getLayer();

    public Facet sort(Sorter provider){
        this.provider = provider;
        return this;
    }

    public void add(FacetList list){
        list.add(this);
    }

    public void add(String name, FacetMap group){
        group.add(name, this);
    }

    public <T extends Facet> T add(){
        Facets.instance().add(this);
        return (T) this;
    }

    public void remove(){
        Facets.instance().remove(this);
    }

    public SpriteFacet sprite(){
        return (SpriteFacet) this;
    }

    public void onFree(){

    }

    public int compareTo(Facet o){
        return provider.compare(this, o);
    }
}
