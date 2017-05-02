/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package io.anuke.ucore.scene.ui;
import static io.anuke.ucore.scene.actions.Actions.*;
import static io.anuke.ucore.scene.style.Styles.styles;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.modules.ModuleController;
import io.anuke.ucore.scene.Action;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.actions.Actions;
import io.anuke.ucore.scene.event.InputEvent;
import io.anuke.ucore.scene.event.InputListener;
import io.anuke.ucore.scene.style.Styles;
import io.anuke.ucore.scene.ui.ImageButton.ImageButtonStyle;
import io.anuke.ucore.scene.ui.Label.LabelStyle;
import io.anuke.ucore.scene.ui.TextButton.TextButtonStyle;
import io.anuke.ucore.scene.ui.layout.Cell;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.scene.utils.ChangeListener;
import io.anuke.ucore.scene.utils.FocusListener;
/** Displays a dialog, which is a modal window containing a content table with a button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any widgets can be added. When a button is clicked,
 * {@link #result(Object)} is called and the dialog is removed from the stage.
 * @author Nathan Sweet */ 
public class Dialog extends Window {
	public static float closePadT, closePadR;
	
	Table contentTable, buttonTable;
	ObjectMap<Element, Object> values = new ObjectMap();
	boolean cancelHide;
	Element previousKeyboardFocus, previousScrollFocus;
	FocusListener focusListener;
	Runnable hideListener, showListener;

