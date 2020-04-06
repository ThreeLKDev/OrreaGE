package threelkdev.orreaGE.tools.pools;

import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.Vector2f;

public class Vec2Pool {
	
	private static final ObjectPool< Vector2f > pool = new ObjectPool< Vector2f >( 128 ) {
		@Override
		protected Vector2f createNewObject() {
			return new Vector2f();
		}
	};
	
	public static Vector2f get( float x, float y ) {
		Vector2f vec = pool.get();
		vec.set( x, y );
		return vec;
	}
	
	public static Vector2f get( ReadableVector2f duplicate ) {
		Vector2f vec = pool.get();
		vec.set( duplicate );
		return vec;
	}
	
	public static Vector2f get() {
		Vector2f vec = pool.get();
		vec.set( 0, 0 );
		return vec;
	}
	
	public static void release( Vector2f old ) {
		pool.release( old );
	}
	
}
