package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.util.vector.Matrix4f;

public class SectionData {
	
	private final boolean customColour;
	private final byte[] colour;
	private final VertexData[] vertices;
	private final int[] indices;
	
	public SectionData( VertexData[] vertices, byte[] colour ) {
		this( vertices, colour, null );
	}
	
	public SectionData( VertexData[] vertices, byte[] colour, int[] indices ) {
		this.customColour = ( colour == null );
		this.colour = colour;
		this.vertices = vertices;
		this.indices = indices;
	}
	
	protected int getByteSize() {
		return vertices.length * VertexData.BYTES_PER_VERTEX;
	}
	
	protected void storeInstanceData( ByteBuffer buffer, Matrix4f transform, byte[] material ) {
		for( VertexData vertex : vertices ) {
			byte[] sectionColour = customColour ? material : colour;
			vertex.storeInstanceData( buffer, transform, sectionColour );
		}
	}
	
	protected void storeInstanceData( ByteBuffer vertexBuffer, IntBuffer indexBuffer, Matrix4f transform, byte[] material ) {
		storeInstanceData( vertexBuffer, transform, material );
		if( indices != null ) {
			indexBuffer.put( indices );
		}
	}
	
	protected void storeRawData( ByteBuffer buffer ) {
		float customIndicator = customColour ? 1 : 0;
		for( VertexData vertex : vertices )
			vertex.storeRawData( buffer, colour, customIndicator );
	}
	
	protected boolean hasIndices() { return indices != null; }
	
}
