package threelkdev.orreaGE.core.rendering.objects;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.tools.colours.Colour;

public class Light {
	
	private Vector3f direction = new Vector3f( 0, -1, 0 );
	private Colour colour = new Colour( 1, 1, 1 );
	private Vector2f bias = new Vector2f( 1, 0 );
	
	public Light( float diffuse, float ambient ) { 
		this.bias.set( ambient, diffuse );
	}
	
	public Light( Vector3f direction, Colour colour, float diffuse, float ambient ) {
		this.direction.set( direction );
		this.colour.setColour( colour );
		this.bias.set( ambient, diffuse );
	}
	
	public void setDirection( Vector3f dir ) { direction.set( dir ).normalise(); }
	
	public void setColour( Colour colour ) { this.colour.set( colour ); }
	
	public Vector3f getDirection() { return direction; }
	public Colour getColour() { return colour; }
	public Vector2f getBias() { return bias; }
}
