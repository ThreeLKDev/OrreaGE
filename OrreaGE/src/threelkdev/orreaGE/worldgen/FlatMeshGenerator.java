package threelkdev.orreaGE.worldgen;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.loading.BackgroundLoader;
import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.core.rendering.terrain.TerrainMesh;

public class FlatMeshGenerator implements TerrainMeshGenerator {
	
	@Override
	public TerrainMesh generate() {
		TerrainVertex[] vertices = generateVertices();
		int byteSize = vertices.length * TerrainVertex.BYTES_PER_VERTEX;
		ByteBuffer buffer = MemoryUtil.memAlloc( byteSize );
		storeDataInBuffer( vertices, buffer );
		TerrainMesh mesh = new TerrainMesh( vertices.length );
		loadToOpengl( mesh, buffer );
		return mesh;
	}
	
	private TerrainVertex[] generateVertices() {
		TerrainVertex[] vertices = new TerrainVertex[ 6 ];
		byte[] colour = new byte[] { ( byte ) 157, ( byte ) 180, 99 };
		vertices[ 0 ] = new TerrainVertex( new Vector3f( 0, 0, 0 ), new Vector3f( 0, 1, 0 ), colour, true );
		vertices[ 1 ] = new TerrainVertex( new Vector3f( 0, 0, 100 ), new Vector3f( 0, 1, 0 ), colour, true );
		vertices[ 2 ] = new TerrainVertex( new Vector3f( 100, 0, 100 ), new Vector3f( 0, 1, 0 ), colour, true );
		vertices[ 3 ] = new TerrainVertex( new Vector3f( 100, 0, 100 ), new Vector3f( 0, 1, 0 ), colour, true );
		vertices[ 4 ] = new TerrainVertex( new Vector3f( 100, 0, 0 ), new Vector3f( 0, 1, 0 ), colour, true );
		vertices[ 5 ] = new TerrainVertex( new Vector3f( 0, 0, 0 ), new Vector3f( 0, 1, 0 ), colour, true );
		return vertices;
	}
	
	private void storeDataInBuffer( TerrainVertex[] vertices, ByteBuffer buffer ) {
		for( TerrainVertex vertex : vertices )
			vertex.storeData( buffer );
	}
	
	private static Vao storeDataInVao( ByteBuffer buffer ) {
		Vao vao = Vao.create();
		vao.bind();
		buffer.flip();
		vao.initDataFeed( buffer,  GL15.GL_STATIC_DRAW, new Attribute( 0, GL11.GL_FLOAT, 3 ),
				new Attribute( 1, GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4, true ),
				new Attribute( 2, GL11.GL_UNSIGNED_BYTE, 4, true ) );
		vao.unbind();
		return vao;
	}
	
	private void loadToOpengl( TerrainMesh mesh, ByteBuffer buffer ) {
		BackgroundLoader.addOpenGlRequest( () -> {
			Vao vao = storeDataInVao( buffer );
			MemoryUtil.memFree( buffer );
			mesh.setMesh( vao );
		});
	}
	
}
