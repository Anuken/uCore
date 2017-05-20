package io.anuke.ucore.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.ObjectMap;

/**XBox controller keys.*/
public class Xkeys{
	private static ObjectMap<Integer, String> names = new ObjectMap<>();
	static public String os = System.getProperty("os.name");
	static public boolean isWindows = os.contains("Windows");
	static public boolean isLinux = os.contains("Linux");
	static public boolean isMac = os.contains("Mac");
	
	// Buttons
	public static final int A;
	public static final int B;
	public static final int X;
	public static final int Y;
	public static final int GUIDE;
	public static final int L_BUMPER;
	public static final int R_BUMPER;
	public static final int BACK;
	public static final int START;
	public static final int DPAD_UP;
	public static final int DPAD_DOWN;
	public static final int DPAD_LEFT;
	public static final int DPAD_RIGHT;
	public static final int L_STICK;
	public static final int R_STICK;

	// Axes
	/** left trigger, -1 if not pressed, 1 if pressed **/
	public static final int L_TRIGGER;
	/** right trigger, -1 if not pressed, 1 if pressed **/
	public static final int R_TRIGGER;
	/** left stick vertical axis, -1 if up, 1 if down **/
	public static final int L_STICK_VERTICAL_AXIS;
	/** left stick horizontal axis, -1 if left, 1 if right **/
	public static final int L_STICK_HORIZONTAL_AXIS;
	/** right stick vertical axis, -1 if up, 1 if down **/
	public static final int R_STICK_VERTICAL_AXIS;
	/** right stick horizontal axis, -1 if left, 1 if right **/
	public static final int R_STICK_HORIZONTAL_AXIS;

	static {
		if (isWindows) {
			A = -1;
			B = -1;
			X = -1;
			Y = -1;
			GUIDE = -1;
			L_BUMPER = -1;
			R_BUMPER = -1;
			BACK = -1;
			START = -1;
			DPAD_UP = -1;
			DPAD_DOWN = -1;
			DPAD_LEFT = -1;
			DPAD_RIGHT = -1;
			L_TRIGGER = -1;
			R_TRIGGER = -1;
			L_STICK_VERTICAL_AXIS = -1;
			L_STICK_HORIZONTAL_AXIS = -1;
			L_STICK = -1;
			R_STICK_VERTICAL_AXIS = -1;
			R_STICK_HORIZONTAL_AXIS = -1;
			R_STICK = -1;
		} else if (isLinux) {
			A = 0;
			B = 1;
			X = 2;
			Y = 3;
			GUIDE = 8;
			L_BUMPER = 4;
			R_BUMPER = 5;
			BACK = 6;
			START = 7;
			DPAD_UP = -1;
			DPAD_DOWN = -1;
			DPAD_LEFT = -1;
			DPAD_RIGHT = -1;
			L_TRIGGER = 2;
			R_TRIGGER = 5;
			L_STICK_VERTICAL_AXIS = 1;
			L_STICK_HORIZONTAL_AXIS = 0;
			L_STICK = 9;
			R_STICK_VERTICAL_AXIS = 4;
			R_STICK_HORIZONTAL_AXIS = 3;
			R_STICK = 10;
		} else if (isMac) {
			A = 11;
			B = 12;
			X = 13;
			Y = 14;
			GUIDE = 10;
			L_BUMPER = 8;
			R_BUMPER = 9;
			BACK = 5;
			START = 4;
			DPAD_UP = 0;
			DPAD_DOWN = 1;
			DPAD_LEFT = 2;
			DPAD_RIGHT = 3;
			L_TRIGGER = 0;
			R_TRIGGER = 1;
			L_STICK_VERTICAL_AXIS = 3;
			L_STICK_HORIZONTAL_AXIS = 2;
			L_STICK = -1;
			R_STICK_VERTICAL_AXIS = 5;
			R_STICK_HORIZONTAL_AXIS = 4;
			R_STICK = -1;
		} else {
			A = -1;
			B = -1;
			X = -1;
			Y = -1;
			GUIDE = -1;
			L_BUMPER = -1;
			R_BUMPER = -1;
			L_TRIGGER = -1;
			R_TRIGGER = -1;
			BACK = -1;
			START = -1;
			DPAD_UP = -1;
			DPAD_DOWN = -1;
			DPAD_LEFT = -1;
			DPAD_RIGHT = -1;
			L_STICK_VERTICAL_AXIS = -1;
			L_STICK_HORIZONTAL_AXIS = -1;
			L_STICK = -1;
			R_STICK_VERTICAL_AXIS = -1;
			R_STICK_HORIZONTAL_AXIS = -1;
			R_STICK = -1;
		}
		
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
	
	public static String toString(int button){
		return names.get(button, "Unknown");
	}
	
	/** @return whether the {@link Controller} is an Xbox controller
	 */
	public static boolean isXboxController(Controller controller) {
		return controller.getName().contains("Xbox");
	}
}
