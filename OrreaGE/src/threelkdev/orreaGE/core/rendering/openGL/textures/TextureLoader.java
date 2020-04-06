package threelkdev.orreaGE.core.rendering.openGL.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {
	
	private final Texture texture;
	private final File textureFile;
	private final TextureBuilder builder;
	
	private boolean decoded = false;
	private int width, height;
	private ByteBuffer buffer;
	
	protected TextureLoader( File textureFile, Texture texture, TextureBuilder builder ) {
		this.textureFile = textureFile;
		this.texture = texture;
		this.builder = builder;
	}
	
	/**
	 * @ return the texture object, loaded or otherwise.
	 */
	public Texture getTexture() {
		return texture;
	}
	
	/**
	 * CPU only part of the texture loading process. No OpenGL calls required here.
	 * @return true if the decoding was successful.
	 */
	public boolean preDecodeTextureFile() {
		if( decoded )
			return true;
		try {
			decodeTextureData();
		} catch ( Exception e ) {
			System.err.println( "Failed to decode texture: " + textureFile.getPath() );
			e.printStackTrace();
			return false;
		}
		this.decoded = true;
		return true;
	}
	
	public Texture loadTexture() {
		preDecodeTextureFile();
		if( !decoded ) {
			return texture;
		}
		int id = initTexture();
		setTextureParams();
		texture.setTextureID( id );
		return texture;
	}
	
	public boolean hasBeenDecoded() { return decoded; }
	
	private void decodeTextureData() throws Exception {
		InputStream inputStream = new FileInputStream( textureFile );
		PNGDecoder decoder = new PNGDecoder( inputStream );
		this.width = decoder.getWidth();
		this.height = decoder.getHeight();
		//TODO use new alloc methods?
		this.buffer = ByteBuffer.allocateDirect( 4 * width * height );
		decoder.decode( buffer,  width * 4, Format.BGRA );
		buffer.flip();
		inputStream.close();
	}
	
	private int initTexture() {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture( GL13.GL_TEXTURE0 );
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, texID );
		GL11.glPixelStorei( GL11.GL_UNPACK_ALIGNMENT, 1 );
		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer );
		return texID;
	}
	
	private void setTextureParams() {
		if( builder.isMipMap() )
			setMipmapParams( builder.isAnisotropic() );
		else if( builder.isNearest() )
			setFilters( GL11.GL_NEAREST, GL11.GL_NEAREST );
		else
			setFilters( GL11.GL_LINEAR, GL11.GL_LINEAR );
		
		setEdgeParams();
	}
	
	private void setMipmapParams( boolean anisotropic ) {
		GL30.glGenerateMipmap( GL11.GL_TEXTURE_2D );
		setFilters( GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR );
		if( anisotropic ) {
			// TODO check supported?
			GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0 );
			GL11.glTexParameterf( GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 4.0f);
		}
	}
	
	private void setFilters( int minFilterType, int magFilterType ) {
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilterType );
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilterType );
	}
	
	private void setEdgeParams() {
		if( builder.isClampEdges() ) {
			setWrapType( GL12.GL_CLAMP_TO_EDGE );
		} else if( builder.isClampToBorder() ) {
			setWrapType( GL13.GL_CLAMP_TO_BORDER );
			GL11.glTexParameterfv( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, builder.getBorderColour().getAsFloatBuffer() );
		} else {
			setWrapType( GL11.GL_REPEAT );
		}
	}
	
	private void setWrapType( int wrapType ) {
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapType );
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapType );
	}
	
}
