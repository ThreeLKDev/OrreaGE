package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.tools.pools.Vec3Pool;
import threelkdev.orreaGE.tools.pools.Vec4Pool;

public class TestModelBatch implements IModelBatch {
	
	private Vao vao;
	private int vertexCount, indexCount;
	
	final Model model;
	public List< ModelInstance > instances;
	
	public TestModelBatch( Model model ) {
		this.model = model;
		this.instances = new ArrayList< ModelInstance >();
	}
	
	public void addInstance( ModelInstance instance ) {
		instances.add( instance );
	}
	
	@Override
	public boolean isVisible() { return true; }

	@Override
	public float getAlpha() { return 1f; }
	
	public void prepVao() {
		vao = Vao.create();
		vao.bind();
		
		vertexCount = model.getVertexCount() * instances.size();
		indexCount = model.getIndexCount() * instances.size();
		
		int indexDataType, bytesPerIndex;
		if( vertexCount <= ( Byte.MAX_VALUE * 2 ) + 1 ) {
			indexDataType = GL11.GL_UNSIGNED_BYTE;
			bytesPerIndex = Byte.BYTES;
		} else if( vertexCount <= ( Short.MAX_VALUE * 2 ) + 1 ) {
			indexDataType = GL11.GL_UNSIGNED_SHORT;
			bytesPerIndex = Short.BYTES;
		} else {
			indexDataType = GL11.GL_UNSIGNED_INT;
			bytesPerIndex = Integer.BYTES;
		}
		
		ByteBuffer vertexBuffer = BufferUtils.createByteBuffer( vertexCount * ModelVertex.BYTES );
		ByteBuffer indexBuffer = indexCount != 0 ? BufferUtils.createByteBuffer( indexCount * bytesPerIndex ) : null;
		Vector4f pass4f = Vec4Pool.get();
		Vector3f pass3f = Vec3Pool.get();
		for( int i = 0; i < instances.size(); i++ ) {
			model.storeInstanceData( vertexBuffer, indexBuffer, i * model.getVertexCount(), indexDataType, instances.get( i ).transform, pass4f, pass3f );
		}
		Vec4Pool.release( pass4f );
		Vec3Pool.release( pass3f );
		
		vertexBuffer.flip();
		vao.initDataFeed( vertexBuffer, GL15.GL_STATIC_DRAW, new Attribute( 0, GL11.GL_FLOAT, 3 ), 
				new Attribute( 1, GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4, true ),
				new Attribute( 2, GL11.GL_UNSIGNED_BYTE, 3, true ) );
		
		if( indexBuffer != null ) {
			indexBuffer.flip();
			vao.createIndexBuffer( indexBuffer, indexDataType );
		}
		
		vao.unbind();
	}
	
	@Override
	public Vao getVao() { return vao; }
	

	@Override
	public int getVertexCount() { return vertexCount; }
	
	@Override
	public int getIndexCount() { return indexCount; }

}
