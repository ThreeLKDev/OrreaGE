package threelkdev.orreaGE.core.ui.text.loading;

import java.util.List;

import org.lwjgl.opengl.GL11;

import threelkdev.orreaGE.core.rendering.openGL.objects.Attribute;
import threelkdev.orreaGE.core.rendering.openGL.objects.Vao;
import threelkdev.orreaGE.core.ui.text.Alignment;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.core.ui.text.TextMesh;
import threelkdev.orreaGE.tools.utils.VaoUtils;

public class TextMeshGenerator {
	
	private static final int VERTS_PER_CHAR = 6;
	private static final int FLOATS_PER_VERT = 4;
	
	private final float spaceWidth;
	
	private double cursorX;
	private double cursorY;
	private double extraSpace;
	private int pointer;
	
	public TextMeshGenerator( float spaceWidth ) {
		this.spaceWidth = spaceWidth;
	}
	
	public TextMesh generateTextMesh( Text text, List< Line > lines ) {
		int vertexCount = calcVertexCount( lines );
		float[] data = new float[ vertexCount * FLOATS_PER_VERT ];
		prepare();
		for( int i = 0; i < lines.size(); i++ ) {
			Line line = lines.get( i );
			applyTextPositioning( text, line, i == lines.size() - 1 );
			processLine( text, line, data );
			cursorY += Text.LINE_HEIGHT_PIXELS * text.getFontSize();
		}
		Vao vao = VaoUtils.createVao( data, new Attribute( 0, GL11.GL_FLOAT, 2 ), new Attribute( 1, GL11.GL_FLOAT, 2 ) );
		return new TextMesh( vao, vertexCount, lines.size() );
	}
	
	private void prepare() {
		this.cursorX = 0f;
		this.cursorY = 0f;
		pointer = 0;
	}
	
	private void processLine( Text text, Line line, float[] data ) {
		for( Word word : line.getWords() ) {
			processWord( word, text.getFontSize(), data );
			cursorX += spaceWidth * text.getFontSize() + extraSpace;
		}
	}
	
	private void processWord( Word word, float fontSize, float[] data ) {
		for( Character letter : word.getCharacters() ) {
			storeCharMeshData( letter, fontSize, data );
			cursorX += letter.getXAdvance() * fontSize;
		}
	}
	
	private void applyTextPositioning( Text text, Line line, boolean lastLine ) {
		this.cursorX = 0;
		if( text.getAlignment() == Alignment.CENTER ) {
			cursorX = ( line.getMaxPixelLength() - line.getPixelLength() ) / 2;
		} else if( text.getAlignment() == Alignment.RIGHT ) {
			cursorX = line.getMaxPixelLength() - line.getPixelLength();
		}
		if( text.getAlignment() == Alignment.JUSTIFY && !lastLine ) {
			this.extraSpace = ( line.getMaxPixelLength() - line.getPixelLength() ) / ( line.getWords().size() - 1 );
		} else {
			this.extraSpace = 0;
		}
	}
	
	private void storeCharMeshData( Character character, float fontSize, float[] data ) {
		double x = cursorX + character.getxOffset() * fontSize;
		double y = cursorY + character.getyOffset() * fontSize;
		double maxX = x + character.getSizeX() * fontSize;
		double maxY = y + character.getSizeY() * fontSize;
		storeVertexData( data, x, y, character.getTexCoordX(), character.getTexCoordY() );
		storeVertexData( data, x, maxY, character.getTexCoordX(), character.getTexCoordMaxY() );
		storeVertexData( data, maxX, maxY, character.getTexCoordMaxX(), character.getTexCoordMaxY() );
		storeVertexData( data, maxX, maxY, character.getTexCoordMaxX(), character.getTexCoordMaxY() );
		storeVertexData( data, maxX, y, character.getTexCoordMaxX(), character.getTexCoordY() );
		storeVertexData( data, x, y, character.getTexCoordX(), character.getTexCoordY() );
	}
	
	private int calcVertexCount( List< Line > lines ) {
		int charCount = 0;
		for( Line line : lines ) {
			charCount += line.getCharCount();
		}
		return charCount * VERTS_PER_CHAR;
	}
	
	private void storeVertexData( float[] data, double x, double y, double texX, double texY ) {
		data[ pointer++ ] = ( float ) x;
		data[ pointer++ ] = ( float ) y;
		data[ pointer++ ] = ( float ) texX;
		data[ pointer++ ] = ( float ) texY;
	}
	
}
