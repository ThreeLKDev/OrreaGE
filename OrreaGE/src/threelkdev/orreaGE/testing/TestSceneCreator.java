package threelkdev.orreaGE.testing;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import the3lks.orreaEngine.core.loading.CsvModelLoader;
import the3lks.orreaEngine.core.rendering.ModelData;
import the3lks.orreaEngine.core.rendering.VertexData;
import the3lks.orreaEngine.core.rendering.openGL.objects.Attribute;
import the3lks.orreaEngine.core.rendering.openGL.objects.IStaticBatch;
import the3lks.orreaEngine.core.rendering.openGL.objects.Vao;
import the3lks.orreaEngine.testing.rendering.objects.Model;
import the3lks.orreaEngine.tools.files.FileUtils;

public class TestSceneCreator {
	
	private static final int MODEL_COUNT = 30;
	private static final File MODEL_FILE = FileUtils.getResource( FileUtils.ResourceType.MODEL, "polyTree.txt" );
	
	public static List< IStaticBatch > generateData() throws Exception{
		Model model = loadModelData();
		ByteBuffer buffer = getSceneData( model );
		Vao vao = storeDataInVao( buffer );
		return initBatches( vao, model );
	}
	
	public static Model loadModelData() throws Exception {
		CsvModelLoader cml = new CsvModelLoader();
		Model model = cml.loadModel( MODEL_FILE );
		return model;
	}
	
	private static ByteBuffer getSceneData( ModelData model ) {
		ByteBuffer buffer = BufferUtils.createByteBuffer( model.getVertexCount() * MODEL_COUNT * VertexData.BYTES_PER_VERTEX );
		Matrix4f transform = new Matrix4f();
		Random rand = new Random();
		for( int i = 0; i < MODEL_COUNT; i++ ) {
			transform.setIdentity();
			transform.translate( new Vector3f( rand.nextFloat() * 100, 0, rand.nextFloat() * 100 ) );
			transform.rotate( ( float ) ( 2 * Math.PI * rand.nextFloat() ), new Vector3f( 0, 1, 0 ) );
			float scale = 0.4f + rand.nextFloat() * 0.4f * 0.01f;
			transform.scale( new Vector3f( scale, scale, scale ) );
			byte[] rawModelData = model.getInstanceData( transform, new byte[] { 0, 0, 0 } );
			buffer.put( rawModelData );
		}
		return buffer;
	}
	
	private static Vao storeDataInVao( ByteBuffer buffer ) {
		Vao vao = Vao.create();
		vao.bind();
		buffer.flip();
		vao.initDataFeed( buffer,  GL15.GL_STATIC_DRAW, new Attribute( 0, GL11.GL_FLOAT, 3),
				new Attribute( 1, GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4, true ),
				new Attribute( 2, GL11.GL_UNSIGNED_BYTE, 4, true ) );
		vao.unbind();
		return vao;
	}
	
	private static List< IStaticBatch > initBatches( Vao vao, ModelData model ){
		IStaticBatch batch = new TestStaticBatch( vao, model.getVertexCount() * MODEL_COUNT );
		List< IStaticBatch > batches = new ArrayList< IStaticBatch >();
		batches.add( batch );
		return batches;
	}
	
}
