package io.anuke.ucore.entities.trait;

public interface DrawTrait extends io.anuke.ucore.entities.trait.Entity {

    default float drawSize(){
        return 20f;
    }

    void draw();
}
