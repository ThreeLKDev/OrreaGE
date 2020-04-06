package threelkdev.orreaGE.tools.utils;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;

public class VaoUtils {
	
	public static Vao createVao( float[] interleavedData, Attribute... attributes ) {
		Vao vao = Vao.create();
		vao.bind();
		try( MemoryStack stack = MemoryStack.stackPush() ) {
			FloatBuffer buffer = stack.mallocFloat( interleavedData.length );
			buffer.put( interleavedData );
			buffer.flip();
			vao.initDataFeed( buffer,  GL15.GL_STATIC_DRAW, attributes );
		}
		vao.unbind();
		return vao;
	}
}
