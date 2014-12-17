/**
 * Settings object to store settings
 * @author Daniel
 */
public class Settings implements java.io.Serializable
{
	//bools to decide whether or not something is checked
	private boolean efficiency;
	private boolean POI;
	private boolean effectivePattern;
	private boolean yield;
	private boolean allImages;
	private boolean drawPOI;
	private boolean drawEffPattern;
	private boolean debug;
	//LUT filter scale value
	private String LUTscale;
	
	Settings() 
	{
		efficiency = false;
		POI = false;
		effectivePattern = false;
		yield = false;
		allImages = false;
		drawPOI = false;
		drawEffPattern = false;
		LUTscale = "90";
	}
	
	/**
	 * Constructor for all parameters passed
	 * @param efficiency
	 * @param POI
	 * @param effectivePattern
	 * @param allImages
	 * @param drawPOI
	 * @param drawEffPattern
	 * @param LUTscale
	 */
	Settings(boolean efficiency, boolean POI, boolean effectivePattern, boolean yield, boolean allImages, boolean drawPOI, boolean drawEffPattern, boolean debug, String LUTscale) 
	{
		this.efficiency = efficiency;
		this.POI = POI;
		this.effectivePattern = effectivePattern;
		this.yield = yield;
		this.allImages = allImages;
		this.drawPOI = drawPOI;
		this.drawEffPattern = drawEffPattern;
		this.LUTscale = LUTscale;
		this.debug = debug;
		
	}
	
	/**
	 * Constructor for a passed settings object
	 * @param inputSettings
	 */
	Settings(Settings inputSettings) {
		this.efficiency = inputSettings.getEfficiency();
		this.POI = inputSettings.getPOI();
		this.effectivePattern = inputSettings.getEffectivePattern();
		this.yield = inputSettings.getYield();
		this.allImages = inputSettings.getAllImages();
		this.drawPOI = inputSettings.getDrawPOI();
		this.drawEffPattern = inputSettings.getDrawEffPattern();
		this.LUTscale = inputSettings.getLUTscale();
		this.debug = inputSettings.getDebug();
	}
	
	boolean getEfficiency() {
		return efficiency;
	}
	
	boolean getPOI() {
		return POI;
	}
	
	boolean getEffectivePattern() {
		return effectivePattern;
	}
	
	boolean getYield() {
		return yield;
	}
	
	boolean getAllImages() {
		return allImages;
	}
	
	boolean getDrawPOI() {
		return drawPOI;
	}
	
	boolean getDrawEffPattern() {
		return drawEffPattern;
	}
	
	String getLUTscale() {
		return LUTscale;
	}
	
	boolean getDebug() {
		return debug;
	}
	
	void setEfficiency(boolean efficiency) {
		this.efficiency = efficiency;
	}
	
	void setPOI(Boolean POI) {
		this.POI = POI;
	}
	
	void setEffectivePattern(boolean effectivePattern) {
		this.effectivePattern = effectivePattern;
	}
	
	void setYield(boolean yield) {
		this.yield = yield;
	}
	
	void setAllImages(boolean allImages) {
		this.allImages = allImages;
	}
	
	void setDrawPOI(boolean drawPOI) {
		this.drawPOI = drawPOI;
	}
	
	void setDrawEffPattern(boolean drawEffPattern) {
		this.drawEffPattern = drawEffPattern;
	}
	
	void setLUTscale(String LUTscale) {
		this.LUTscale = LUTscale;
	}
	
	void setDebug(boolean debug) {
		this.debug = debug;
	}
}
