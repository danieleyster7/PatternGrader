import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class DisplayGradedPic extends JLabel
{
	Pattern userPattern;
	boolean displayPOI, displayEfficiency, displayEffPattern;
	
	ImageIcon inputImage;
	
	DisplayGradedPic(ImageIcon inputImage, Pattern userPattern, boolean displayPOI, boolean displayEfficiency, boolean displayEffPattern)
	{
		super(inputImage);
		this.inputImage = inputImage;
		this.userPattern = userPattern;
		this.displayPOI = displayPOI;
		this.displayEfficiency = displayEfficiency;
		this.displayEffPattern = displayEffPattern;
		
		JFrame gradedFrame = new JFrame();
		
		JButton ok = new JButton("Ok");
		ok.setToolTipText("Close window.");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gradedFrame.dispose();
			}
		});
		
		gradedFrame.add(this, BorderLayout.CENTER);
		gradedFrame.add(ok, BorderLayout.SOUTH);
		gradedFrame.setResizable(false);
		gradedFrame.pack();
		gradedFrame.setTitle("Graded");
		gradedFrame.setLocationRelativeTo(null);
		gradedFrame.setVisible(true);
		gradedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setIcon(inputImage);
		g.setColor(Color.CYAN);
		g.fillRect((userPattern.getXCenter() - 5) / 4, (userPattern.getYCenter() - 5) / 4, 10, 10);
		g.setColor(Color.MAGENTA);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.drawOval((int)(userPattern.getXCenter() - (15 * userPattern.getPixelsPerInch())) / 4, (int) (userPattern.getYCenter() - (15 * userPattern.getPixelsPerInch())) / 4, (int) (30 * userPattern.getPixelsPerInch()) / 4, (int) (30 * userPattern.getPixelsPerInch()) / 4);
	}
}
