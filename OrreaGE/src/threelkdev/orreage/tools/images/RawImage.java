package threelkdev.orreage.tools.images;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class RawImage {
	
	private final ByteBuffer image;
	private final int width, height;
	
	private RawImage( ByteBuffer image, int width, int height ) {
		this.image = image;
		this.width = width;
		this.height = height;
	}
	
	public ByteBuffer getImage() {
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public static RawImage load( File file ) throws Exception {
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer comp = stack.mallocInt( 1 );
			IntBuffer width = stack.mallocInt( 1 );
			IntBuffer height = stack.mallocInt( 1 );
			ByteBuffer image = STBImage.stbi_load( file.getPath(), width, height, comp, 4);
			if( image == null ) {
				throw new Exception( "Couldn't load image: " + file.getPath() );
			}
			return new RawImage( image, width.get(), height.get() );
		}
	}
	
}
