package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

/**
 * Wrapper class for OpenGL Vertex Attributes.
 * Taken and modified from ThinMatrix's code.
 * @author Luca Kieran
 */
public class Attribute {
	
	protected final int attributeNumber;
	protected final int dataType;
	protected final boolean normalized;
	protected final int componentCount;
	protected final int bytesPerVertex;
	
	public Attribute( int attrNumber, int dataType, int componentCount ) {
		this( attrNumber, dataType, componentCount, false );
	}
	
	public Attribute( int attrNumber, int dataType, int componentCount, boolean normalized ) {
		this.attributeNumber = attrNumber;
		this.dataType = dataType;
		this.componentCount = componentCount;
		this.normalized = normalized;
		this.bytesPerVertex = calcBytesPerVertex();
	}
	protected void enable( boolean enable ) {
		if ( enable ) {
			GL20.glEnableVertexAttribArray( attributeNumber );
		} else {
			GL20.glDisableVertexAttribArray( attributeNumber );
		}
	}
	
	protected void link( int offset, int stride ) {
		GL20.glVertexAttribPointer( attributeNumber, componentCount, dataType, normalized, stride, offset );
	}
	
	private int calcBytesPerVertex() {
		switch ( dataType ) {
			case GL11.GL_FLOAT:
			case GL11.GL_UNSIGNED_INT:
			case GL11.GL_INT:
				return 4 * componentCount;
			case GL11.GL_SHORT:
			case GL11.GL_UNSIGNED_SHORT:
				return 2 * componentCount;
			case GL11.GL_BYTE:
			case GL11.GL_UNSIGNED_BYTE:
				return componentCount;
			case GL12.GL_UNSIGNED_INT_2_10_10_10_REV:
				return 4;
			default:
				System.err.println( "Unsupported data type for VAO attribute: " + dataType );
				return 0;
				
		}
	}
}
