package threelkdev.orreaGE.testing.ui;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.tools.colours.Colour;

public class Quad {
	
	public static final int VERTEX_COUNT = 4;
	public static final int INDICES_COUNT = 6;
	
	private Vector3f position = new Vector3f( 50f, 0, 0 ); ;
	
	private Vao vao;
	private Colour colour = new Colour( 0x4488aa );
	private boolean loaded = false;
	
	public void init() {
		QuadVertex[] vertices = new QuadVertex[ VERTEX_COUNT ];
		byte[] colour = this.colour.asBytesRGBA();
		vertices[ 0 ] = new QuadVertex( new Vector3f( 0, 0, 0 ), new Vector3f( 0, 0, 1 ), colour );
		vertices[ 1 ] = new QuadVertex( new Vector3f( 50, 0, 0 ), new Vector3f( 0, 0, 1 ), colour );
		vertices[ 2 ] = new QuadVertex( new Vector3f( 50, 100, 0 ), new Vector3f( 0, 0, 1 ), colour );
//		vertices[ 3 ] = new QuadVertex( new Vector3f( 50, 100, 0 ), new Vector3f( 0, 0, 1 ), colour );
		vertices[ 3 ] = new QuadVertex( new Vector3f( 0, 100, 0 ), new Vector3f( 0, 0, 1 ), colour );
//		vertices[ 5 ] = new QuadVertex( new Vector3f( 0, 0, 0 ), new Vector3f( 0, 0, 1 ), colour );
		
		ByteBuffer buffer = MemoryUtil.memAlloc( VERTEX_COUNT * QuadVertex.BYTES_PER_VERTEX );
		ByteBuffer indices = MemoryUtil.memAlloc( INDICES_COUNT * Byte.BYTES );
		indices.put( new byte[] { 0, 1, 2, 2, 3, 0 } );
		indices.flip();
		
		for( QuadVertex vert : vertices )
			vert.storeData( buffer );
		
		Vao vao = Vao.create();
		vao.bind();
		buffer.flip();
		vao.initDataFeed( buffer,  GL15.GL_STATIC_DRAW, new Attribute( 0, GL11.GL_FLOAT, 3 ),
				new Attribute( 1, GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4, true ),
				new Attribute( 2, GL11.GL_UNSIGNED_BYTE, 4, true ) );
		vao.createIndexBuffer( indices, GL11.GL_UNSIGNED_BYTE );
		vao.unbind();
		MemoryUtil.memFree( buffer );
		setMesh( vao );
	}
	
	public void setMesh( Vao vao ) {
		this.vao = vao;
		this.loaded = true;
	}
	
	public Vao getVao() { return this.vao; }
	public Colour getColour() { return this.colour; }
	public boolean isLoaded() { return this.loaded; }
	public Vector3f getPosition() { return this.position; }
	
	public void setColour( Colour colour ) {
		this.colour.setColour( colour );
	}
	
	
}
