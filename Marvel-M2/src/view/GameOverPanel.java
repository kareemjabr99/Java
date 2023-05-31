package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import engine.Player;

public class GameOverPanel  extends JPanel {
	
	private MainView mainView;
	JTextArea area;
	
	public GameOverPanel(MainView mainView, Player p) throws IOException {
		
		this.mainView = mainView;
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.setSize(new Dimension(1400 , 800));
		area = new JTextArea();
		area.setText(p.getName() + "\n Won!");
		area.setBackground(Color.BLACK);
		area.setForeground(Color.WHITE);
		area.setEditable(false);
		area.setFont(new Font ("Impact" , Font.BOLD , 75));
		area.setBounds(500 , 300 , 500 ,500);
		
		
		this.add(area);	
	}
	
}
