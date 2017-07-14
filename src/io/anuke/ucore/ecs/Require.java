package io.anuke.ucore.ecs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**Used to mark trait requirements for a specfic trait.*/
@Retention(RetentionPolicy.RUNTIME)
public @interface Require{
	public Class<? extends Trait>[] value();
}
