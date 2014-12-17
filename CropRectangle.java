import java.awt.Color;
import java.awt.Graphics;

/**
* Class used to draw outline of image to be cropped
*/
public class CropRectangle 
{
	//Initial click points, used in instantiation
	int startX, startY;
	
	CropRectangle(int startX, int startY)
	{
		this.startX = startX;
		this.startY = startY;
	}
	
	public void setStartX(int startX) {
		this.startX = startX;
	}
	
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	//Draws rectangle regardless of starting point
	public void drawCropRect(Graphics g, int endX, int endY)
	{
		int leftX, upperY;
		if(startX < endX)
			leftX = startX;
		else
			leftX = endX;
		if(startY < endY)
			upperY = startY;
		else
			upperY = endY;

		g.setColor(Color.GREEN);
		g.drawRect(leftX, upperY, Math.abs(endX - startX), Math.abs(endY - startY));
	}
	
}
