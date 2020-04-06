package threelkdev.orreaGE.core.rendering;

import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;

public class Material {
	
	public static final int COLOUR_DIFFUSE	= 0b0001;
	public static final int ALPHA			= 0b0010;
	public static final int TEXTURE			= 0b0100;
	public static final int MAX				= 0b1111;
	
	public static final float NO_ALPHA = -1;
	
	public Vector3f colour;
	public float alpha = NO_ALPHA;
	public final Texture texture;
	
	private int usage = 0;
	
	public Material( Texture texture ) {
		this.texture = texture;
		if( texture != null ) {
			usage |= TEXTURE;
		}
	}
	
	public boolean hasAlpha() { return alpha != NO_ALPHA && hasFlag( ALPHA ); }
	
	public byte[] getBytes() {
		return new byte[] { 0, 0, 0, 0 };
	}
	
	public boolean hasFlag( int flag ) {
		return ( usage & flag ) == flag;
	}
	
	public boolean hasFlags( int... flags ) {
		boolean result = true;
		for( int flag : flags ) {
			result = result && ( ( this.usage & flag ) == flag );
			if( !result ) break;
		}
		return result;
	}
	
	public void setDiffuse( Vector3f colour ) {
		this.colour = colour;
		this.usage |= COLOUR_DIFFUSE;
	}
	
	public void setAlpha( float alpha ) {
		this.alpha = alpha;
		this.usage |= ALPHA;
	}
	
	/* FIXME
	public void setTexture( Texture texture ) {
		this.texture = texture;
		this.usage |= TEXTURE;
	}
	*/
	
}