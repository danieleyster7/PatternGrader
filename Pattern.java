import java.util.ArrayList;
import org.opencv.highgui.*;
import org.opencv.core.*;

/**
* Pattern class to store and process properties of a shogun pattern
* Class is able to process an image of a shotgun pattern and isolate
* the hits from each pellet
* Class is able to find the point of impact, efficiency and effective pattern
* among other properties
*/
public class Pattern 
{
	//Arrays for x and y coodinates of hits
	private ArrayList<Integer> xCoordinates;
	private ArrayList<Integer> yCoordinates;
	//Number of registered hits
	private int numberOfHits;
	//x and y center of pattern (in pixels)
	private int xCenter, yCenter;
	private int shotSizeIndex;
	private String ounceString;
	//Number of pellets supposed to be in load (used for yield)
	private int numberOfPellets;
	//yield = numberOfHits/numberOfPellets
	private double yield;
	private double efficiency;
	private double effectivePattern;
	//Image matrices for processing
	private Mat inputImage, LUTMatTable, LUTImage;
	String filePrefix;
	//Pixels per inch
	double pixelsPerInch;

	
	//Used to find the 'absolute' index for ounceload. If index from 
	//MainMenu was used the index wouldn't be consistant
	private final String[] OUNCESTRINGS = {"1/2", "3/4", "7/8", "1", "1 1/8", "1 1/4"};

	/*
	might not use these
	static final int POINT_OF_IMPACT = 1;
	static final int EFFICIENCY = 2;
	static final int EFFECTIVE_PATTERN = 3;
	*/
	
	/**
	 * Constructor for passed shot size and ounce load
	 * @param shotSizeIndex
	 * @param ounceLoadIndex
	 */
	Pattern(int shotSizeIndex, String ounceString, int LUTscale, String filePrefix, String filePath) 
	{	
		xCoordinates = new ArrayList<Integer>();
		yCoordinates = new ArrayList<Integer>();
		LUTMatTable = new Mat(1, 256, CvType.CV_64F);
		numberOfHits = 0;
		this.shotSizeIndex = shotSizeIndex;
		this.ounceString = ounceString;
		this.filePrefix = filePrefix;
		//Set numOfPellets based on shotsize and ounce
		for(int i = 0; i < OUNCESTRINGS.length; i++)
		{
			if(OUNCESTRINGS[i] == ounceString)
			{
				//Get number of pellets
				numberOfPellets = ShotTable.getNumOfPellets(shotSizeIndex, i);
				break;
			}
		}
		//Load image into Pattern object (grayscale)
		loadImage(filePath);
		
		//Create the LUTImage of the input image (filtered to single out shot hits)
		createLUTImage(LUTscale);
		
		//Finds hits, condenses them to single pixels
		processImage();
		
		//Find the scale
		calcPixelsPerInch();
	}
	
	/**
	 * Constructor for passed number of pellets
	 * @param numberOfPellets
	 */
	Pattern(int numberOfPellets, int LUTscale, String filePrefix, String filePath) 
	{
		xCoordinates = new ArrayList<Integer>();
		yCoordinates = new ArrayList<Integer>();
		LUTMatTable = new Mat(1, 256, CvType.CV_32F);
		numberOfHits = 0;
		this.numberOfPellets = numberOfPellets;
		this.filePrefix = filePrefix;
		
		//Load image into Pattern object (grayscale)
		loadImage(filePath);
		
		//Create the LUTImage of the input image (filtered to single out shot hits)
		createLUTImage(LUTscale);
		
		//Finds hits, condenses them to single pixels
		processImage();
		
		//Find the scale
		calcPixelsPerInch();
	}
	
	int getNumberOfHits() {
		return numberOfHits;
	}
	
	/**
	 * Adds hit to x and y arrays, increments number of hits
	 * @param xCoordinate
	 * @param yCoordinate
	 */
	void addHit(int xCoordinate, int yCoordinate) 
	{
		xCoordinates.add(xCoordinate);
		yCoordinates.add(yCoordinate);
		numberOfHits++;
	}
	
