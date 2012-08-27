package tetz42.cellom.body;

import tetz42.cellom.Context;


public class CellForMap<T> extends Cell<T> {

	private final String key;
	private final CelloMap<T> cumap;

	CellForMap(CelloMap<T> cumap, String key, Context<?> context) {
		super(cumap, cumap.getCellDef(), context);
		this.cumap = cumap;
		this.key = key;
	}

	@Override
	public T get() {
		return cumap.get(key);
	}

	@Override
	public void set(T value) {
		cumap.set(key, value);
	}

}
