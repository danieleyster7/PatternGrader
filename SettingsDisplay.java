import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
* Class to display settings menu
* Class saves the settings to a .dat file to be used
* Class uses the Settings object to read/write the settings file
*/
public class SettingsDisplay extends JFrame implements ActionListener
{
	private JCheckBox POI, efficiency, effectivePattern, yield;
	private JCheckBox debug, allImages, drawPOI, drawEffPattern;
	private JTextField LUTscaleField;
	private JButton OK, Cancel;
	
	SettingsDisplay()
	{
		setLayout(new GridLayout(6, 2, 5, 5));

		//Make checkboxes
		POI = new JCheckBox("POI");
		POI.setToolTipText("Calculate Point of Impact.");

		efficiency = new JCheckBox("Efficiency");
		efficiency.setToolTipText("Calculate Efficiency.");

		effectivePattern = new JCheckBox("Effective Pattern");
		effectivePattern.setToolTipText("Calculate Effective Pattern diameter.");

		yield = new JCheckBox("Yield");
		yield.setToolTipText("Calculate Yield.");

		//Change/get rid of
		debug = new JCheckBox("Debug");

		allImages = new JCheckBox("Show All Images");
		allImages.setToolTipText("Display images for each image process.");

		drawPOI = new JCheckBox("Draw POI");
		drawPOI.setToolTipText("Display POI on output image.");

		drawEffPattern = new JCheckBox("Draw Effective Pattern");
		drawEffPattern.setToolTipText("Display Effective Pattern on output image.");

		LUTscaleField = new JTextField(4);
		OK = new JButton("OK");
		Cancel = new JButton("Cancel");
		
		Settings userSettings = new Settings();
		//Try to load previous settings
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("settings.dat"));
			userSettings = (Settings)input.readObject();
			input.close();
		}
		catch(IOException io) {}
		catch(ClassNotFoundException c) 
		{
			c.printStackTrace();
			System.out.println("Settings object not found in dat file.");
		}
		
		//If item is true from settings, check box
		POI.setSelected(userSettings.getPOI());
		efficiency.setSelected(userSettings.getEfficiency());
		effectivePattern.setSelected(userSettings.getEffectivePattern());
		yield.setSelected(userSettings.getYield());
		allImages.setSelected(userSettings.getAllImages());
		drawPOI.setSelected(userSettings.getDrawPOI());
		drawEffPattern.setSelected(userSettings.getDrawEffPattern());
		debug.setSelected(userSettings.getDebug());
		
		LUTscaleField.setText(userSettings.getLUTscale());
		
		//Add listener
		OK.addActionListener(this);
		Cancel.addActionListener(this);
		
		//add checkboxes to frame
		add(POI);
		add(allImages);
		add(efficiency);
		add(drawPOI);
		add(effectivePattern);
		add(drawEffPattern);
		add(yield);
		add(debug);
		add(new JLabel("LUT Scale: "));
		add(LUTscaleField);
		add(OK);
		add(Cancel);
		
		pack();
		setTitle("Settings");
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Create a settings object based on checked selections
	 * and overwrite the old settings
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == Cancel)
			dispose();
		else
		{
			//Set settings based on current state
			Settings userSettings = new Settings();
			userSettings.setPOI(POI.isSelected());
			userSettings.setEfficiency(efficiency.isSelected());
			userSettings.setEffectivePattern(effectivePattern.isSelected());
			userSettings.setYield(yield.isSelected());
			userSettings.setAllImages(allImages.isSelected());
			userSettings.setDrawEffPattern(drawEffPattern.isSelected());
			userSettings.setDrawPOI(drawPOI.isSelected());
			userSettings.setDebug(debug.isSelected());
			userSettings.setLUTscale(LUTscaleField.getText());
			
			//Save the settings file
			try
			{
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("settings.dat"));
				output.writeObject(userSettings);
				output.close();
			}
			catch(IOException io) {
				io.printStackTrace();
			}
			dispose();
		}
		
	}
	
	public static void DisplaySettingsWindow() {
		new SettingsDisplay();
	}
}
