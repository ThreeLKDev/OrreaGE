package threelkdev.orreaGE.tools.pools;

import java.util.Stack;

/** Credit to ThinMatrix */

public abstract class ObjectPool< T > {
	
	private final int maxSize;
	private final Stack< T > pool = new Stack< T >();
	
	public ObjectPool( int maxSize ) {
		this.maxSize = maxSize;
	}
	
	public T get() {
		if( pool.isEmpty() ) {
			return createNewObject();
		}
		return pool.pop();
	}
	
	public void release( T unusedObject ) {
		if( pool.size() >= maxSize ) {
			return;
		}
		pool.push( unusedObject );
	}
	
	protected abstract T createNewObject();
	
}
