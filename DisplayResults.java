import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
* Class to display results of graded pattern
* Displays only results that were requested
*/
public class DisplayResults extends JFrame
{
	DisplayResults(Pattern userPattern, Settings userSettings)
	{
		setLayout(new GridLayout(5, 2, 5, 5));
		
		DecimalFormat numOfDec = new DecimalFormat("#.0");
		
		//Display POI
		if(userSettings.getPOI())
		{
			JLabel poi = new JLabel("POI: ");
			JTextField poiResult = new JTextField(12);
			String POIText = "";
			if(userPattern.getXCenter() < 0)
				POIText = POIText + "Left: " + numOfDec.format(userPattern.getXCenter() / userPattern.getPixelsPerInch()) + ", ";
			else
				POIText = POIText + "Right: " + numOfDec.format(userPattern.getXCenter() / userPattern.getPixelsPerInch()) + ", ";
			if(userPattern.getYCenter() < 0)
				POIText = POIText + "Under: " + numOfDec.format(userPattern.getYCenter() / userPattern.getPixelsPerInch());
			else
				POIText = POIText + "Over: " + numOfDec.format(userPattern.getYCenter() / userPattern.getPixelsPerInch());
			poiResult.setText(POIText);
			poiResult.setEditable(false);
			add(poi);
			add(poiResult);
		}
		//Display Efficiency
		if(userSettings.getEfficiency())
		{
			numOfDec = new DecimalFormat("#.00");
			JLabel efficiency = new JLabel("Efficiency: ");
			JTextField efficiencyResult = new JTextField(10);
			efficiencyResult.setText("" + numOfDec.format(userPattern.getEfficiency()) + "%");
			efficiencyResult.setEditable(false);
			add(efficiency);
			add(efficiencyResult);
		}
		//Display Effective Pattern
		if(userSettings.getEffectivePattern())
		{
			JLabel effectivePattern = new JLabel("Effective Pattern: ");
			JTextField effectiveResult = new JTextField(10);
			effectiveResult.setText("" + userPattern.getEffectivePattern());
			effectiveResult.setEditable(false);
			add(effectivePattern);
			add(effectiveResult);
		}
		//Display Yield
		if(userSettings.getYield())
		{
			JLabel yield = new JLabel("Yield: ");
			JTextField yieldResult = new JTextField(10);
			yieldResult.setText("" + userPattern.getYield() + "%");
			yieldResult.setEditable(false);
			add(yield);
			add(yieldResult);
		}

		JButton ok = new JButton("OK");
		ok.setToolTipText("Back to main menu.");
		JButton save = new JButton("Save");
		save.setToolTipText("Save results to excel file.");
		add(ok);
		add(save);
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DisplaySave(userPattern, userSettings);
				dispose();
			}
		});
		
		pack();
		setTitle("Results");
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
