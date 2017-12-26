package io.anuke.ucore.scene.actions;

import io.anuke.ucore.function.Callable;
import io.anuke.ucore.scene.Action;

public class CallAction extends Action{
    public Callable call;

    @Override
    public boolean act(float delta) {
        call.run();
        return true;
    }
}
