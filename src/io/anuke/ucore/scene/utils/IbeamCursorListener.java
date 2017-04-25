package io.anuke.ucore.scene.utils;

import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.event.InputEvent;

public class IbeamCursorListener extends ClickListener{
	@Override
	public void enter (InputEvent event, float x, float y, int pointer, Element fromActor) {
		super.enter(event, x, y, pointer, fromActor);
		if (pointer == -1 && event.getTarget().isVisible()) {
			CursorManager.setIbeam();
		}
	}

	@Override
	public void exit (InputEvent event, float x, float y, int pointer, Element toActor) {
		super.exit(event, x, y, pointer, toActor);
		if (pointer == -1) {
			CursorManager.restoreCursor();
		}
	}
}
