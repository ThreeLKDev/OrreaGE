package threelkdev.orreaGE.core.ui.text;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiRenderBundle;
import threelkdev.orreaGE.tools.colours.Colour;

public class Text extends UiComponent {

	/** Height of a line of text at a font size of 1 */
	public static final int LINE_HEIGHT_PIXELS = 25;
	
	private final Font font;
	private final float fontSize;
	private final Alignment alignment;
	private final boolean scalable;
	
	private String textString;
	private Colour colour;
	
	private TextMesh mesh;
	
	private int lineCount;
	private float originalWidthPixels;
	
	protected Text( String text, Font font, float fontSize, Alignment alignment, Colour colour, boolean scalable ) {
		this.textString = text;
		this.font = font;
		this.fontSize = fontSize;
		this.scalable = scalable;
		this.alignment = alignment;
		this.colour = colour.duplicate();
		this.focusable = false;
		if( !scalable ) {
			super.setReloadOnSizeChange();
		}
	}
	
	public static TextBuilder newText( String text ) {
		return new TextBuilder( text );
	}
	
	public Text set( String text ) {
		if( !textString.equals( text ) ) {
			textString = text;
			onInit();
		}
		return this;
	}
	
	public Font getFont() { return font; }
	public Alignment getAlignment() { return alignment; }
	public float getFontSize() { return fontSize; }
	public String getString() { return textString; }
	public TextMesh getMesh() { return mesh; }
	public boolean isEmpty() { return textString.isEmpty(); }
	public int getLineCount() { return lineCount; }
	public Colour getColour() { return colour; }
	public int[] getClippingBounds() { return null;/** FIXME */ }
	
	public float getTextScale() {
		float scale = 1;
		if( scalable ) {
			scale = super.getPixelWidth() / originalWidthPixels;
		}
		return scale * super.getWidthMod();
	}
	
	public void setColour( Colour newCol ) { this.colour.setColour( newCol ); }
	
	@Override
	protected void getRenderData( UiRenderBundle renderData ) {
		renderData.addText( this );
	}
	
	@Override
	protected void onInit() {
		if( mesh != null )
			mesh.dispose();
		this.originalWidthPixels = super.getPixelWidth();
		this.mesh = font.initText( this,  originalWidthPixels );
		super.getHeightConstraint().setPixelValue( ( int ) ( LINE_HEIGHT_PIXELS * fontSize * mesh.getLineCount() ) );
		super.calculateScreenSpacePosition( true );
	}
	
	@Override
	protected void updateSelf() {}

	@Override
	protected void onReInit() {
		// TODO Auto-generated method stub
		
	}
	
}
