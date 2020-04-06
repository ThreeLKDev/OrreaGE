package threelkdev.orreaGE.core.ui.text.loading;

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1. 
 * 
 * @author Karl
 *
 */
public class Character {
	
	private final int id;
	private final double xTextureCoord;
	private final double yTextureCoord;
	private final double xMaxTextureCoord;
	private final double yMaxTextureCoord;
	private final double xOffset;
	private final double yOffset;
	private final double sizeX;
	private final double sizeY;
	private final double xAdvance;
	
	/**
	 * @param id
	 *            - the ASCII value of the character.
	 * @param xTextureCoord
	 *            - the x texture coordinate for the top left corner of the
	 *            character in the texture atlas (value between 0-1).
	 * @param yTextureCoord
	 *            - the y texture coordinate for the top left corner of the
	 *            character in the texture atlas (value between 0-1).
	 * @param xTexSize
	 *            - the width of the character in the texture atlas (value between 0-1).
	 * @param yTexSize
	 *            - the height of the character in the texture atlas (value between 0-1).
	 * @param xOffset
	 *            - the x distance from the cursor to the left edge of the
	 *            character's quad in pixels.
	 * @param yOffset
	 *            - the y distance from the cursor to the top edge of the
	 *            character's quad in pixels.
	 * @param sizeX
	 *            - the width of the character's quad in pixels.
	 * @param sizeY
	 *            - the height of the character's quad in pixels.
	 * @param xAdvance - how far the cursor should advance after this character, in pixels.
	 */
	protected Character( int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize, 
			double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance ) {
		this.id = id;
		this.xTextureCoord = xTextureCoord;
		this.yTextureCoord = yTextureCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xMaxTextureCoord = xTexSize + xTextureCoord;
		this.yMaxTextureCoord = yTexSize + yTextureCoord;
		this.xAdvance = xAdvance;
	}
	
	protected int getId() { return id; }
	protected double getTexCoordX() { return xTextureCoord; }
	protected double getTexCoordY() { return yTextureCoord; }
	protected double getTexCoordMaxX() { return xMaxTextureCoord; }
	protected double getTexCoordMaxY() { return yMaxTextureCoord; }
	protected double getxOffset() { return xOffset; }
	protected double getyOffset() { return yOffset; }
	protected double getSizeX() { return sizeX; }
	protected double getSizeY() { return sizeY; }
	protected double getXAdvance() { return xAdvance; }
	
}
