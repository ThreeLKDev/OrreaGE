package threelkdev.orreaGE.core.rendering;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vbo;
import threelkdev.orreaGE.tools.pools.Vec3Pool;
import threelkdev.orreaGE.tools.pools.Vec4Pool;
import threelkdev.orreaGE.tools.utils.Array;
import threelkdev.orreaGE.tools.utils.Pair;

public class ModelMaterialBatch extends AModelMaterialBatch {
	
	//ModelMaterialBatch:
	// Based around a single Model, handling multiple Materials
	
	// The Model that this batch is based around
	final Model model;
	
//	Array< ModelInstance > instances;
	Map< Material, Pair< Vao, Integer > > matMap;
	Map< Material, TrackedInstanceArray > instanceMap;
	Map< Material, Integer > materialCount;
	
	public ModelMaterialBatch( Model model ) {
		this.model = model;
		this.matMap = new HashMap<>();
		this.instanceMap = new HashMap<>();
		this.materialCount = new HashMap<>();
	}
	
	@Override
	public void notifyTransform(ModelInstance instance) {
		TrackedInstanceArray tia = instanceMap.get( instance.getMaterial()  == null ? model.defaultMaterial : instance.getMaterial() );
		if( tia != null ) {
			tia.pendingChanges++;
		}
	}
	
	@Override
	public void notifyMaterialChange( ModelInstance instance, Material from, Material to ) {
		TrackedInstanceArray fromTIA = instanceMap.get( from );
		TrackedInstanceArray toTIA = instanceMap.get( to );
		if( fromTIA != null ) {
			fromTIA.remove( instance );
			if( fromTIA.instances.size <= 0 ) {
				instanceMap.remove( from );
			}
		}
		if( toTIA == null ) {
			toTIA = new TrackedInstanceArray();
			instanceMap.put( to, toTIA );
		}
		toTIA.add( instance );
	}
	
	@Override
	protected void onAddInstance( ModelInstance instance ) {
		Material material = instance.getMaterial() == null ? model.defaultMaterial : instance.getMaterial();
		TrackedInstanceArray tia = instanceMap.get( material );
		if( tia == null ) {
			tia = new TrackedInstanceArray();
			instanceMap.put( material, tia );
		}
		tia.add( instance );
	}

	@Override
	protected void onRemoveInstance( ModelInstance instance ) {
		Material material = instance.getMaterial() == null ? model.defaultMaterial : instance.getMaterial();
		TrackedInstanceArray tia = instanceMap.get( material );
		if( tia != null ) {
			tia.remove( instance );
			if( tia.instances.size <= 0 ) {
				instanceMap.remove( material );
			}
		}
	}
	
	public Map< Material, Pair< Vao, Integer > > getVaoMap(){ return matMap; }
	
	boolean init = false;
	
	public void prepare() {
		if( !init ) {
			initVaos();
		}
		for( Entry< Material, TrackedInstanceArray > matEntry : instanceMap.entrySet() ) {
			
			matEntry.getValue().prepVao( model );
			
		}
		
	}
	
	@SuppressWarnings("boxing")
	private void initVaos() {
		init = true;
		matMap.clear();
		for( Entry< Material, TrackedInstanceArray > matEntry : instanceMap.entrySet() ) {
			
			matEntry.getValue().initVao( model );
			
			matMap.put( matEntry.getKey(), new Pair< Vao, Integer >( matEntry.getValue().vao, matEntry.getValue().vaoPassInteger ) );
			
		}
	}
	
	private static class TrackedInstanceArray {
		
		Array< ModelInstance > instances;
		boolean needsRebuilding = false;
		int pendingChanges = 0;
		Vao vao;
		int vaoPassInteger;
		
		TrackedInstanceArray() {
			this.instances = new Array<>( true, 1, ModelInstance.class );
		}
		
		void initVao( Model model ) {
			int vertexCount = instances.size * model.getVertexCount();
			int indexCount = instances.size * model.getIndexCount();
			
			vao = Vao.create();
			vao.bind();
			
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
			int indexOffset = 0, modelVertCount = model.getVertexCount();
			Vector4f pass4f = Vec4Pool.get();
			Vector3f pass3f = Vec3Pool.get();
			for( ModelInstance instance : instances ) {
				model.storeInstanceData( vertexBuffer, indexBuffer, indexOffset, indexDataType, instance.transform, pass4f, pass3f );
				indexOffset += modelVertCount;
			}
			Vec4Pool.release( pass4f );
			Vec3Pool.release( pass3f );
			
			vertexBuffer.flip();
			vao.initDataFeed( vertexBuffer, GL15.GL_STATIC_DRAW, new Attribute( 0, GL11.GL_FLOAT, 3 ),
					new Attribute( 1, GL11.GL_FLOAT, 3, true ),
					new Attribute( 2, GL11.GL_FLOAT, 2, true ) );
			
			if( indexBuffer != null ) {
				indexBuffer.flip();
				vao.createIndexBuffer( indexBuffer, indexDataType );
			}
			
			vao.unbind();
			vaoPassInteger = indexBuffer != null ? indexCount : vertexCount;
			needsRebuilding = false;
		}
		
		void prepVao( Model model ) {
			if( vao == null || needsRebuilding ) {
				initVao( model );
				return;
			}
			if( pendingChanges > 0 ) {
				ModelInstance instance;
				int modelByteSize = model.getByteSize();
				int vaoTotalSize = modelByteSize * instances.size;
				
				int instanceDataStart;
				Vector4f pass4f = Vec4Pool.get();
				Vector3f pass3f = Vec3Pool.get();
				
				Vbo vbo = vao.relatedVbos.get( 0 );
				vbo.bind();
				ByteBuffer vertexBuffer = GL15.glMapBuffer( vbo.getType(), GL15.GL_READ_WRITE, vaoTotalSize, null );
				
				for( int i = 0; i < instances.size; i++ ) {
					instance = instances.get( i );
					if( instance.wasTransformed() ) {
						instanceDataStart = modelByteSize * i;
						
						vertexBuffer.position( instanceDataStart );
						model.storeInstanceData( vertexBuffer, instance.transform, pass4f, pass3f);
						GL15.glUnmapBuffer( vbo.getType() );
						
						instance.transformNoted();
						pendingChanges--;
					}
					if( pendingChanges == 0 ) break;
				}

				vbo.unbind();
				
				Vec4Pool.release( pass4f );
				Vec3Pool.release( pass3f );
			}
		}
		
		void remove( ModelInstance instance ) {
			instances.removeValue( instance, true );
			needsRebuilding = true;
		}
		
		void add( ModelInstance instance ) {
			instances.add( instance );
			needsRebuilding = true;
		}
		
	}
	
}
