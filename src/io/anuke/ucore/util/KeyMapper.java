package io.anuke.ucore.util;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

import static com.badlogic.gdx.controllers.mappings.Xbox.*;

public class KeyMapper {
    private static IntMap<String> names = new IntMap<>();

    static{
        names.put(-1, "Unknown");

        names.put(A, "A");
        names.put(B, "B");
        names.put(X, "X");
        names.put(Y, "Y");
        names.put(GUIDE, "Guide");
        names.put(L_BUMPER, "L Bumper");
        names.put(R_BUMPER, "R Bumper");
        names.put(L_TRIGGER, "L Trigger");
        names.put(R_TRIGGER, "R Trigger");
        names.put(BACK, "Back");
        names.put(START, "Start");
        names.put(DPAD_UP, "D-pad Up");
        names.put(DPAD_DOWN, "D-pad Down");
        names.put(DPAD_LEFT, "D-pad Left");
        names.put(DPAD_RIGHT, "D-pad Right");
    }

    public static String getControllerKey(int code){
        return names.get(code, "Unknown");
    }

}