	protected InputListener ignoreTouchDown = new InputListener() {
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			event.cancel();
			return false;
		}
	};

	public Dialog (String title) {
		super(title, styles.get(WindowStyle.class));
		initialize();
	}

	public Dialog (String title, String windowStyleName) {
		super(title, styles.get(windowStyleName, WindowStyle.class));
		initialize();
	}

	public Dialog (String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		initialize();
	}

	private void initialize () {
		setModal(true);
		setMovable(false);

		defaults().space(6);
		add(contentTable = new Table()).expand().fill();
		row();
		add(buttonTable = new Table()).fillX();

		contentTable.defaults().space(6);
		buttonTable.defaults().space(6);

		buttonTable.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Element actor) {
				if (!values.containsKey(actor)) return;
				while (actor.getParent() != buttonTable)
					actor = actor.getParent();
				result(values.get(actor));
				if (!cancelHide) hide();
				cancelHide = false;
			}
		});

		focusListener = new FocusListener() {
			public void keyboardFocusChanged (FocusEvent event, Element actor, boolean focused) {
				if (!focused) focusChanged(event);
			}

			public void scrollFocusChanged (FocusEvent event, Element actor, boolean focused) {
				if (!focused) focusChanged(event);
			}

			private void focusChanged (FocusEvent event) {
				Scene stage = getScene();
				if (isModal && stage != null && stage.getRoot().getChildren().size > 0
					&& stage.getRoot().getChildren().peek() == Dialog.this) { // Dialog is top most actor.
					Element newFocusedActor = event.getRelatedActor();
					if (newFocusedActor != null && !newFocusedActor.isDescendantOf(Dialog.this) &&
						!(newFocusedActor.equals(previousKeyboardFocus) || newFocusedActor.equals(previousScrollFocus)) )
						event.cancel();
				}
			}
		};
	}

	protected void setScene (Scene stage) {
		if (stage == null)
			addListener(focusListener);
		else
			removeListener(focusListener);
		super.setScene(stage);
	}
	
	public void addCloseButton () {
		Label titleLabel = getTitleLabel();
		Table titleTable = getTitleTable();

		ImageButton closeButton = new ImageButton(Styles.styles.get("close-window", ImageButtonStyle.class));
		
		titleTable.add(closeButton).padRight(-getPadRight() + 0.7f+closePadR).size(40).padTop(-titleTable.getPadTop()-12+closePadT);
		
		closeButton.changed(()->{
			hide();
		});

		if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
			titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
	}
	
	public Table content(){
		return contentTable;
	}
	
	public Label title(){
		return titleLabel;
	}

	public Table getContentTable () {
		return contentTable;
	}

	public Table getButtonTable () {
		return buttonTable;
	}

	/** Adds a label to the content table. The dialog must have been constructed with a skin to use this method. */
	public Cell<Label> text (String text) {
		return text(text, styles.get(LabelStyle.class));
	}

	/** Adds a label to the content table. */
	public Cell<Label> text (String text, LabelStyle labelStyle) {
		return text(new Label(text, labelStyle));
	}

	/** Adds the given Label to the content table */
	public Cell<Label> text (Label label) {
		return contentTable.add(label);
	}

	/** Adds a text button to the button table. Null will be passed to {@link #result(Object)} if this button is clicked. The dialog
	 * must have been constructed with a skin to use this method. */
	public Cell<Button> button (String text) {
		return button(text, null);
	}

	/** Adds a text button to the button table. The dialog must have been constructed with a skin to use this method.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Cell<Button> button (String text, Object object) {
		return button(text, object, styles.get(TextButtonStyle.class));
	}

	/** Adds a text button to the button table.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Cell<Button> button (String text, Object object, TextButtonStyle buttonStyle) {
		return button(new TextButton(text, buttonStyle), object);
	}

	/** Adds the given button to the button table. */
	public Cell<Button> button (Button button) {
		return button(button, null);
	}

	/** Adds the given button to the button table.
	 * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
	public Cell<Button> button (Button button, Object object) {
		Cell<Button> c = buttonTable.add(button);
		setObject(button, object);
		return c;
	}
	
	/**Adds a show() listener.*/
	public void shown(Runnable run){
		this.showListener = run;
	}
	
	/**Adds a hide() listener.*/
	public void hidden(Runnable run){
		this.hideListener = run;
	}
	
	/**Sets style to 'dialog'.*/
	public Dialog setDialog(){
		setStyle(Styles.styles.get("dialog", WindowStyle.class));
		return this;
	}

	/** {@link #pack() Packs} the dialog and adds it to the stage with custom action which can be null for instant show */
	public Dialog show (Scene stage, Action action) {
		if(showListener != null)
			showListener.run();
		clearActions();
		removeCaptureListener(ignoreTouchDown);

		previousKeyboardFocus = null;
		Element actor = stage.getKeyboardFocus();
		if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

		previousScrollFocus = null;
		actor = stage.getScrollFocus();
		if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;

		pack();
		stage.add(this);
		stage.setKeyboardFocus(this);
		stage.setScrollFocus(this);
		if (action != null) addAction(action);

		return this;
	}
	
	/**Shows using the ModuleController's UI.*/
	public Dialog show () {
		return show(ModuleController.ui().scene);
	}

	/** {@link #pack() Packs} the dialog and adds it to the stage, centered with default fadeIn action */
	public Dialog show (Scene stage) {
		show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		return this;
	}

	/** Hides the dialog with the given action and then removes it from the stage. */
	public void hide (Action action) {
		if(hideListener != null)
			hideListener.run();
		
		Scene stage = getScene();
		if (stage != null) {
			removeListener(focusListener);
			if (previousKeyboardFocus != null && previousKeyboardFocus.getScene() == null) previousKeyboardFocus = null;
			Element actor = stage.getKeyboardFocus();
			if (actor == null || actor.isDescendantOf(this)) stage.setKeyboardFocus(previousKeyboardFocus);

			if (previousScrollFocus != null && previousScrollFocus.getScene() == null) previousScrollFocus = null;
			actor = stage.getScrollFocus();
			if (actor == null || actor.isDescendantOf(this)) stage.setScrollFocus(previousScrollFocus);
		}
		if (action != null) {
			addCaptureListener(ignoreTouchDown);
			addAction(sequence(action, Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
		} else
			remove();
	}

	/** Hides the dialog. Called automatically when a button is clicked. The default implementation fades out the dialog over 400
	 * milliseconds. */
	public void hide () {
		hide(fadeOut(0.4f, Interpolation.fade));
	}

	public void setObject (Element actor, Object object) {
		values.put(actor, object);
	}

	/** If this key is pressed, {@link #result(Object)} is called with the specified object.
	 * @see Keys */
	public Dialog key (final int keycode, final Object object) {
		addListener(new InputListener() {
			public boolean keyDown (InputEvent event, int keycode2) {
				if (keycode == keycode2) {
					result(object);
					if (!cancelHide) hide();
					cancelHide = false;
				}
				return false;
			}
		});
		return this;
	}

	/** Called when a button is clicked. The dialog will be hidden after this method returns unless {@link #cancel()} is called.
	 * @param object The object specified when the button was added. */
	protected void result (Object object) {
	}

	public void cancel () {
		cancelHide = true;
	}
}
