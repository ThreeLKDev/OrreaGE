package threelkdev.orreaGE.core.loading;

import java.io.File;

import threelkdev.orreaGE.core.rendering.Model;
import threelkdev.orreaGE.core.rendering.ModelNode;
import threelkdev.orreaGE.core.rendering.ModelVertex;

public abstract class AModelLoader {
	
	/**
	 * Constructs and returns a {@link Model} from the given {@link File}.
	 * @param file - The {@link File} to load from.
	 * @return The constructed {@link Model}, or {@code null} if an error occurred.
	 */
	public abstract Model loadModel( File file );
	
	/**
	 * Constructs and returns a {@link ModelNode} from the given {@link File}.
	 * @param file - The {@link File} to load from.
	 * @param nodeName - The name of the {@link ModelNode} to load; if left blank or null, the first viable result should be returned.
	 * @return The constructed {@link ModelNode}, or {@code null} if an error occurred.
	 */
	public abstract ModelNode loadNode( File file, String nodeName );
	
	/**
	 * Constructs and returns an array of {@link ModelVertex} from the given {@link File}
	 * @param file - The {@link File} to load from.
	 * @param node - The {@link ModelNode} to load from; if left blank or null, will use the first viable {@link ModelNode} found.
	 * @param start - Offset from which to start loading the vertices.
	 * @param numVertices - Number of vertices to load. If this is less than one or greater than the number of vertices in the current node, then all vertices of the given node should be loaded; if no node was specified, then nodes will be ignored
	 * @return The array of {@link ModelVertex}, or {@code null} if an error occurred.
	 */
	public abstract ModelVertex[] loadVertices( File file, String node, int start, int numVertices );
	
	/** Convenience method, see {@link #loadVertices(File, String, int, int)} for full description. */
	public ModelVertex[] loadVertices( File file, int start, int numVertices ) { return loadVertices( file, null, start, numVertices ); }
	/** Convenience method, see {@link #loadVertices(File, String, int, int)} for full description. */
	public ModelVertex[] loadVertices( File file, String node, int numVertices ) { return loadVertices( file, node, 0, numVertices ); }
	/** Convenience method, see {@link #loadVertices(File, String, int, int)} for full description. */
	public ModelVertex[] loadVertices( File file, int numVertices ) { return loadVertices( file, 0, numVertices ); }
}
