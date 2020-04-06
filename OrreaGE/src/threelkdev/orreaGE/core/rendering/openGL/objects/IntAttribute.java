package threelkdev.orreaGE.core.rendering.openGL.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;


/** Credit to ThinMatrix */

public class IntAttribute extends Attribute {
	
	public IntAttribute( int attrNumber, int dataType, int componentCount ) {
		super( attrNumber, dataType, componentCount );
	}
	
	@Override
	protected void link( int offset, int stride ) {
		GL30.glVertexAttribIPointer( attributeNumber, componentCount, GL11.GL_INT, stride, offset );
	}
}
