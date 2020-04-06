package threelkdev.orreaGE.core.rendering.terrain;

import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.tools.colours.Colour;

public class TerrainMesh {
	
	private final int vertexCount;
	
	private Vao vao;
	private Colour grassColour = new Colour();
	
	private boolean loaded = false;
	
	public TerrainMesh( int vertexCount ) {
		this.vertexCount = vertexCount;
	}
	
	public void setMesh( Vao vao ) {
		this.vao = vao;
		this.loaded = true;
	}
	
	public Vao getVao() { return this.vao; }
	public int getVertexCount() { return this.vertexCount; }
	public boolean isLoaded() { return this.loaded; }
	public Colour getGrassColour() { return this.grassColour; }
	
	public void setGrassColour( Colour colour ) {
		this.grassColour.setColour( colour );
	}
	
}
