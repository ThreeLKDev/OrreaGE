package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.rendering.openGL.objects.IStaticBatch;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;

public class TestStaticBatch implements IStaticBatch {
	
	private Vao vao;
	private int vertexCount;
	
	public TestStaticBatch( Vao vao, int vertexCount ) {
		this.vertexCount = vertexCount;
		this.vao = vao;
	}
	
	@Override
	public boolean isVisible() { return true; }

	@Override
	public float getAlpha() { return 1f; }

	@Override
	public Vao getVao() { return vao; }

	@Override
	public int getVertexCount() { return vertexCount; }

	@Override
	public float getProgression() { return 1.0f; }

}
