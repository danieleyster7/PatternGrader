import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

/**
* Label class used to display image and crop the needed 'sub-image'
* Class creates a JFrame and adds itself and buttons
* Class uses CropRectangle to draw a rectangle where the user drags to outline crop area
* Ok is pressed to crop the image
*/
public class CropLabel extends JLabel implements MouseListener, MouseMotionListener
{
	int startX, startY, endX, endY, currentX, currentY;
	String fileName, folderPath;
	ImageIcon inputImage;
	CropRectangle cropRect;
	JButton ok;
	
	CropLabel(ImageIcon inputImage, String fileName, String folderPath)
	{
		super(inputImage);
		this.inputImage = inputImage;
		this.fileName = fileName;
		this.folderPath = folderPath;
		JFrame cropFrame = new JFrame();
		addMouseListener(this);
		addMouseMotionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ok = new JButton("Ok");
		ok.setToolTipText("Crop selected area.");
		ok.setEnabled(false);
		JButton cancel = new JButton ("Cancel");
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cropPic();
				cropFrame.dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cropFrame.dispose();
			}
		});
		
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		cropFrame.add(this, BorderLayout.CENTER);
		cropFrame.add(buttonPanel, BorderLayout.SOUTH);
		cropFrame.setResizable(false);
		cropFrame.pack();
		cropFrame.setTitle("Crop Image");
		cropFrame.setLocationRelativeTo(null);
		cropFrame.setVisible(true);
		cropFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void mousePressed(MouseEvent e) 
	{
		startX = e.getX();
		startY = e.getY();
		if(cropRect == null)
			cropRect = new CropRectangle(startX, startY);
		else
		{
			cropRect.setStartX(startX);
			cropRect.setStartY(startY);
		}
		//System.out.println("startX: " + startX + " startY: " + startY);
	}

	public void mouseReleased(MouseEvent e) 
	{
		endX = e.getX();
		endY = e.getY();
		ok.setEnabled(true);
		//System.out.println("endX: " + endX + " endY: " + endY);
	}
	
	public void mouseDragged(MouseEvent e) {
		currentX = e.getX();
		currentY = e.getY();
		this.repaint();
	}

		
	
	void cropPic()
	{
		Mat inputImage = Highgui.imread(fileName);
		System.out.println(fileName);
		//Modify for scaling factor
		int leftX, upperY;
		if(startX < endX)
			leftX = startX;
		else
			leftX = endX;
		if(startY < endY)
			upperY = startY;
		else
			upperY = endY;

		Rect cropRect = new Rect(startX * 4, startY * 4, Math.abs(endX - startX) * 4, Math.abs(endY - startY) * 4);
		int dims = inputImage.dims();
		System.out.println(dims);
		Mat croppedImage = new Mat(inputImage, cropRect);
		Highgui.imwrite(folderPath + "cropped.png", croppedImage);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setIcon(inputImage);
		//pack?
		if(cropRect != null)
			cropRect.drawCropRect(g, currentX, currentY);
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent arg0) {}
}
