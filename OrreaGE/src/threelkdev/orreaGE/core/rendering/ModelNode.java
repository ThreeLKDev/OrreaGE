package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ModelNode {
	
	private final ModelVertex[] vertices;
	private final int[] indices;
	private final String name;
	
	public ModelNode( String name, ModelVertex[] vertices ) { this( name, vertices, null ); }
	public ModelNode( String name, ModelVertex[] vertices, int[] indices ) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
	}
	
	protected String getName() { return name; }
	
	protected int getByteSize() {
		return vertices.length * ModelVertex.BYTES;
	}
	
	protected int getNumVertices() { return vertices.length; }
	protected int getNumIndices() { return indices != null ? indices.length : 0; }
	
	protected void storeInstanceData( ByteBuffer buffer, Matrix4f transform, Vector4f pass4f, Vector3f pass3f ) {
		for( ModelVertex vert : vertices ) {
			vert.storeInstanceData( buffer, transform, pass4f, pass3f );
		}
	}
	
	protected void storeInstanceData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, Material material, Matrix4f transform, Vector4f pass4f, Vector3f pass3f ) {
		storeInstanceData( vertexBuffer, indexBuffer, 0, GL11.GL_UNSIGNED_INT , transform, pass4f, pass3f );
	}
	
	protected void storeInstanceData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, int indexOffset, int indexDataType, Matrix4f transform, Vector4f pass4f, Vector3f pass3f ) {
		storeInstanceData( vertexBuffer, transform, pass4f, pass3f );
		if( indexBuffer == null ) return;
		if( indices != null ) {
			int indexToStore;
			for( int i = 0; i < indices.length; i++ ) {
				indexToStore = indices[ i ] + indexOffset;
				switch( indexDataType ) {
					case GL11.GL_UNSIGNED_BYTE:
						indexBuffer.put( ( byte ) indexToStore );
						break;
					case GL11.GL_UNSIGNED_SHORT:
						indexBuffer.putShort( ( short ) indexToStore );
						break;
					case GL11.GL_UNSIGNED_INT:
						indexBuffer.putInt( indexToStore );
						break;
				}
			}
		}
	}
	
	protected void storeRawData( ByteBuffer buffer ) {
		for( ModelVertex vert : vertices ) {
			vert.storeRawData( buffer );
		}
	}
	
	protected void storeRawData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer ) { storeRawData( vertexBuffer, indexBuffer, 0, GL11.GL_UNSIGNED_INT ); }
	protected void storeRawData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, int indexOffset, int indexDataType ) {
		storeRawData( vertexBuffer );
		int indexToStore;
		if( indices != null ) {
			for( int i = 0; i < indices.length; i++ ) {
				indexToStore = indices[ i ] + indexOffset;
				switch( indexDataType ) {
					case GL11.GL_UNSIGNED_BYTE:
						indexBuffer.put( ( byte ) indexToStore );
						break;
					case GL11.GL_UNSIGNED_SHORT:
						indexBuffer.putShort( ( short ) indexToStore );
						break;
					case GL11.GL_UNSIGNED_INT:
						indexBuffer.putInt( indexToStore );
						break;
				}
			}
		}
	}
	
}
