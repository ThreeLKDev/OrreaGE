package threelkdev.orreaGE.core.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.rendering.ModelData;
import threelkdev.orreaGE.core.rendering.SectionData;
import threelkdev.orreaGE.core.rendering.VertexData;

public class ShapeFactory {
	
	private static ModelData plane;
	
	public static byte[] getPlaneInstance( Matrix4f transform, byte[] material ) {
		
		if( plane == null ) {
			Vector3f normal = new Vector3f( 0, 0, -1 );
			plane = new ModelData( 4, new SectionData( 
				new VertexData[] {
						new VertexData( new Vector3f( 0, 0, 0 ), normal, ( byte ) 0 ),
						new VertexData( new Vector3f( 1, 0, 0 ), normal, ( byte ) 0 ),
						new VertexData( new Vector3f( 1, 1, 0 ), normal, ( byte ) 0 ),
						new VertexData( new Vector3f( 0, 1, 0 ), normal, ( byte ) 0 )
				}, null, new int[] { 0, 1, 2, 2, 3, 0 }
			) );
		}
		
		return plane.getInstanceData( transform, material );
	}
	
}
