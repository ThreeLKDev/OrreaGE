package threelkdev.orreaGE.core.loading;

import java.io.File;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.rendering.Model;
import threelkdev.orreaGE.core.rendering.ModelNode;
import threelkdev.orreaGE.core.rendering.ModelVertex;
import threelkdev.orreaGE.tools.files.CsvReader;

/**
 * ModelLoader for CSV files.<br />
 * Note that CSV are only used in basic testing, and as such this loader is rudimentary at best.
 *  Only the {@link #loadModel(File)} method is implemented, and this version assumes the data 
 *  was stored as follows:<br />
 * {@code ( int ) nodeCount }<br />
 * {@code ( string ) nodeName; ( int ) vertexCount; ( int ) indexCount }<br />
 * {@code ( float ) position1X; ( float ) position1Y; ( float ) position1Z; ( float ) normal1X; ( float ) normal1Y; ( float ) normal1Z; ...; ( float ) positionNX; ...; ( float ) normalNZ }<br />
 * {@code ( int ) index1; ( int ) index2; ... ; ( int ) indexN }<br />
 * {@code ... }<br />
 * @author Luca Kieran
 *
 */
public class CsvModelLoader extends AModelLoader {
	
	
	@Override
	public Model loadModel(File file) {
		CsvReader reader = tryGetReader( file );
		reader.nextLine();
		int nodeCount = reader.getNextInt();
		ModelNode[] nodes = new ModelNode[ nodeCount ];
		for( int i = 0; i < nodeCount; i++ ) {
			reader.nextLine();
			String name = reader.getNextString();
			int vertexCount = reader.getNextInt();
			int indexCount = reader.getNextInt();
			ModelVertex[] vertices = new ModelVertex[ vertexCount ];
			reader.nextLine();
			for( int j = 0; j < vertexCount; j++ ) {
				Vector3f position = reader.getNextVector3f();
				Vector3f normal = reader.getNextVector3f();
				Vector2f texCoords = new Vector2f( 0, 0 );
				vertices[ j ] = new ModelVertex( position, normal, texCoords );
			}
			int[] indices = new int[ indexCount ];
			reader.nextLine();
			for( int k = 0; k < indexCount; k++ ) {
				indices[ k ] = reader.getNextInt();
			}
			nodes[ i ] = new ModelNode( name, vertices, indices );
		}
		reader.close();
		return new Model( nodes );
	}

	@Override
	public ModelNode loadNode(File file, String nodeName) {
		System.out.println( "Attempted loadNode in CsvModelLoader; Function not supported. ");
		return null;
	}

	@Override
	public ModelVertex[] loadVertices(File file, String node, int start, int numVertices) {
		System.out.println( "Attempted loadVertices in CsvModelLoader; Function not supported. ");
		return null;
	}
	
	private CsvReader tryGetReader( File file ) {
		CsvReader reader = null;
		try {
			reader = CsvReader.open( file );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reader;
	}

}
