package io.anuke.ucore.entities.trait;

import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.entities.EntityGroup;

public interface Entity extends PosTrait, MutPosTrait {

    int getID();
    void resetID(int id);

    default void update(){}
    default void removed(){}
    default void added(){}

    default EntityGroup targetGroup(){
        return Entities.defaultGroup();
    }

    default void add(){
        targetGroup().add(this);
    }

    default void remove(){
        if(getGroup() != null) {
            getGroup().remove(this);
        }

        setGroup(null);
    }

    EntityGroup getGroup();
    void setGroup(EntityGroup group);

    default boolean isAdded(){
        return getGroup() != null;
    }
}
