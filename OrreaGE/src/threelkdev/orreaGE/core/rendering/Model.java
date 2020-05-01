package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Model {
	
	private final int vertexCount;
	protected final ModelNode[] nodes;
	private final int indexCount;
	private final int indexDataType;
	
	public Material defaultMaterial;
	
	public Model( ModelNode... nodes ) {
		this.nodes = nodes;
		
		int numVertices = 0, numIndices = 0;
		for( ModelNode node : nodes ) {
			numVertices += node.getNumVertices();
			numIndices += node.getNumIndices();
		}
		this.vertexCount = numVertices;
		this.indexCount = numIndices;
		
		this.indexDataType = vertexCount <= ( 2 * Byte.MAX_VALUE ) + 1 ? GL11.GL_UNSIGNED_BYTE : 
			( vertexCount <= ( 2 * Short.MAX_VALUE ) + 1 ? GL11.GL_UNSIGNED_SHORT : GL11.GL_UNSIGNED_INT );
	}
	
	public void setDefaultMaterial( Material material ) { this.defaultMaterial = material; }
	
	public int getVertexCount() { return vertexCount; }
	public int getIndexCount() { return indexCount; }
	// For ONE instance ONLY
	public int bytesPerIndex() { 
		switch( indexDataType ) {
		case GL11.GL_UNSIGNED_BYTE:
			return 1;
		case GL11.GL_UNSIGNED_SHORT:
			return 2;
		case GL11.GL_UNSIGNED_INT:
			default:
				return 4;
		}
	}
	
	public boolean hasNode( ModelNode modelNode ) {
		for( ModelNode node : nodes ) {
			if( node == modelNode ) return true;
		}
		return false;
	}
	
	public boolean hasNode( String nodeName ) {
		for( ModelNode node : nodes ) {
			if( node.getName().equalsIgnoreCase( nodeName ) )
				return true;
		}
		return false;
	}
	
	public ModelNode getNode( int index ) {
		if( index < nodes.length )
			return nodes[ index ];
		else return null;
	}
	
	public ModelNode getNode( String nodeName ) {
		for( ModelNode node : nodes ) {
			if( node.getName().equalsIgnoreCase( nodeName ) )
				return node;
		}
		return null;
	}
	
	public void storeRawData( ByteBuffer vertexBuffer ) {
		for( ModelNode node : nodes ) {
			node.storeRawData( vertexBuffer );
		}
	}
	
	public void storeRawData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer ) { storeRawData( vertexBuffer, indexBuffer, 0, indexDataType ); }
	public void storeRawData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, int indexOffset, int indexDataType ) {
		for( ModelNode node : nodes ) {
			node.storeRawData( vertexBuffer, indexBuffer, indexOffset, indexDataType );
			indexOffset += node.getNumVertices();
		}
	}
	
	public void storeInstanceData( ByteBuffer vertexBuffer, Matrix4f transform, Vector4f pass4f, Vector3f pass3f ) {
		for( ModelNode node : nodes ) {
			node.storeInstanceData( vertexBuffer, transform, pass4f, pass3f );
		}
	}
	
