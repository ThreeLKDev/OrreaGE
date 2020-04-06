package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import threelkdev.orreaGE.tools.pools.Vec3Pool;
import threelkdev.orreaGE.tools.pools.Vec4Pool;
import threelkdev.orreaGE.tools.utils.DataUtils;

public class VertexData {
	
	public static final int BYTES_PER_VERTEX = 20;
	
	private final Vector3f position;
	private final Vector3f normal;
	private final byte flex;
	
	public VertexData( Vector3f position, Vector3f normal, byte flex ) {
		this.position = position;
		this.normal = normal;
		this.flex = flex;
	}
	
	protected void storeInstanceData( ByteBuffer buffer, Matrix4f transform, byte[] material ) {
		storeInstancedPosition( buffer, transform );
		storeInstancedNormal( buffer, transform );
		buffer.put( material );
		buffer.put( flex );
	}
	
	protected void storeRawData( ByteBuffer buffer, byte[] colour, float customIndicator ) {
		storePosition( buffer, position );
		storeNormal( buffer, normal, customIndicator );
		buffer.put( colour );
		buffer.put( flex );
	}
	
	private void storePosition( ByteBuffer buffer, ReadableVector3f pos ) {
		buffer.putFloat( pos.getX() );
		buffer.putFloat( pos.getY() );
		buffer.putFloat( pos.getZ() );
	}
	
	private void storeNormal( ByteBuffer buffer, ReadableVector3f norm, float customIndicator ) {
		int packedInt = DataUtils.pack_2_10_10_10_REV_int( norm.getX(), norm.getY(), norm.getZ(), customIndicator );
		buffer.putInt( packedInt );
	}
	
	private void storeInstancedPosition( ByteBuffer buffer, Matrix4f transform ) {
		Vector4f transformedPos = transformVector( transform, position, 1f );
		storePosition( buffer, transformedPos );
		Vec4Pool.release( transformedPos );
	}
	
	private void storeInstancedNormal( ByteBuffer buffer, Matrix4f transform ) {
		Vector4f transformedNormal = transformVector( transform, normal, 0 );
		Vector3f unitNormal = Vec3Pool.get( transformedNormal );
		unitNormal.normalise();
		storeNormal(buffer, unitNormal, 0);
		Vec3Pool.release( unitNormal );
		Vec4Pool.release( transformedNormal );
	}
	
	private Vector4f transformVector( Matrix4f transform, Vector3f vector, float w ) {
		Vector4f pos4f = Vec4Pool.get();
		pos4f.set( vector.x, vector.y, vector.z, w );
		return Matrix4f.transform( transform, pos4f, pos4f );
	}
	
}
