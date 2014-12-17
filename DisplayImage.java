import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to display a frame with given image
 * @author Daniel
 */
public class DisplayImage extends JFrame
{	
	/**
	 * Constructor given name of image and display title
	 * @param inputImageName
	 * @param displayTitle
	 */
	DisplayImage(String inputImageName, String displayTitle) 
	{
		try 
		{
			File f = new File(inputImageName);
			BufferedImage b = ImageIO.read(f);
			ImageIcon inputImage = new ImageIcon(b);
			JLabel imageLabel = new JLabel(inputImage);
			add(imageLabel);
			pack();
			setTitle(displayTitle);
			setLocationRelativeTo(null);
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
}
