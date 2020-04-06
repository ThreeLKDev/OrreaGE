package threelkdev.orreaGE.testing;

import org.lwjgl.util.vector.Vector3f;

import the3lks.orreaEngine.core.rendering.ModelData;
import the3lks.orreaEngine.core.rendering.SectionData;
import the3lks.orreaEngine.core.rendering.VertexData;
import the3lks.orreaEngine.tools.files.CsvReader;
import the3lks.orreaEngine.tools.utils.DataUtils;

public class ModelDataLoader {
	
	public static ModelData loadModel( CsvReader reader, float size ) {
		reader.nextLine();
		int vertexCount = reader.getNextInt();
		int sectionsCount = reader.getNextInt();
		SectionData[] sections = new SectionData[ sectionsCount ];
		for( int i = 0; i < sectionsCount; i++ ) {
			sections[ i ] = loadSection( reader, size );
		}
		return new ModelData( vertexCount, sections );
	}
	
	private static SectionData loadSection( CsvReader reader, float size ) {
		reader.nextLine();
		int vertexCount = reader.getNextInt();
		byte[] colour = reader.getNextByteArray( 3 );
		float flexibility = reader.getNextFloat();
		VertexData[] vertices = loadVertices( reader, flexibility, size, vertexCount );
		return new SectionData( vertices, colour );
	}
	
	private static VertexData[] loadVertices( CsvReader reader, float flexibility, float size, int vertexCount ) {
		reader.nextLine();
		VertexData[] vertices = new VertexData[ vertexCount ];
		for( int i = 0; i < vertexCount; i++ )
			vertices[ i ] = loadVertex( reader, flexibility, size );
		return vertices;
	}
	
	private static VertexData loadVertex( CsvReader reader, float flexibility, float size ) {
		Vector3f position = reader.getNextVector();
		position.scale( size );
		byte actualFlex = DataUtils.quantizeToByte( position.y * flexibility, 1, false );
		Vector3f normal = reader.getNextVector();
		return new VertexData( position, normal, actualFlex );
	}
	
}
