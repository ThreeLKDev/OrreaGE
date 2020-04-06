package threelkdev.orreaGE.worldgen;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.tools.utils.DataUtils;

public class TerrainVertex {
	
	protected static final int BYTES_PER_VERTEX = 20;
	
	private final Vector3f position;
	private final Vector3f normal;
	private final byte[] colour;
	private final boolean grass;
	
	public TerrainVertex( Vector3f pos, Vector3f normal, byte[] colour, boolean grass ) {
		this.position = pos;
		this.normal = normal;
		this.colour = colour;
		this.grass = grass;
	}
	
	public Vector3f getPosition() { return this.position; }
	public Vector3f getNormal() { return this.normal; }
	public byte[] getColour() { return colour; }
	
	protected void storeData( ByteBuffer buffer ) {
		buffer.putFloat( position.getX() );
		buffer.putFloat( position.getY() );
		buffer.putFloat( position.getZ() );
		int packedInt = DataUtils.pack_2_10_10_10_REV_int( normal.getX(), normal.getY(), normal.getZ(), 0 );
		buffer.putInt( packedInt );
		buffer.put( colour );
		buffer.put( ( byte ) ( grass ? 0 : DataUtils.BYTE_MAX ) );
	}
	
}
