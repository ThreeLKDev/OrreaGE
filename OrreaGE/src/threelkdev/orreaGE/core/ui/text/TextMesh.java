package threelkdev.orreaGE.core.ui.text;

import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.tools.utils.Disposable;

public class TextMesh implements Disposable {
	
	private final Vao vao;
	private final int vertexCount;
	private final int lineCount;
	
	public TextMesh( Vao vao, int vertexCount, int lineCount ) {
		this.vao = vao;
		this.vertexCount = vertexCount;
		this.lineCount = lineCount;
	}
	
	public Vao getVao() { return this.vao; }
	public int getVertexCount() { return this.vertexCount; }
	public int getLineCount() { return this.lineCount; }

	@Override
	public void dispose() {
		vao.dispose();
	}
	
}
