package io.anuke.ucore.facet;

import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.graphics.Draw;

public class FacetLayers{
    public static final FacetLayer

            shadow = new FacetLayer("shadow", Sorter.shadow, 0){

        @Override
        public void end(){
            Draw.color(0, 0, 0, 0.13f);
            Graphics.flushSurface();
            Draw.color();
        }
    },
            light = new FacetLayer("light", Sorter.light, 6){

            },
            darkness = new FacetLayer("darkness", Sorter.dark, 0){

            };
}
