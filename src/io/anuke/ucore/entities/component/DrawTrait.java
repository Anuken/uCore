package io.anuke.ucore.entities.component;

public interface DrawTrait extends Entity {

    default float drawSize(){
        return 20f;
    }

    void draw();
}
