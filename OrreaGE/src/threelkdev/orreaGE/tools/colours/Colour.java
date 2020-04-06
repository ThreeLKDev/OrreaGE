package threelkdev.orreaGE.tools.colours;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.tools.math.Maths;
import threelkdev.orreaGE.tools.pools.Vec3Pool;

public class Colour {
	
	private Vector3f col = new Vector3f();
	private float a = 1f;
	
	/*
	 * @param hex - Colour value as a packed ARGB hexadecimal integer
	 */
	public Colour( int hex ) {
		this.set( hex );
	}
	
	public Colour( float r, float g, float b ) {
		col.set( r, g, b );
	}
	
	public Colour( Vector3f colour ) {
		col.set( colour );
	}
	
	public Colour( float r, float g, float b, float a ) {
		col.set( r, g, b );
		this.a = a;
	}
	
	public Colour( float r, float g, float b, boolean convert ) {
		if( convert )
			col.set( r / 255f, g / 255f, b / 255f );
		else
			col.set( r, g, b );
	}
	
	public Colour( Vector3f colour, float a ) {
		col.set( colour );
		this.a = a;
	}
	
	public Colour() {}
	
	public Vector3f getVector() { return col; }
	
	public FloatBuffer getAsFloatBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer( 4 );
		buffer.put( new float[] { col.x, col.y, col.z, a } );
		buffer.flip();
		return buffer;
	}
	
	public float getR() { return col.x; }
	public float getG() { return col.y; }
	public float getB() { return col.z; }
	public float getA() { return a; }
	
	public Colour duplicate() {
		return new Colour( col, a );
	}
	
	public Colour lighten( int hex ) {
		return lighten( ( float ) ( hex & 0xFF ) / ( float ) 0xFF );
	}
	
	public Colour lighten( float amount ) {
		this.col.x = Maths.clamp( this.col.x + amount, 0, 1f );
		this.col.y = Maths.clamp( this.col.y + amount, 0, 1f );
		this.col.z = Maths.clamp( this.col.z + amount, 0, 1f );
		return this;
	}
	
	public Colour darken( int hex ) { return darken( ( float ) ( hex & 0xFF ) / ( float ) 0xFF ); }
	public Colour darken( float amount ) {
		this.col.x = Maths.clamp( this.col.x - amount, 0, 1f );
		this.col.y = Maths.clamp( this.col.y - amount, 0, 1f );
		this.col.z = Maths.clamp( this.col.z - amount, 0, 1f );
		return this;
	}
	
	public void multiplyBy( Colour colour ) {
		this.col.x *= colour.col.x;
		this.col.y *= colour.col.y;
		this.col.z *= colour.col.z;
	}
	
	/**
	 * @param hex - The new Colour value, in a packed RGB hexadecimal integer
	 */
	public void setColour( int hex ) {
		col.set( 
				( float ) ( ( hex >> 16 ) & 0xFF ) / ( float ) 0xFF,
				( float ) ( ( hex >> 8 ) & 0xFF ) / ( float ) 0xFF,
				( float ) ( hex & 0xFF ) / ( float ) 0xFF );
	}
	
	public void setColour( float r, float g, float b ) { col.set( r, g, b ); }
	public void setColour( Vector3f colour ) { col.set( colour ); }
	public void setColour( Colour colour ) { col.set( colour.col ); }
	
	/**
	 * @param hex - The new Colour and Alpha values, in a packed ARGB hexadecimal integer
	 */
	public void set( int hex ) {
		this.setColour( hex );
		this.a = ( float ) ( ( hex >> 24 ) & 0xFF ) / ( float ) 0xFF;
	}
	
	/** Ideologically identical to the overloaded set( int ), with a different bit order. */
	public void setRGBA( int hex ) {
		col.set( ( float ) ( ( hex >> 24 ) & 0xFF ) / ( float ) 0xFF,
			( float ) ( ( hex >> 16 ) & 0xFF ) / ( float ) 0xFF,
			( float ) ( ( hex >> 8 ) & 0xFF ) / ( float ) 0xFF );
		this.a = ( float ) ( hex & 0xFF ) / ( float ) 0xFF;
	}
	
	public void set( float r, float g, float b, float a ) { col.set( r, g, b ); this.a = a; }
	public void set( Vector3f colour, float a) { col.set( colour ); this.a = a; }
	public void set( Colour colour ) { col.set( colour.col ); this.a = colour.a; }
	public void set( Colour colour, float a ) { col.set( colour.col ); this.a = a; }
	
	public void setR( float r ) { col.x = r; }
	public void setG( float g ) { col.y = g; }
	public void setB( float b ) { col.z = b; }
	public void setA( float a ) { this.a = a; }
	
	public Colour scale( float value ) {
		col.scale( value );
		return this;
	}
	
	public String toString() {
		return ( "(" + col.x + ", " + col.y + ", " + col.z + ", " + a + " )" );
	}
	
	public static Colour sub( Colour colLeft, Colour colRight, Colour dest ) {
		if( dest == null ) {
			Vector3f newCol = Vector3f.sub( colLeft.col, colRight.col, Vec3Pool.get() );
			Colour col = new Colour( newCol );
			Vec3Pool.release( newCol );
			return col;
		} else {
			Vector3f.sub( colLeft.col, colRight.col, dest.col );
			return dest;
		}
	}
	
	public static float calculateDifference( Colour colA, Colour colB ) {
		return Colour.sub( colB,  colA,  null ).length();
	}
	
	public Colour getUnit() {
		Colour colour = new Colour();
		if( col.x == 0 && col.y == 0 && col.z == 0 ) {
			return colour;
		}
		colour.setColour( this );
		colour.scale( 1f / length() );
		return colour;
	}
	
	public float length() {
		return ( float ) Math.sqrt( lengthSquared() );
	}
	
	public float lengthSquared() {
		return col.lengthSquared();
	}
	
	public byte[] asBytesRGB() {
		return new byte[] { ( byte ) ( col.x * 255f), ( byte ) ( col.y * 255f ), ( byte ) ( col.z * 255f ) };
	}
	
	public byte[] asBytesRGBA() {
		return new byte[] { ( byte ) ( col.x * 255f), ( byte ) ( col.y * 255f ), ( byte ) ( col.z * 255f ), ( byte ) ( a * 255f ) };
	}
	
	public HsvColour convertToHsv() {
		float[] hsv = new float[ 3 ];
		Color.RGBtoHSB( ( int ) ( col.x * 255 ), ( int ) ( col.y * 255 ), ( int ) ( col.z * 255 ), hsv );
		return new HsvColour( hsv[0], hsv[1], hsv[2] );
	}
	
	public static Colour hsvToRgb( float hue, float saturation, float value ) {
		int h = ( int ) ( hue * 6 );
		float f = hue * 6 - h;
		float p = value * ( 1 - saturation );
		float q = value * ( 1 - f * saturation );
		float t = value * ( 1 - ( 1 - f ) * saturation );
		switch( h ) {
		case 0:
			return new Colour( value, t, p );
		case 1:
			return new Colour( q, value, p );
		case 2:
			return new Colour( p, value, t );
		case 3:
			return new Colour( p, q, value );
		case 4:
			return new Colour( t, p, value );
		case 5:
			return new Colour( value, p, q );
		default:
			return new Colour();
		}
	}
	
	public static Colour interpolateColours( Colour colour1, Colour colour2, float blend, Colour dest ) {
		float colour1Weight = 1 - blend;
		float r = ( colour1Weight * colour1.col.x ) + ( blend * colour2.col.x );
		float g = ( colour1Weight * colour1.col.y ) + ( blend * colour2.col.y );
		float b = ( colour1Weight * colour1.col.z ) + ( blend * colour2.col.z );
		if( dest == null ) {
			return new Colour( r, g, b );
		} else {
			dest.setColour( r, g, b );
			return dest;
		}
	}
	
	public static Colour add( Colour colour1, Colour colour2, Colour dest ) {
		if( dest == null ) {
			return new Colour( Vector3f.add( colour1.col, colour2.col, null ) );
		} else {
			Vector3f.add( colour1.col, colour2.col, dest.col );
			return dest;
		}
	}
	
}
