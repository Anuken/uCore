package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;

public class EffectEntity extends TimedEntity implements Poolable{
	public Effect effect;
	public Color color = Color.WHITE;
	public float rotation = 0f;

	/**For pooling use only!*/
	public EffectEntity(){
	}

	@Override
	public void reset() {
		effect = null;
		color = Color.WHITE;
		rotation = 0f;
		time = 0f;
	}

	@Override
	public void draw(){
		Effects.renderEffect(id, effect, color, time, rotation, x, y);
	}

	@Override
	public void removed() {
		Pools.free(this);
	}
}
