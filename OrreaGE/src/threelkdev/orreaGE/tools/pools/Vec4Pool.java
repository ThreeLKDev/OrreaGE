package threelkdev.orreaGE.tools.pools;

import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector4f;

public class Vec4Pool {
	
	private static final ObjectPool< Vector4f > pool = new ObjectPool< Vector4f >( 128 ) {
		@Override
		protected Vector4f createNewObject() {
			return new Vector4f();
		}
	};
	
	public static Vector4f get( float x, float y, float z, float w ) {
		Vector4f vec = pool.get();
		vec.set( x, y, z, w );
		return vec;
	}
	
	public static Vector4f get( ReadableVector4f duplicate ) {
		Vector4f vec = pool.get();
		vec.set( duplicate );
		return vec;
	}
	
	public static Vector4f get() {
		Vector4f vec = pool.get();
		vec.set( 0, 0, 0, 0 );
		return vec;
	}
	
	public static void release( Vector4f old ) {
		pool.release( old );
	}

	public static Vector4f getRandom() { return getRandom( 1f ); }
	public static Vector4f getRandom( float scalar ) {
		Vector4f vec = pool.get();
		vec.set( 
			( float ) Math.random() * scalar, 
			( float ) Math.random() * scalar, 
			( float ) Math.random() * scalar, 
			( float ) Math.random() * scalar 
		);
		return vec;
	}
	
}
