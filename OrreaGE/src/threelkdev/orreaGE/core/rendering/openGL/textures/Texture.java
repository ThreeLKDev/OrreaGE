package threelkdev.orreaGE.core.rendering.openGL.textures;

import java.io.File;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
	
	private int textureID;
	private boolean loaded;
	
	protected Texture( int id ) {
		this.textureID = id;
		this.loaded = true;
	}
	
	protected Texture() {
		this.loaded = false;
	}
	
	public void setTextureID( int id ) {
		this.textureID = id;
		loaded = true;
	}
	
	public void bindToBank( int bankNumber ) {
		GL13.glActiveTexture( GL13.GL_TEXTURE0 + bankNumber );
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, textureID );
//		GL11.glEnable( GL11.GL_TEXTURE_2D );
	}
	
	public void unbindFromBank( int bankNumber ) {
		GL13.glActiveTexture( GL13.GL_TEXTURE0 + bankNumber );
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, 0 );
//		GL11.glDisable( GL11.GL_TEXTURE_2D );
	}
	
	public boolean isLoaded() { return loaded; }
	public int getID() { return textureID; }
	
	public void delete() {
		loaded = false;
		GL11.glDeleteTextures( textureID );
	}
	
	public static Texture newEmptyTexture() {
		return new Texture();
	}
	
	public static TextureBuilder newTexture( File file ) {
		return new TextureBuilder( file );
	}
}
