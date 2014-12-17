import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DisplaySave extends JFrame
{
	JButton ok;
	JTextField fileTextField;
	JCheckBox newSheet;
	String fileName, filePath;
	
	DisplaySave(Pattern userPattern, Settings userSettings)
	{
		setLayout(new GridLayout(9, 1, 5, 5));
		
		newSheet = new JCheckBox("New Spreadsheet");
		
		JPanel blank = new JPanel();
		
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(9, 2, 5, 5));
		JLabel fileLabel = new JLabel("Filename: ");
		fileTextField = new JTextField(10);
		fileTextField.setEditable(false);
		JButton chooseFile = new JButton("Choose...");
		JPanel choosePanel = new JPanel();
		choosePanel.add(fileTextField);
		choosePanel.add(chooseFile);
		
		JLabel serialLabel = new JLabel ("Serial: ");
		JTextField serialTextField = new JTextField(10);
		serialTextField.setEditable(false);
		
		JLabel chokeLabel = new JLabel("Choke: ");
		JComboBox chokeBox = new JComboBox(new Object [] {"O3", "U3", "O2.5", "U2.5", "O2", "U2", "O1.5", "U1.5", "OSKT", "USKT", "O1", "U1"});
		
		JLabel shooterLabel = new JLabel("Shooter: ");
		JTextField shooterTextField = new JTextField(10);
		
		JLabel viewLabel = new JLabel("View: ");
		JTextField viewTextField = new JTextField(10);
		
		JLabel ammoLabel = new JLabel("Ammo: ");
		JTextField ammoTextField = new JTextField(10);
		
		JLabel distanceLabel = new JLabel("Distance: ");
		JTextField distanceTextField = new JTextField(10);
		
		ok = new JButton("OK");
		ok.setEnabled(false);
		JButton cancel = new JButton("Cancel");
		
		add(newSheet);
		add(blank);
		add(fileLabel);
		add(choosePanel);
		add(serialLabel);
		add(serialTextField);
		add(chokeLabel);
		add(chokeBox);
		add(shooterLabel);
		add(shooterTextField);
		add(viewLabel);
		add(viewTextField);
		add(ammoLabel);
		add(ammoTextField);
		add(distanceLabel);
		add(distanceTextField);
		add(ok);
		add(cancel);
		
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if new workbook
				if(newSheet.isSelected())
				{
					ShotgunSpreadsheet.newSpreadsheet(serialTextField.getText(), filePath, chokeBox.getSelectedIndex(), shooterTextField.getText(),
							viewTextField.getText(), ammoTextField.getText(), distanceTextField.getText(), userPattern, userSettings);
				}
				//if add to workbook
				else
				{
					ShotgunSpreadsheet.updateSpreadsheet(filePath, chokeBox.getSelectedIndex(), shooterTextField.getText(), viewTextField.getText(),
							ammoTextField.getText(), distanceTextField.getText(), userPattern, userSettings);
				}
				dispose();
			}
		});
		
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser();
		}});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
		}});
		
		newSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(newSheet.isSelected())
				{
					serialTextField.setEditable(true);
					ok.setEnabled(true);
				}
				else
				{
					serialTextField.setEditable(false);
				}
			}
		});
		
		pack();
		setTitle("Save");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void fileChooser()
	{
		JFileChooser chooser = new JFileChooser();
		if(newSheet.isSelected())
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = chooser.showOpenDialog(null);
		if(returnValue == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = chooser.getSelectedFile();
			fileName = selectedFile.getName();
			filePath = selectedFile.getAbsolutePath();
			fileTextField.setText(filePath);
			ok.setEnabled(true);
		}
	}
}