//	public void storeInstanceData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, Matrix4f transform, Material material ) { storeInstanceData( vertexBuffer, indexBuffer, 0, indexDataType, transform, material ); }
	public void storeInstanceData( ByteBuffer vertexBuffer, ByteBuffer indexBuffer, int indexOffset, int indexDataType, Matrix4f transform, Vector4f pass4f, Vector3f pass3f ) {//, Material material ) {
		int offset = indexOffset;
		for( ModelNode node : nodes ) {
			node.storeInstanceData( vertexBuffer, indexBuffer, offset, indexDataType, transform, pass4f, pass3f );
			offset += node.getNumVertices();
		}
	}
	
	public int getByteSize() {
		int total = 0;
		for( ModelNode node : nodes )
			total += node.getByteSize();
		return total;
	}
	

	private static Model CUBE, TETRA, OCTA;
	
	public static Model getFlatCube() {
		float r = 0.5f;
		Vector3f backNormal = new Vector3f( 0, 0, 1 );
		Vector3f rightNormal = new Vector3f( 1, 0, 0 );
		Vector3f frontNormal = new Vector3f( 0, 0, -1 );
		Vector3f leftNormal = new Vector3f( -1, 0, 0 );
		Vector3f topNormal = new Vector3f( 0, 1, 0 );
		Vector3f bottomNormal = new Vector3f( 0, -1, 0 );
		Vector3f pos000 = new Vector3f( -r, -r, -r );
		Vector3f pos010 = new Vector3f( -r, r, -r );
		Vector3f pos011 = new Vector3f( -r, r, r );
		Vector3f pos001 = new Vector3f( -r, -r, r );
		Vector3f pos100 = new Vector3f( r, -r, -r );
		Vector3f pos110 = new Vector3f( r, r, -r );
		Vector3f pos111 = new Vector3f( r, r, r );
		Vector3f pos101 = new Vector3f( r, -r, r );
		Vector2f frontTex0	= new Vector2f( 0.2500f, 0.2500f );
		Vector2f frontTex1	= new Vector2f( 0.5000f, 0.2500f );
		Vector2f frontTex2	= new Vector2f( 0.2500f, 0.5000f );
		Vector2f frontTex3	= new Vector2f( 0.5000f, 0.5000f );
		Vector2f topTex0	= new Vector2f( 0.2500f,      0f );
		Vector2f topTex1	= new Vector2f( 0.5000f,      0f );
		Vector2f topTex2	= new Vector2f( 0.2500f, 0.2500f );
		Vector2f topTex3	= new Vector2f( 0.5000f, 0.2500f );
		Vector2f bottomTex0	= new Vector2f( 0.2500f, 0.7500f );
		Vector2f bottomTex1	= new Vector2f( 0.5000f, 0.7500f );
		Vector2f bottomTex2	= new Vector2f( 0.2500f, 0.5000f );
		Vector2f bottomTex3	= new Vector2f( 0.5000f, 0.5000f );
		Vector2f backTex0	= new Vector2f( 0.2500f,      1f );
		Vector2f backTex1	= new Vector2f( 0.5000f,      1f );
		Vector2f backTex2	= new Vector2f( 0.2500f, 0.7500f );
		Vector2f backTex3	= new Vector2f( 0.5000f, 0.7500f );
		Vector2f rightTex0	= new Vector2f( 0.5000f, 0.2500f );
		Vector2f rightTex1	= new Vector2f( 0.7500f, 0.2500f );
		Vector2f rightTex2	= new Vector2f( 0.5000f, 0.5000f );
		Vector2f rightTex3	= new Vector2f( 0.7500f, 0.5000f );
		Vector2f leftTex0	= new Vector2f( 0.2500f, 0.2500f );
		Vector2f leftTex1	= new Vector2f(      0f, 0.2500f );
		Vector2f leftTex2	= new Vector2f( 0.2500f, 0.5000f );
		Vector2f leftTex3	= new Vector2f(      0f, 0.5000f );
		return new Model(
			new ModelNode( "cube", new ModelVertex[] {
				// Back, positive Z ('away')
				new ModelVertex( pos011, backNormal, backTex0 ),
				new ModelVertex( pos111, backNormal, backTex1 ),
				new ModelVertex( pos001, backNormal, backTex2 ),
				new ModelVertex( pos101, backNormal , backTex3),
				// Right, positive X
				new ModelVertex( pos110, rightNormal, rightTex0 ),
				new ModelVertex( pos111, rightNormal, rightTex1 ),
				new ModelVertex( pos100, rightNormal, rightTex2 ),
				new ModelVertex( pos101, rightNormal, rightTex3 ),
				// Front, negative Z ('towards')
				new ModelVertex( pos010, frontNormal, frontTex0 ),
				new ModelVertex( pos110, frontNormal, frontTex1 ),
				new ModelVertex( pos000, frontNormal, frontTex2 ),
				new ModelVertex( pos100, frontNormal, frontTex3 ),
				// Left, negative X
				new ModelVertex( pos010, leftNormal, leftTex0 ),
				new ModelVertex( pos011, leftNormal, leftTex1 ),
				new ModelVertex( pos000, leftNormal, leftTex2 ),
				new ModelVertex( pos001, leftNormal, leftTex3 ),
				// Top, positive Y
				new ModelVertex( pos011, topNormal, topTex0 ),
				new ModelVertex( pos111, topNormal, topTex1 ),
				new ModelVertex( pos010, topNormal, topTex2 ),
				new ModelVertex( pos110, topNormal, topTex3 ),
				// Bottom, negative Y
				new ModelVertex( pos001, bottomNormal, bottomTex0 ),
				new ModelVertex( pos101, bottomNormal, bottomTex1 ),
				new ModelVertex( pos000, bottomNormal, bottomTex2 ),
				new ModelVertex( pos100, bottomNormal, bottomTex3 )
			},  new int[] {
					0, 2, 1, 2, 3, 1,		// Back
					6, 4, 5, 6, 5, 7,		// Right
					8, 9, 10, 10, 9, 11,	// Front
					12, 14, 13, 14, 15, 13,	// Left
					16, 17, 18, 18, 17, 19,	// Top
					20, 22, 21, 22, 23, 21	// Bottom
			} )
		);
	}
	
	public static Model getCube() {
		if( CUBE == null ) {
			float r = 0.5f;
			Vector3f frontBottomLeft	= new Vector3f( -r, -r, r );
			Vector3f frontTopLeft		= new Vector3f( -r, r, r );
			Vector3f frontTopRight		= new Vector3f( r, r, r );
			Vector3f frontBottomRight	= new Vector3f( r, -r, r );
			Vector3f backBottomLeft		= new Vector3f( -r, -r, -r );
			Vector3f backTopLeft		= new Vector3f( -r, r, -r );
			Vector3f backTopRight		= new Vector3f( r, r, -r );
			Vector3f backBottomRight	= new Vector3f( r, -r, -r );
			CUBE = new Model(
				new ModelNode( "cube", new ModelVertex[] { 
						new ModelVertex( frontBottomLeft, frontBottomLeft, new Vector2f( 1f/4f,1f/4f ) ), //0 0
						new ModelVertex( frontTopLeft, frontTopLeft, new Vector2f( 1f/4f, 1f/2f ) ), // 0 1
						new ModelVertex( frontTopRight, frontTopRight, new Vector2f( 1f/2f, 1f/2f ) ), // 1 1
						new ModelVertex( frontBottomRight, frontBottomRight, new Vector2f( 1f/2f, 1f/4f ) ), // 1 0
						new ModelVertex( backBottomLeft, backBottomLeft, new Vector2f( 1f/4f, 0f ) ), // 1 0
						new ModelVertex( backTopLeft, backTopLeft, new Vector2f( 1f/4f, 3f/4f ) ), // 1 1
						new ModelVertex( backTopRight, backTopRight, new Vector2f( 1f/2f, 3f/4f ) ), // 0 1
						new ModelVertex( backBottomRight, backBottomRight, new Vector2f( 1f/2f, 0f ) ), // 0 0
						
						new ModelVertex( backBottomLeft, backBottomLeft, new Vector2f( 1f/4f, 1f ) ),
						new ModelVertex( backBottomRight, backBottomRight, new Vector2f( 1f/2f, 1f ) ),
						
						new ModelVertex( backBottomLeft, backBottomLeft, new Vector2f( 0f, 1f/4f ) ),
						new ModelVertex( backTopLeft, backTopLeft, new Vector2f( 0f, 1f/2f ) ),
						
						new ModelVertex( backBottomRight, backBottomRight, new Vector2f( 3f/4f, 1f/4f ) ),
						new ModelVertex( backTopRight, backTopRight, new Vector2f( 3f/4f, 2f/4f ) )
				},  new int[] { 
						0, 3, 2, 0, 2, 1, // Front - fBL, fBR, fTR, fBL, fTR, fTL
						3, 12, 13, 3, 13, 2, //Right
						10, 0, 1, 10, 1, 11, //Left
						9, 8, 5, 9, 5, 6, //Back
						1, 2, 6, 1, 6, 5, //Top
						3, 0, 4, 3, 4, 7 //Bottom
						} )
			);
		}
		return CUBE;
	}
	
	/*function get_texel_coords(x, y, tex_width, tex_height)
    u = (x + 0.5) / tex_width
    v = (y + 0.5) / tex_height
    return u, v
end*/
	
	public static Model getFlatHedron( int complexity, float height, float radius ) {
		ModelVertex[] verts = new ModelVertex[ complexity * 6 ];
		int v = 0, in = 0;
		int[] indices = new int[ complexity * 6 ];
		for( int i = 1; i <= complexity; i++ ) {
			Vector3f top = new Vector3f( 0, height / 2f, 0 );
			Vector3f bot = new Vector3f( 0, -height / 2f, 0 );
			Vector3f curr = new Vector3f(), last = new Vector3f(), topNormal = new Vector3f(), botNormal = new Vector3f();
			Vector2f currTex = new Vector2f();
			Vector2f lastTex = new Vector2f();
			curr.set(
				( float ) Math.cos( ( ( float ) i / ( float ) complexity ) * 2f * Math.PI ) * radius, 0,
				( float ) Math.sin( ( ( float ) i / ( float ) complexity ) * 2f * Math.PI ) * radius );
			last.set(
				( float ) Math.cos( ( ( float ) ( i - 1 ) / ( float ) complexity ) * 2f * Math.PI ) * radius, 0,
				( float ) Math.sin( ( ( float ) ( i - 1 ) / ( float ) complexity ) * 2f * Math.PI ) * radius );
			currTex.set( curr.x / ( 2f * radius ) + 0.5f , curr.z / ( 2f * radius ) + 0.5f );
			lastTex.set( last.x / ( 2f * radius ) + 0.5f , last.z / ( 2f * radius ) + 0.5f );
			
			Vector3f.add( top, curr, topNormal );
			Vector3f.add( topNormal, last, topNormal );
			topNormal.normalise();
			
			verts[ v++ ] = new ModelVertex( top, topNormal, new Vector2f( 0.5f, 0.5f ) );
			verts[ v++ ] = new ModelVertex( curr, topNormal, currTex );
			verts[ v++ ] = new ModelVertex( last, topNormal, lastTex );
			
			Vector3f.add( bot, curr, botNormal );
			Vector3f.add( botNormal, last, botNormal );
			botNormal.normalise();
			
			verts[ v++ ] = new ModelVertex( curr, botNormal, currTex );
			verts[ v++ ] = new ModelVertex( bot, botNormal, new Vector2f( 0.5f, 0.5f ) );
			verts[ v++ ] = new ModelVertex( last, botNormal, lastTex );

			indices[ in ] = in++;
			indices[ in ] = in++;
			indices[ in ] = in++;
			indices[ in ] = in++;
			indices[ in ] = in++;
			indices[ in ] = in++;
			
		}
		System.out.println(indices.length + " > " + indices[ indices.length - 1 ]);
		return new Model( new ModelNode( complexity + "-hedronFlat", verts, indices ) );
	}
	
	public static Model getHedron( int complexity, float height, float radius ) {
		Vector3f top = new Vector3f( 0, height / 2f, 0 );
		Vector3f bot = new Vector3f( 0, - height / 2f, 0 );
		ModelVertex[] verts = new ModelVertex[ complexity + 2 ];
		verts[ 0 ] = new ModelVertex( top, top, new Vector2f( 0.5f, 0.5f ) );
		verts[ 1 ] = new ModelVertex( bot, bot, new Vector2f( 0.5f, 0.5f ) );
		int[] indices = new int[ 2 * complexity * 3 ];
		for( int i = 0; i < complexity; i++ ) {
			Vector3f pos;
			Vector2f tex;
			pos = new Vector3f( 
					( float ) Math.cos( ( ( float ) i / ( float ) complexity ) * 2f * Math.PI ) * radius, 0,
					( float ) Math.sin( ( ( float ) i / ( float ) complexity ) * 2f * Math.PI ) * radius );
			tex = new Vector2f( pos.x / ( 2f * radius ) + 0.5f , pos.z / ( 2f * radius ) + 0.5f );
			verts[ i + 2 ] = new ModelVertex( pos, pos, tex );
			if( i > 0 ) {
				int x = ( i - 1 ) * 6;
				indices[ x++ ] = 0;		// top
				indices[ x++ ] = i + 2;	// current
				indices[ x++ ] = i + 1; // prev
				indices[ x++ ] = i + 1; // prev
				indices[ x++ ] = i + 2;	// current
				indices[ x++ ] = 1;		// bottom
			}
			if( i == complexity - 1) {
				int x = ( i ) * 6;
				indices[ x++ ] = 0;		// top
				indices[ x++ ] = 2;		// first
				indices[ x++ ] = i + 2; // current
				indices[ x++ ] = i + 2; // current
				indices[ x++ ] = 2;		// first
				indices[ x++ ] = 1;		// bottom
			}
		}
		return new Model( new ModelNode( complexity + "-hedron", verts, indices ) );
		
	}
	
	public static Model getTetrahedron() {
		if( TETRA == null ) {
			Vector3f v1 = new Vector3f( ( float ) Math.sqrt( 8d / 9d ), 0, -1f / 3f );
			Vector3f v2 = new Vector3f( -( float ) Math.sqrt( 2d / 9d), ( float ) Math.sqrt( 2d / 3d ), -1f / 3f );
			Vector3f v3 = new Vector3f( v2.x, - v2.y, v2.z );
			Vector3f v4 = new Vector3f( 0, 0, 1 );
			TETRA = new Model(
				new ModelNode( "tetrahedron", new ModelVertex[] {
						new ModelVertex( v1, v1, new Vector2f( 0, 1 ) ),
						new ModelVertex( v2, v2, new Vector2f( 0, 1 ) ),
						new ModelVertex( v3, v3, new Vector2f( 0, 1 ) ),
						new ModelVertex( v4, v4, new Vector2f( 0, 1 ) )
				},  new int [] {
						0, 2, 1,
						0, 3, 2,
						0, 1, 3,
						1, 2, 3 //
				} )
			);
		}
		return TETRA;
	}
	
	public static Model getOctahedron() {
		if( OCTA == null ) {
			float r = 0.5f;
			Vector3f top = new Vector3f( 0, r, 0 );
			Vector3f v1 = new Vector3f( -r, 0, 0 );
			Vector3f v2 = new Vector3f( 0, 0, -r );
			Vector3f v3 = new Vector3f( r, 0, 0 );
			Vector3f v4 = new Vector3f( 0, 0, r );
			Vector3f bot = new Vector3f( 0, -r, 0 );
			OCTA = new Model(
				new ModelNode( "octahedron", new ModelVertex[] {
						new ModelVertex( top, top, new Vector2f( 0, 1 ) ),
						new ModelVertex( v1, v1, new Vector2f( 0, 1 ) ),
						new ModelVertex( v2, v2, new Vector2f( 0, 1 ) ),
						new ModelVertex( v3, v3, new Vector2f( 0, 1 ) ),
						new ModelVertex( v4, v4, new Vector2f( 0, 1 ) ),
						new ModelVertex( bot, bot, new Vector2f( 0, 1 ) )
				}, new int[] {
						0, 2, 1,
						0, 3, 2,
						0, 4, 3,
						0, 1, 4,
						2, 5, 1,
						3, 5, 2,
						4, 5, 3,
						1, 5, 4
				} )
			);
		}
		return OCTA;
	}
	
}
