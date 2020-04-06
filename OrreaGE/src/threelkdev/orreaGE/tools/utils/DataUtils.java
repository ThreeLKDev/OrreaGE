package threelkdev.orreaGE.tools.utils;

import threelkdev.orreaGE.tools.math.Maths;

/** Credit to ThinMatrix */

public class DataUtils {
	
	private static final int TEN_BITS_MAX = (int) (Math.pow(2, 10) - 1);
	private static final int TWO_BITS_MAX = (int) (Math.pow(2, 2) - 1);
	public static final int BYTE_MAX = (int) (Math.pow(2, 8) - 1);

	public static final int BYTES_IN_FLOAT = 4, BYTES_IN_INT = 4;

	/**
	 * @param x - Float between -1 and 1. Will get stored in 10 bits so will lose some precision.
	 * @param y - Float between -1 and 1. Will get stored in 10 bits so will lose some precision.
	 * @param z - Float between -1 and 1. Will get stored in 10 bits so will lose some precision.
	 * @param w - Float between 0 and 1. Will get stored in 2 bits so will end up being one of: 0, 0.333, 0.666, or 1.
	 * @return 4 byte of data (presented as an int) containing the 4 float inputs in the 2_10_10_10_Rev format.
	 */
	public static int pack_2_10_10_10_REV_int(float x, float y, float z, float w) {
		//FIXME need to check native byte order? (big/small endian)
		int val = 0;
		val = val | (quantizeNormalized(w, TWO_BITS_MAX, false) << 30);
		val = val | (quantizeNormalized(z, TEN_BITS_MAX, true) << 20);
		val = val | (quantizeNormalized(y, TEN_BITS_MAX, true) << 10);
		val = val | quantizeNormalized(x, TEN_BITS_MAX, true);
		return val;
	}

	public static int quantizeNormalized(float original, int highestLevel, boolean signed) {
		if (signed) {
			original = original * 0.5f + 0.5f;
		}
		return Math.round(original * highestLevel);
	}
	
	public static int quantize(float original, float max, int highestLevel, boolean signed){
		float minValue = signed ? -1 : 0;
		float normalized = Maths.clamp(original/max, minValue, 1);
		return quantizeNormalized(normalized, highestLevel, signed);
	}
	
	public static byte quantizeToByte(float original, float max, boolean signed){
		float minValue = signed ? -1 : 0;
		float normalized = Maths.clamp(original/max, minValue, 1);
		return quantizeNormalizedToByte(normalized, signed);
	}
	
	public static byte quantizeNormalizedToByte(float original, boolean signed){
		return (byte) quantizeNormalized(original, BYTE_MAX, signed);
	}

//	public static void storeDataInBuffer(FloatBuffer buffer, int vertexCount, float[]... data) {
//		float[] interleavedData = interleaveFloatData(vertexCount, data);
//		buffer.clear();
//		buffer.put(interleavedData);
//		buffer.flip();
//	}
//
//	public static float[] interleaveFloatData(int vertexCount, float[]... data) {
//		int totalSize = 0;
//		int[] lengths = new int[data.length];
//		for (int i = 0; i < data.length; i++) {
//			int elementLength = data[i].length / vertexCount;
//			lengths[i] = elementLength;
//			totalSize += data[i].length;
//		}
//		float[] interleavedBuffer = new float[totalSize];
//		int pointer = 0;
//		for (int i = 0; i < vertexCount; i++) {
//			for (int j = 0; j < data.length; j++) {
//				int elementLength = lengths[j];
//				for (int k = 0; k < elementLength; k++) {
//					interleavedBuffer[pointer++] = data[j][i * elementLength + k];
//				}
//			}
//		}
//		return interleavedBuffer;
//	}

}