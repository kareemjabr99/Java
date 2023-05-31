package model.effects;

public interface EffectListener {
	void onApply(Effect effect);
	void onRemove(Effect effect);
}
