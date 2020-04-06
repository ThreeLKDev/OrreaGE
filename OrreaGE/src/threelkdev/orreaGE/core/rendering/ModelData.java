package threelkdev.orreaGE.core.rendering;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ModelData {
	
	public static final int NO_INDEX_BUFFER = 0;
	
	private final int vertexCount;
	private final SectionData[] sections;
	private final int indexCount;
	
	public ModelData( int vertexCount, SectionData... sections ) {
		this.vertexCount = vertexCount;
		this.sections = sections;
		this.indexCount = NO_INDEX_BUFFER;
	}
	
	public ModelData( int vertexCount, int indexCount, SectionData... sections ) {
		this.vertexCount = vertexCount;
		this.sections = sections;
		this.indexCount = indexCount > NO_INDEX_BUFFER ? indexCount : NO_INDEX_BUFFER;;
	}
	
	public int getVertexCount() { return vertexCount; }
	
	@Deprecated
	public byte[] getInstanceData( Matrix4f transform, byte[] material ) {
		ByteBuffer buffer = ByteBuffer.allocate( getByteSize() ).order( ByteOrder.nativeOrder() );
		for( SectionData section : sections ) {
			section.storeInstanceData( buffer, transform, material );
		}
		return buffer.array();
	}
	
	public byte[] getRawData() {
		ByteBuffer buffer = ByteBuffer.allocate( getByteSize() ).order( ByteOrder.nativeOrder() );
		storeRawData( buffer );
		return buffer.array();
	}
	
	public void storeRawData( ByteBuffer buffer ) {
		for( SectionData section : sections ) {
			section.storeRawData( buffer );
		}
	}
	
	private Buffer genIndexBuffer( int indexCount ) {
		Buffer indexBuffer;
		
		if( indexCount <= Byte.MAX_VALUE )
			indexBuffer = BufferUtils.createByteBuffer( indexCount );
		else if( indexCount <= Short.MAX_VALUE )
			indexBuffer = BufferUtils.createShortBuffer( indexCount );
		else if( indexCount <= Integer.MAX_VALUE )
			indexBuffer = BufferUtils.createIntBuffer( indexCount );
		else {
			//ERR
			indexBuffer = BufferUtils.createLongBuffer( indexCount );
		}
		
		return indexBuffer;
	}
	
	public int getByteSize() {
		int total = 0;
		for( SectionData section : sections )
			total += section.getByteSize();
		return total;
	}
	
}
