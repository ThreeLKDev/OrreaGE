package threelkdev.orreaGE.tools.pools;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class Vec3Pool {
	
	private static final ObjectPool< Vector3f > pool = new ObjectPool< Vector3f >( 128 ) {
		@Override
		protected Vector3f createNewObject() {
			return new Vector3f();
		}
	};
	
	public static Vector3f get( float x, float y, float z ) {
		Vector3f vec = pool.get();
		vec.set( x, y, z );
		return vec;
	}
	
	public static Vector3f get( ReadableVector3f duplicate ) {
		Vector3f vec = pool.get();
		vec.set( duplicate );
		return vec;
	}
	
	public static Vector3f get() {
		Vector3f vec = pool.get();
		vec.set( 0, 0, 0 );
		return vec;
	}
	
	public static Vector3f getRandom() { return getRandom( 1f ); }
	public static Vector3f getRandom( float scalar ) {
		Vector3f vec = pool.get();
		vec.set( 
			( float ) Math.random() * scalar, 
			( float ) Math.random() * scalar, 
			( float ) Math.random() * scalar 
		);
		return vec;
	}
	
	public static void release( Vector3f old ) {
		pool.release( old );
	}
	
}
