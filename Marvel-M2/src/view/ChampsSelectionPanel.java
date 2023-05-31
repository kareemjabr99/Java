package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class ChampsSelectionPanel extends JPanel implements ActionListener, ItemListener{
	
	private MainView mainView;
	
	private JLabel firstPlayer;
	private JLabel secondPlayer;
	private JComboBox<String>[] boxes;
	private JRadioButton [] r;
	private JTextArea infoArea;
	private JButton play;
	
	public ChampsSelectionPanel (MainView mainView) {
		this.mainView = mainView;
		this.setLayout(null);
		
		firstPlayer = new JLabel();
		firstPlayer.setBounds(100, 30, 190, 30);
		firstPlayer.setFont(new Font("Impact", Font.PLAIN, 22));
		firstPlayer.setSize(400, 50);
		this.add(firstPlayer);
		
		boxes = new JComboBox [6];
		
		boxes[0] = new JComboBox<String>(mainView.availableChampions());
		boxes[0].setBounds(150, 100, 250, 50);
		boxes[0].addItemListener(this);
		boxes[0].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[0]);
		
		boxes[1] = new JComboBox<String>(mainView.availableChampions());
		boxes[1].setBounds(150, 160, 250, 50);
		boxes[1].addItemListener(this);
		boxes[1].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[1]);
		
		boxes[2] = new JComboBox<String>(mainView.availableChampions());
		boxes[2].setBounds(150, 220, 250, 50);
		boxes[2].addItemListener(this);
		boxes[2].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[2]);
		
		secondPlayer = new JLabel();
		secondPlayer.setBounds(100, 320, 190, 30);
		secondPlayer.setSize(400, 50);
		secondPlayer.setFont(new Font("Impact", Font.PLAIN, 22));
		this.add(secondPlayer);
		
		boxes[3] = new JComboBox<String>(mainView.availableChampions());
		boxes[3].setBounds(150, 400, 250, 50);
		boxes[3].addItemListener(this);
		boxes[3].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[3]);
		
		boxes[4] = new JComboBox<String>(mainView.availableChampions());
		boxes[4].setBounds(150, 460, 250, 50);
		boxes[4].addItemListener(this);
		boxes[4].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[4]);
		
		boxes[5] = new JComboBox<String>(mainView.availableChampions());
		boxes[5].setBounds(150, 520, 250, 50);
		boxes[5].addItemListener(this);
		boxes[5].setFont(new Font ("Copperplate" , Font.ITALIC , 20));
		this.add(boxes[5]);
		
		r = new JRadioButton[6];
		
		r[0] = new JRadioButton("Set Leader");
		r[0].setBounds(450, 100, 190, 30);
		r[0].setSelected(true);
		r[0].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[0]);
		
		r[1] = new JRadioButton("Set Leader");
		r[1].setBounds(450, 160, 190, 30);
		r[1].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[1]);
		
		r[2] = new JRadioButton("Set Leader");
		r[2].setBounds(450, 220, 190, 30);
		r[2].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[2]);
		
		ButtonGroup rFirstPlayer = new ButtonGroup();
		rFirstPlayer.add(r[0]);
		rFirstPlayer.add(r[1]);
		rFirstPlayer.add(r[2]);
		
		r[3] = new JRadioButton("Set Leader");
		r[3].setBounds(450, 400, 190, 30);
		r[3].setSelected(true);
		r[3].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[3]);
		
		r[4] = new JRadioButton("Set Leader");
		r[4].setBounds(450, 460, 190, 30);
		r[4].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[4]);
		
		r[5] = new JRadioButton("Set Leader");
		r[5].setBounds(450, 520, 190, 30);
		r[5].setFont(new Font ("Krungthep" , Font.ITALIC , 16));
		this.add(r[5]);
		
		ButtonGroup rSecondPlayer = new ButtonGroup();
		rSecondPlayer.add(r[3]);
		rSecondPlayer.add(r[4]);
		rSecondPlayer.add(r[5]);
		
		infoArea = new JTextArea();
		infoArea.setBounds(700 , 200 , 300 , 400);
		infoArea.setEditable(false);
		infoArea.setFont(new Font ("Rockwell" , Font.ITALIC , 16));
		this.add(infoArea);
		
		play = new JButton("Play");
		
		play.setBounds(1110, 615, 120, 30);
		play.addActionListener(this);
		this.add(play);
		
		
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		for(int i = 0 ; i < boxes.length ; i++) {
			if(e.getSource() == boxes[i]) {
				infoArea.setText(mainView.ChampInfo(boxes[i].getSelectedIndex()));
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == play) {
			 boolean flag = false;
			for(int i = 0 ; i < boxes.length ; i++) {
				for(int j = i+1 ; j < boxes.length ; j++) {
					if(boxes[i].getSelectedIndex() == boxes[j].getSelectedIndex()) {
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				JOptionPane.showMessageDialog(this, "Champions must be different", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
			int Leader1 = 0;
			int Leader2 = 0;
			
			for(int i = 0 ; i < 3 ; i++) {
				if(r[i].isSelected()) {
					Leader1 = boxes[i].getSelectedIndex();
				}
			}
			for(int i = 3 ; i < 6; i++) {
				if(r[i].isSelected()) {
					Leader2 = boxes[i].getSelectedIndex();
				}
			}
		mainView.changeToGamePanel(boxes[0].getSelectedIndex(), boxes[1].getSelectedIndex() ,boxes[2].getSelectedIndex() 
				,boxes[3].getSelectedIndex(), boxes[4].getSelectedIndex(), boxes[5].getSelectedIndex(), Leader1 , Leader2);
		
		
	}

	public JLabel getFirstPlayer() {
		return firstPlayer;
	}
	
	public String getFirstPlayerName () {
		return firstPlayer.getText();
	}

	public JLabel getSecondPlayer() {
		return secondPlayer;
	}
	
	

}
