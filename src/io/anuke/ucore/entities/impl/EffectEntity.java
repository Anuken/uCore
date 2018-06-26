package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;
import io.anuke.ucore.entities.trait.DrawTrait;

public class EffectEntity extends TimedEntity implements Poolable, DrawTrait {
	public Effect effect;
	public Color color = Color.WHITE;
	public Object data;
	public float rotation = 0f;

	public BaseEntity parent;
	public float poffsetx, poffsety;

	/**For pooling use only!*/
	public EffectEntity(){
	}

	public void setParent(BaseEntity parent){
		this.parent = parent;
		this.poffsetx = x - parent.x;
		this.poffsety = y - parent.y;
	}

	@Override
	public float lifetime() {
		return effect.lifetime;
	}

	@Override
	public float drawSize() {
		return effect.size;
	}

	@Override
	public void update() {
		if(effect == null){
			remove();
			return;
		}

		super.update();
		if(parent != null){
			x = parent.x + poffsetx;
			y = parent.y + poffsety;
		}
	}

	@Override
	public void reset() {
		effect = null;
		color = Color.WHITE;
		rotation = time = poffsetx = poffsety = 0f;
		parent = null;
		data = null;
	}

	@Override
	public void draw(){
		Effects.renderEffect(id, effect, color, time, rotation, x, y, data);
	}

	@Override
	public void removed() {
		Pools.free(this);
	}
}
