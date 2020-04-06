package threelkdev.orreaGE.core.rendering.openGL.textures;

import java.io.File;

import threelkdev.orreaGE.core.loading.BackgroundLoader;
import threelkdev.orreaGE.tools.colours.Colour;

public class TextureBuilder {
	
	private final File file;
	
	private boolean clampEdges = false;
	private boolean mipmap = true;
	private boolean anisotropic = true;
	private boolean nearest = false;
	private boolean clampToBorder = false;
	private Colour borderColour = new Colour( 0, 0, 0 );
	
	protected TextureBuilder( File textureFile ) {
		this.file = textureFile;
	}
	
	public TextureBuilder clampEdges() {
		this.clampEdges = true;
		clampToBorder = false;
		return this;
	}
	
	public TextureBuilder clampToBorder( float r, float g, float b, float a ) {
		borderColour.set( r, g, b, a );
		clampToBorder = true;
		clampEdges = false;
		return this;
	}
	
	public TextureBuilder noMipMap() {
		this.mipmap = false;
		this.anisotropic = false;
		return this;
	}
	
	public TextureBuilder nearestFiltering() {
		this.mipmap = false;
		this.nearest = true;
		return this;
	}
	
	public TextureBuilder noAnisotropicFiltering() {
		this.anisotropic = false;
		return this;
	}
	
	public boolean isClampEdges() { return clampEdges; }
	protected Colour getBorderColour() { return borderColour; }
	protected boolean isClampToBorder() { return clampToBorder; }
	protected boolean isMipMap() { return mipmap; }
	protected boolean isAnisotropic() { return anisotropic; }
	protected boolean isNearest() { return nearest; }
	
	public Texture create() {
		Texture texture = Texture.newEmptyTexture();
		TextureLoader loader = new TextureLoader( file, texture, this );
		loader.preDecodeTextureFile();
		BackgroundLoader.addOpenGlRequest( () -> loader.loadTexture() );
		return loader.getTexture();
	}
}
