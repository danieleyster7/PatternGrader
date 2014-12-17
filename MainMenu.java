
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import org.opencv.highgui.*;
import org.opencv.core.*;

/**
* Main class of shotgun pattern grader
* Class creates the main menu GUI
* class creates a Pattern object and calls the methods
* needed based on user input
*/
public class MainMenu extends JFrame implements ActionListener
{
	private JButton ok, help, exit, settings, chooseFile, cropImage;
	private JCheckBox POI, efficiency, effectivePattern;
	private JTextField inputFileName, shotCount;
	private JComboBox shotSize, ounceLoad;
	
	String fileName, filePath, folderPath;	
	Pattern userPattern;
	
	/**
	* No arg constructor
	* Creates main menu
	*/
	public MainMenu() 
	{	
		setLayout(new GridLayout(4, 1, 5, 5));
		
		//Set up 'north' panel with label and textfield
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		inputFileName = new JTextField(10);
		inputFileName.setEditable(false);
		chooseFile = new JButton("Choose...");
		chooseFile.setToolTipText("Click to browse for image file.");
		filePanel.add(new JLabel("Select File:"));
		filePanel.add(inputFileName);
		filePanel.add(chooseFile);
		add(filePanel);
			
		//Set up combo box panels with labels
		JPanel shotSizePanel = new JPanel();
		shotSizePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		shotSize = new JComboBox(new Object[] {" ", "2", "4", "5", "6", "7.5", "8", "8.5", "9"});
		shotSizePanel.add(new JLabel("Select: "));
		shotSizePanel.add(new JLabel("Shot Size"));
		shotSizePanel.add(shotSize);
		
		
		ounceLoad = new JComboBox(new Object [] {" "});
		shotSizePanel.add(new JLabel("Ounce Load"));
		shotSizePanel.add(ounceLoad);
		add(shotSizePanel);
		
		//Optional Shot count input
		JPanel shotCountPanel = new JPanel();
		shotCountPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		shotCount = new JTextField(5);
		shotCountPanel.add(new JLabel("Or Enter Shot Count: "));
		shotCountPanel.add(shotCount);
		add(shotCountPanel);
		
		//Set up 'south' panel with buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ok = new JButton("OK");
		ok.setEnabled(false);
		ok.setToolTipText("Click to grade selected image.");

		cropImage = new JButton("Crop Image");
		cropImage.setEnabled(false);
		cropImage.setToolTipText("Click to crop selected image.");

		exit = new JButton("Exit");
		exit.setToolTipText("Click to exit program.");
		help = new JButton("Help");
		help.setToolTipText("Click to open help window.");
		settings = new JButton("Settings");
		settings.setToolTipText("Click to open settings window.");

		buttonPanel.add(ok);
		buttonPanel.add(cropImage);
		buttonPanel.add(settings);
		buttonPanel.add(help);
		buttonPanel.add(exit);
		add(buttonPanel);
		
		//Set properties of menu frame
		pack();
		setTitle("Pattern Grader");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		

		//Listener for shotsize combo box to make ounce combobox
		shotSize.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e) 
			{
				setOunceComboBox(shotSize.getSelectedIndex());
				pack();
			}
		});
		
		//Add action listener
		ok.addActionListener(this);
		cropImage.addActionListener(this);
		settings.addActionListener(this);
		//help.addActionListener(this);
		exit.addActionListener(this);
		chooseFile.addActionListener(this);
	}
	
	/**
	* Action performed if a button is clicked
	* If ok hit, runs program
	* If settings hit, shows settings window
	* If help hit, shows help window
	* If choose hit, runs filechooser method
	* If exit hit, exits program
	*/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == ok)
		{
			if(shotCount.getText().equals(""))
				runProgram(false);
			else
				runProgram(true);
		}
		else if(e.getSource() == cropImage)
		{
			cropImage();
			filePath = folderPath + "cropped.png";
		}
		else if(e.getSource() == settings)
			SettingsDisplay.DisplaySettingsWindow();
		//else if(e.getSource() == help)
			//run help window
		else if(e.getSource() == chooseFile)
			fileChooser();
		else if(e.getSource() == exit)
			System.exit(0);	
	}
	
	/**
	* Method to choose input file
	* gets file name and path and sets them to 
	* class variables
	*/
	public void fileChooser()
	{
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(null);
		if(returnValue == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = chooser.getSelectedFile();
			fileName = selectedFile.getName();
			filePath = selectedFile.getAbsolutePath();
			inputFileName.setText(fileName);
			ok.setEnabled(true);
			cropImage.setEnabled(true);
			
			//Get prefix for filenames (file path minus the file extension)
			StringBuilder pictureName = new StringBuilder(filePath);
			int fileNameIndex = pictureName.indexOf(fileName);
			//Get just folderPath
			folderPath = pictureName.substring(0, fileNameIndex);
		}
	}

	public void cropImage()
	{
		try
		{
			File f = new File(filePath);
			BufferedImage b = ImageIO.read(f);
			BufferedImage scaled = scale(b, BufferedImage.TYPE_INT_RGB, b.getWidth() / 4, b.getHeight() / 4, .25, .25);
			ImageIcon inputImage = new ImageIcon(scaled);
			new CropLabel(inputImage, filePath, folderPath);
		}
		catch(IOException io) {
			io.printStackTrace();
		}
	}

	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
	
	/**
	* Method that handles the grading of the pattern
	* Method loads settings to decide what operations to use on image
	* Method creates Pattern object based on user input
	* Method finally creates a results display
	*/
	public void runProgram(boolean customShotCount) 
	{
		
		//Load settings to decide what to do
		Settings userSettings = new Settings();
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("settings.dat"));
			userSettings = (Settings)input.readObject();
			input.close();
		}
		catch(IOException io)
		{
			io.printStackTrace();
			System.out.println("Settings file not found.");
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
			System.out.println("Settings data not found in settings.dat");
		}

		//Get string from selected ounce in combobox
		String ounce = (String) ounceLoad.getSelectedItem();
		
		//Create pattern based on pellet selection
		if(customShotCount)
			userPattern = new Pattern(Integer.parseInt(shotCount.getText()), Integer.parseInt(userSettings.getLUTscale()), folderPath, filePath);
		else
			userPattern = new Pattern(shotSize.getSelectedIndex(), ounce, Integer.parseInt(userSettings.getLUTscale()), folderPath, filePath);
			
		
		/*
		//Load image into Pattern object (grayscale)
		userPattern.loadImage(filePath);
		
		//Create the LUTImage of the input image (filtered to single out shot hits)
		userPattern.createLUTImage(Integer.parseInt(userSettings.getLUTscale()));
		
		//Finds hits, condenses them to single pixels
		userPattern.processImage();
		
		//Find the scale
		userPattern.calcPixelsPerInch();
		*/
		//Display images for debugging
		if(userSettings.getDebug())
		{
			//Display LUTImage
			new DisplayImage(folderPath + "LUTImage.png", "LUTImage");

			//Display Condensed (aka processed) image
			new DisplayImage(folderPath + "CondensedImage.png", "Condensed");
		}
		
		//Based on choice, do stuff
		if(userSettings.getPOI())
			userPattern.findCenter();
		if(userSettings.getEfficiency())
			userPattern.calcEfficiency();
		if(userSettings.getEffectivePattern())
			userPattern.calcEffectivePattern();
		if(userSettings.getYield())
			userPattern.calcYield();

		//Display results
		new DisplayResults(userPattern, userSettings);
		//Display Pic
		try
		{
			File f = new File(filePath);
			BufferedImage b = ImageIO.read(f);
			BufferedImage scaled = scale(b, BufferedImage.TYPE_INT_RGB, b.getWidth() / 4, b.getHeight() / 4, .25, .25);
			ImageIcon inputImage = new ImageIcon(scaled);
			new DisplayGradedPic(inputImage, userPattern, userSettings.getPOI(), userSettings.getEfficiency(), userSettings.getEffectivePattern());
		}
		catch(IOException io) {
			io.printStackTrace();
		}
		
	}
	
	/**
	* Sets the ounce combo box's elements bases on the shot size selected
	*/
	public void setOunceComboBox(int shotSizeIndex)
	{	
		//Remove previous items if another shot size was selected previously
		ounceLoad.removeAllItems();

		switch(shotSizeIndex)
		{
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 8:
			ounceLoad.addItem("1/2");
			ounceLoad.addItem("3/4");
			ounceLoad.addItem("7/8");
			ounceLoad.addItem("1");
			ounceLoad.addItem("1 1/8");
			ounceLoad.addItem("1 1/4");
			break;
		case 7:
			ounceLoad.addItem("1/2");
			ounceLoad.addItem("3/4");
			ounceLoad.addItem("7/8");
			ounceLoad.addItem("1");
			ounceLoad.addItem("1 1/8");
			break;
		case 1:
			ounceLoad.addItem("1");
			ounceLoad.addItem("1 1/8");
			ounceLoad.addItem("1 1/4");
			break;
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java249");
		new MainMenu();
	}

}
