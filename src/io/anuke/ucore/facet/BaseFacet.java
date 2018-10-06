package io.anuke.ucore.facet;

public class BaseFacet extends Facet{
    public float layer = 0f;
    DrawFunc drawable;

    public BaseFacet(float layer, Sorter sort, DrawFunc draw){
        this.layer = layer;
        this.drawable = draw;
        this.sort(sort);
    }

    public BaseFacet(float layer, DrawFunc draw){
        this.layer = layer;
        this.drawable = draw;
        sort(Sorter.object);
    }

    public BaseFacet(DrawFunc draw){
        this.drawable = draw;
        sort(Sorter.object);
    }


    public BaseFacet(){

    }

    @Override
    public void reset(){

    }

    @Override
    public void draw(){
        Facets.instance().requestSort();

        if(drawable != null) drawable.draw(this);
    }

    @Override
    public Facet set(float x, float y){
        return this;
    }

    @Override
    public float getLayer(){
        return layer;
    }

    public interface DrawFunc{
        void draw(BaseFacet l);
    }
}
