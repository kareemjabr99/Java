package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WelcomePanel extends JPanel implements ActionListener{
	
	private MainView mainView;
	private JTextField name1;
	private JTextField name2;
	private JButton start;
	private JLabel image;
	private JPanel image1;
	private JPanel NamesPanel;
	
	
	public WelcomePanel(MainView mainView) {
		
		this.mainView = mainView;
		this.setLayout(new BorderLayout());
		
		
		
		NamesPanel = new JPanel();
		NamesPanel.setBackground(Color.WHITE);
		NamesPanel.setSize(500,500);
		NamesPanel.setLayout(new FlowLayout());
		
		JLabel firstName = new JLabel("First Player Name: ");
		NamesPanel.add(firstName);
		
		name1 = new JTextField("Enter Name");
		name1.setColumns(15);
		name1.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 22));
		NamesPanel.add(name1);
		
		JLabel secondName = new JLabel("Second Player Name: ");
		NamesPanel.add(secondName);
		
		name2 = new JTextField("Enter Name");
		name2.setColumns(15);
		name2.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 22));
		NamesPanel.add(name2);
	
		
		this.add(NamesPanel,BorderLayout.SOUTH);
		this.validate();
		
		start = new JButton("Start");
		start.setBounds(570, 600, 200, 50);
		start.addActionListener(this);
		NamesPanel.add(start);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == start){
			if (name1.getText().equals("") || name2.getText().equals("")){
				JOptionPane.showMessageDialog(this, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
			} 
			else{
				mainView.changeToSelectionOfChamps(name1.getText(), name2.getText());
			}
		}
	}

}

