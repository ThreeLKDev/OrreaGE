package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ModelVertex {
	
	// Position:	3x 4-byte Floats			= 12 bytes
	// Normal:		3x 4-byte Floats			= 12 bytes
	// TexCoords:	2x 4-byte Floats			=  8 bytes
	public static final int BYTES = 32;
	
	private final Vector3f position;
	private final Vector3f normal;
	private final Vector2f texCoords;
	
	public ModelVertex( Vector3f position ) { this( position, position, new Vector2f( 0, 0 ) ); }
	public ModelVertex( Vector3f position, Vector3f normal, Vector2f texCoords ) {
		this.position = position;
		this.normal = normal;
		this.texCoords = texCoords;
	}
	
	protected void storeInstanceData( ByteBuffer buffer, Matrix4f transform, Vector4f dest4f, Vector3f dest3f ) {
		storeTransformedPosition( buffer, transform, dest4f );
		storeTransformedNormal( buffer, transform, dest4f, dest3f );
		storeTexCoords( buffer, texCoords );
	}
	
	protected void storeRawData( ByteBuffer buffer ) {
		storePosition( buffer, position );
		storeNormal( buffer, normal );
		storeTexCoords( buffer, texCoords );
	}
	
	private void storePosition( ByteBuffer buffer, ReadableVector3f pos ) {
		buffer.putFloat( pos.getX() );
		buffer.putFloat( pos.getY() );
		buffer.putFloat( pos.getZ() );
	}
	
	private void storeNormal( ByteBuffer buffer, ReadableVector3f norm ) {
		buffer.putFloat( norm.getX() );
		buffer.putFloat( norm.getY() );
		buffer.putFloat( norm.getZ() );
	}
	
	private void storeTexCoords( ByteBuffer buffer, ReadableVector2f tex ) {
		buffer.putFloat( tex.getX() );
		buffer.putFloat( tex.getY() );
	}
	
	private void storeTransformedPosition( ByteBuffer buffer, Matrix4f transform, Vector4f dest ) {
		transformVector( transform, position, 1f, dest );
		storePosition( buffer, dest );
	}
	
	private void storeTransformedNormal( ByteBuffer buffer, Matrix4f transform, Vector4f dest4f, Vector3f dest3f ) {
		dest4f = transformVector( transform, normal, 0, dest4f );
		dest3f.set( dest4f );
		dest3f.normalise();
		storeNormal( buffer, dest3f );
	}
	
	private Vector4f transformVector( Matrix4f transform, Vector3f vector, float w, Vector4f dest ) {
		dest.set( vector.x, vector.y, vector.z, w );
		return Matrix4f.transform( transform, dest, dest );
	}
	
}
