package model.effects;

import model.world.Champion;

public abstract class Effect implements Cloneable{
	private String name;
	private EffectType type;
	private int duration;
	private EffectListener listener;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}
public Object clone() throws CloneNotSupportedException
{
	return super.clone();
}
	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}
	
	public void setListener(EffectListener listener) {
		this.listener = listener;
	}
	
	public void apply() {
		// inform interested listener, if any, that the product has been bought
		if (listener != null) {
			listener.onApply(this);
		}
	}
	
	public void rempve() {
		// inform interested listener, if any, that the product has been bought
		if (listener != null) {
			listener.onRemove(this);
		}
	}
	
public abstract void apply(Champion c);

public abstract void remove(Champion c);
	

}
