package threelkdev.orreaGE.tools.colours;

public class HsvColour {
	
	private float hue, saturation, value;
	
	public HsvColour( float hue, float saturation, float value ) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}
	
	public float getHue() { return hue; }
	public float getSaturation() { return saturation; }
	public float getValue() { return value; }
	
	public void setHue( float hue ) { this.hue = hue; }
	public void setSaturation( float saturation ) { this.saturation = saturation; }
	public void setValue( float value ) { this.value = value; }
	
	public Colour converToRgb() {
		return Colour.hsvToRgb( hue,  saturation, value);
	}
	
}
