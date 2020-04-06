package threelkdev.orreage.core.loading;

import java.io.File;

import org.lwjgl.glfw.GLFWImage;

import threelkdev.orreage.tools.images.RawImage;

public class IconLoader {
	
	public static GLFWImage.Buffer loadIcon( File... files ) {
		GLFWImage.Buffer imageBuffer = GLFWImage.malloc( files.length );
		for( int i = 0; i < files.length; i++ ) {
			GLFWImage image = null;
			try {
				image = loadGLFWImage( files[ i ] );
			} catch( Exception e ) {
				e.printStackTrace();
				if( image != null )
					image.close();
				return null;
			}
			imageBuffer.put( i, image );
			image.close();
		}
		return imageBuffer;
	}
	
	private static GLFWImage loadGLFWImage( File file ) throws Exception {
		RawImage rawImage = RawImage.load( file );
		GLFWImage image = GLFWImage.malloc();
		image.set( rawImage.getWidth(),rawImage.getHeight(), rawImage.getImage() );
		return image;
	}
	
}