	int returnXCoordinate(int location) {
		return xCoordinates.get(location);
	}
	
	int returnYCoordinate(int location) {
		return yCoordinates.get(location);
	}
	
	/**
	 * Find POI (center) of pattern
	 */
	void findCenter() 
	{
		int xSum = 0;
		int ySum = 0;
		
		for(int i = 0; i < xCoordinates.size(); i++) 
		{
			xSum += xCoordinates.get(i);
			ySum += yCoordinates.get(i);
		}
		if(numberOfHits != 0)
		{
			xCenter = xSum / numberOfHits;
			yCenter = ySum / numberOfHits;
		}
	}
	
	int getXCenter() {
		return xCenter;
	}
	
	int getYCenter() {
		return yCenter;
	}

	/**
	 * Load image specified in grayscale
	 * @param inputFileName
	 */
	void loadImage(String inputFileName) {
		inputImage = Highgui.imread(inputFileName, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
	}
	
	/**
	 * Create filtered image based on LUT value (cutoff)
	 * @param greyScaleCutOff LUT cutoff value
	 */
	void createLUTImage(int greyScaleCutOff) 
	{
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		for(int i = 0; i < greyScaleCutOff; i++)
			LUTMatTable.put(0,  i, 0);
		for(int i = greyScaleCutOff; i < 256; i++)
			LUTMatTable.put(0, i, 255);
		//Create mat object for LUTImage
		LUTImage = new Mat();
		//Save the LUTImage into LUTImage based on inputImage and LUTMatTable 
		Core.LUT(inputImage, LUTMatTable, LUTImage);
		//Save the file
		Highgui.imwrite(filePrefix + "LUTImage.png", LUTImage);
	}
	
	/**
	 * Sort through image
	 * When a dark spot (0) is found, call the condense method
	 */
	void processImage() 
	{
		double[] currentElement = new double[1];
		for(int rowIndex = 0; rowIndex < LUTImage.rows(); rowIndex++) 
		{
			for(int columnIndex = 0; columnIndex < LUTImage.cols(); columnIndex++) 
			{
				LUTImage.get(rowIndex, columnIndex, currentElement);
				if((int)currentElement[0] == 0) 
					condenseShotHit(rowIndex, columnIndex);
			}
		}
		Highgui.imwrite(filePrefix + "CondensedImage.png", LUTImage);
	}
	
	/**
	 * Condenses pixels of a hit
	 * Finds adjacent black pixels
	 * @param rowIndex row index where black pixel found
	 * @param columnIndex column index where black pixel found
	 */
	void condenseShotHit(int rowIndex, int columnIndex)
	{
		boolean firstRow = true;
		boolean evenRow = true;
		double[] currentElement = new double[1];
		ArrayList<Integer> xBlobCoordinates = new ArrayList<Integer>();
		ArrayList<Integer> yBlobCoordinates = new ArrayList<Integer>();
		int numberOfPixels = 0;
		int leftColumnEdge = columnIndex;
		int rightColumnEdge = columnIndex;
		
		do
		{
			//If first row, check to right
			if(firstRow)
			{
				//Check index of hit and to right
				for(int i = columnIndex; i < LUTImage.cols(); i++)
				{
					LUTImage.get(rowIndex, i, currentElement);
					if((int)currentElement[0] == 0)
					{
						xBlobCoordinates.add(i);
						yBlobCoordinates.add(rowIndex);
						numberOfPixels++;
						LUTImage.put(rowIndex, i, 255);
						if(i > columnIndex)
							rightColumnEdge++;
					}
					else
						break;
				}
				firstRow = false;
			}
			//If any other row
			else
			{
				//If 'even' row
				if(evenRow)
				{
					//Check index of hit and to the right
					for(int i = rightColumnEdge + 1; i < LUTImage.cols(); i++)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
							if(i > columnIndex)
								rightColumnEdge++;
						}
						else
							break;
					}
					//Check to the left to previous left bound
					for(int i = rightColumnEdge; i >= leftColumnEdge; i--)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
						}
					}
					//Check past previous left bound
					for(int i = leftColumnEdge - 1; i >= 0; i--)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
						}
						else
						{
							leftColumnEdge = xBlobCoordinates.get(numberOfPixels - 1);
							break;
						}
					}
				}
				//If 'odd' row
				else
				{
					//Check to left
					for(int i = leftColumnEdge; i >= 0; i--)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
							if(i < leftColumnEdge)
								leftColumnEdge--;
						}
						else
							break;
					}
					//Check to right up to previous right
					for(int i = leftColumnEdge + 1; i <= rightColumnEdge; i++)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
							if(i < leftColumnEdge)
								leftColumnEdge--;
						}
					}
					//Check past right bound
					for(int i = rightColumnEdge + 1; i < LUTImage.cols(); i++)
					{
						LUTImage.get(rowIndex, i, currentElement);
						if((int)currentElement[0] == 0)
						{
							xBlobCoordinates.add(i);
							yBlobCoordinates.add(rowIndex);
							numberOfPixels++;
							LUTImage.put(rowIndex, i, 255);
							if(i < leftColumnEdge)
								leftColumnEdge--;
						}
						else
						{
							rightColumnEdge = xBlobCoordinates.get(numberOfPixels - 1);
							break;
						}
					}
				}
				evenRow = !evenRow;
			}
			if(yBlobCoordinates.get(numberOfPixels - 1) != rowIndex)
				break;
			
			//Drop 'down' a row
			rowIndex++;
		}
		while(rowIndex < LUTImage.rows());
		//Finds the center of the blob of pixels found, puts the center back into the image
		findPelletLocation(xBlobCoordinates, yBlobCoordinates);
	}
	
	/**
	 * Finds center of a blob of pixels given
	 * @param xValues
	 * @param yValues
	 */
	void findPelletLocation(ArrayList<Integer> xValues, ArrayList<Integer> yValues)
	{
		int xTotal = 0;
		int yTotal = 0;
		
		for(int i = 0; i < xValues.size(); i++)
		{
			xTotal += (int)xValues.get(i);
			yTotal += (int)yValues.get(i);
		}
		
		//Put center of pattern back in
		//Add hit
		if(xValues.size() != 0)
		{
			int xAverage = xTotal / xValues.size();
			int yAverage = yTotal / yValues.size();
			addHit(xAverage, yAverage);
			LUTImage.put(yAverage, xAverage, 0);	
		}
		else
			System.out.println("Division by 0!!!!");
	}
	
	/**
	* Returns and sets yield
	*/
	void calcYield()
	{
		if(numberOfPellets != 0)
			yield = numberOfHits / numberOfPellets * 100;
		else
			yield = -1;
	}
	
	void calcEfficiency()
	{
		int hitsInsideRadius = 0;
		int effRadius = (int) (15 * pixelsPerInch);
		for(int i = 0; i < xCoordinates.size(); i++)
		{
			if(xCoordinates.get(i) <= (xCenter + effRadius) && xCoordinates.get(i) >= (xCenter - effRadius))
			{
				if(yCoordinates.get(i) <= (yCenter + effRadius) && yCoordinates.get(i) >= (yCenter - effRadius))
				{
					hitsInsideRadius++;
				}
			}
		}
		efficiency = hitsInsideRadius / numberOfPellets;
	}

	void calcEffectivePattern()
	{
		//calc
	}

	double getYield() {
		return yield;
	}
	
	double getEfficiency() {
		return efficiency;
	}

	double getEffectivePattern () {
		return effectivePattern;
	}
	
	void calcPixelsPerInch() {
		pixelsPerInch = LUTImage.width() / 48;
	}
	
	double getPixelsPerInch() {
		return pixelsPerInch;
	}
}


