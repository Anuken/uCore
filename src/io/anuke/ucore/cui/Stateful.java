package io.anuke.ucore.cui;

/**Indicates that this section has a state.*/
public interface Stateful{
	public Enum<?>[] stateValues();
	public Enum<?> defaultState();
}
