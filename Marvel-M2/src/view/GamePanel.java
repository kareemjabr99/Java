package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import engine.Game;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.*;
import exceptions.*;

public class GamePanel extends JPanel implements ActionListener , KeyListener, ItemListener {
	private MainView mainView;
	
	private Direction d;
	
	private JPanel board;
	private JPanel playerOne;
	private JPanel playerTwo;
	private JPanel currentChampion;
	private JPanel actionButtons;
	private JPanel pQ;
	
	private JButton attackUp;
	private JButton attackDown;
	private JButton attackRight;
	private JButton attackLeft;
	
	
	private JButton moveUp;
	private JButton moveDown;
	private JButton moveRight;
	private JButton moveLeft;
	
	private JButton castAbility;
	private JButton castLeaderAbility;
//	private JButton inputAbility;
	private JButton endTurn;
	
	
	private JComboBox <String> playerOneChamps;
	private JComboBox <String> playerTwoChamps;
	private JComboBox <String> currentAbilities; 
	
	private JTextArea playerOneChampsAtt;
	private JTextArea playerTwoChampsAtt;
	private JTextArea playerOneLeaderAbility;
	private JTextArea playerTwoLeaderAbility;
	private JTextArea enterDirection;
	private JTextArea currentChampionAtt;
	private JTextArea champTurnOrder;
	
	private JLabel currChamp;
	private JLabel playerOneName;
	private JLabel playerTwoName;
	private JLabel xCorr;
	private JLabel yCorr;
	JLabel enterD;
	
	private String [] playerOneTeam;
	private String [] playerTwoTeam;
	private String [] currentChampionAbilities;
	
	private JTextField X;
	private JTextField Y;
	private JTextField direction;
	private JButton gridBtn;
	private JScrollPane pane;
	
	
	public GamePanel (MainView mainView) throws IOException {
		this.mainView = mainView;
		this.setLayout(null);
		this.addKeyListener(this);
		
		playerOneTeam = new String [3];
		
		for (int i = 0 ; i < mainView.game.getFirstPlayer().getTeam().size() ; i++) {
			playerOneTeam[i] = 	mainView.game.getFirstPlayer().getTeam().get(i).getName();
		}
		
		playerTwoTeam = new String [3];
		
		for (int i = 0 ; i < mainView.game.getSecondPlayer().getTeam().size() ; i++) {
			playerTwoTeam[i] = 	mainView.game.getSecondPlayer().getTeam().get(i).getName();
		}
		
		currentChampionAbilities = new String [3];
		
		for (int i = 0; i < mainView.game.getCurrentChampion().getAbilities().size(); i++) {
			currentChampionAbilities [i] = mainView.game.getCurrentChampion().getAbilities().get(i).getName();
		}
		
		
//		------------------------------------------------------------------------------------------------------------
		
		
		// Board Panel
		board = new JPanel ();
		board.setLayout(new GridLayout(5, 5));
		board.setBounds(250, 0, 900, 500);
		board.setBackground(Color.black);
		
		for (int i = 0 ; i < 5 ; i++) {
			for (int j = 0 ; j < 5 ; j++) {
				Damageable d=(Damageable) mainView.game.getBoard()[i][j];
				if (d != null) {
					if (d instanceof Champion) {
						String champName = ((Champion) d).getName();
						int hp = ((Champion)d).getCurrentHP();
						gridBtn = new JButton ();
						gridBtn.setText(champName);
						gridBtn.setToolTipText("Champion HP: " + hp);
						gridBtn.addActionListener(this);
						gridBtn.setFocusable(false);
						if(mainView.game.getFirstPlayer().getTeam().contains(d)) {
							gridBtn.setBackground(Color.blue);
							gridBtn.setOpaque(true);
							gridBtn.setBorderPainted(false);
						}
						else {
							gridBtn.setBackground(Color.red);
							gridBtn.setOpaque(true);
							gridBtn.setBorderPainted(false);
						}
						board.add(gridBtn);
					}
					else {
						JButton gridBtn = new JButton ();
						int hp = ((Cover)d).getCurrentHP();
						gridBtn.setText("Cover " + hp);
						gridBtn.addActionListener(this);
						gridBtn.setFocusable(false);
						//gridBtn.setBackground(Color.GRAY);
						//gridBtn.setOpaque(true);
						//gridBtn.setBorderPainted(false);
						board.add(gridBtn);
					}
				}
				
				else {
					JButton gridBtn = new JButton();
					gridBtn.addActionListener(this);
					gridBtn.setFocusable(false);
					//gridBtn.setBackground(Color.WHITE);
					//gridBtn.setOpaque(true);
					//gridBtn.setBorderPainted(false);
					board.add(gridBtn);
				}
			}
		}
		
		
		this.add(board);
		
		
		
//		------------------------------------------------------------------------------------------------------------
		
		// Player One Panel
		playerOne = new JPanel();
		playerOne.setLayout(null);
		playerOne.setBounds(0 , 0 , 250 , 500);
		playerOne.setBackground(new Color (10878976));
		
		
		
				// Player One Name
		playerOneName = new JLabel(mainView.game.getFirstPlayer().getName()+" TEAM");
		playerOneName.setFont(new Font ("Impact" , Font.BOLD ,22));
		playerOneName.setForeground(Color.GRAY);
		playerOneName.setBounds(10, 10, 230, 50);
		
		if (mainView.game.getFirstPlayer().getTeam().contains(mainView.game.getCurrentChampion())) {
			playerOneName.setForeground(Color.BLUE);
		}
		
		playerOne.add(playerOneName);
		
		
				// Player One Champs
		playerOneChamps = new JComboBox <String> (playerOneTeam);
		playerOneChamps.setToolTipText( mainView.game.getFirstPlayer().getName() + "'s Champions" );
		playerOneChamps.setBounds(10 , 60 , 230 , 50);
		playerOneChamps.addItemListener(this);
		playerOne.add(playerOneChamps);
		
		
		
				// Player Champ Attributes
		playerOneChampsAtt = new JTextArea();
		playerOneChampsAtt.setBounds(10 , 150 , 230 , 250);
		playerOneChampsAtt.setEditable(false);
		playerOneChampsAtt.setFont(new Font ("Rockwell" , Font.ITALIC , 16));
		playerOneChampsAtt.setOpaque(false);
		playerOneChampsAtt.setForeground(Color.WHITE);
		playerOne.add(playerOneChampsAtt);
		
				// Player Leader Ability
		playerOneLeaderAbility = new JTextArea();

		playerOneLeaderAbility.setEditable(false);
		playerOneLeaderAbility.setBounds(10 , 400 , 230 , 25);
		playerOneLeaderAbility.setFont(new Font ("Phosphate" , Font.BOLD , 20));
		playerOneLeaderAbility.setText("            Leader Ability");
		playerOneLeaderAbility.setBackground(new Color(2293504));
		
		playerOne.add(playerOneLeaderAbility);
		
		this.add(playerOne);
		
//		------------------------------------------------------------------------------------------------------------
		
		// Player Two Panel
		playerTwo = new JPanel();
		playerTwo.setLayout(null);
		playerTwo.setBounds(1150 , 0 , 250 , 500);
		playerTwo.setBackground(new Color (10878976));
				
				// Player Two Name
		playerTwoName = new JLabel(mainView.game.getSecondPlayer().getName()+"  TEAM");
		playerTwoName.setFont(new Font ("Impact" , Font.BOLD , 22));
		playerTwoName.setForeground(Color.GRAY);
		playerTwoName.setBounds(10 , 10 , 230 , 50);
		
		if (mainView.game.getSecondPlayer().getTeam().contains(mainView.game.getCurrentChampion()))
			playerTwoName.setForeground(Color.RED);
		
		playerTwo.add(playerTwoName);
		
				// Player Two Champs
		playerTwoChamps = new JComboBox <String> (playerTwoTeam);
		playerTwoChamps.setToolTipText(mainView.game.getSecondPlayer().getName() + "'s Champions");
		playerTwoChamps.setBounds(10 , 60 , 230 , 50);
		playerTwoChamps.addItemListener(this);
		playerTwo.add(playerTwoChamps);
		
				// Player Champ Attr
		playerTwoChampsAtt = new JTextArea();
		playerTwoChampsAtt.setBounds(10 , 150 , 230 , 250);
		playerTwoChampsAtt.setEditable(false);
		playerTwoChampsAtt.setFont(new Font ("Rockwell" , Font.ITALIC , 16));
		playerTwoChampsAtt.setOpaque(false);
		playerTwoChampsAtt.setForeground(Color.WHITE);
		playerTwo.add(playerTwoChampsAtt);
		
				// Player Two Leader Ability
		playerTwoLeaderAbility = new JTextArea();

		playerTwoLeaderAbility.setEditable(false);
		playerTwoLeaderAbility.setBounds(10 , 400 , 230 , 25);
		playerTwoLeaderAbility.setFont(new Font ("Phosphate" , Font.BOLD , 20));
		playerTwoLeaderAbility.setText("            Leader Ability");
		playerTwoLeaderAbility.setBackground(new Color(2293504));
		
		playerTwo.add(playerTwoLeaderAbility);
		
		
		this.add(playerTwo);
		
//		------------------------------------------------------------------------------------------------------------
		
		
		
		// Current Champion Panel
		currentChampion = new JPanel();
		currentChampion.setLayout(null);
		currentChampion.setBounds(0 , 500 , 250 , 300);
		currentChampion.setBackground(new Color (10878976));
		

		currentChampionAtt = new JTextArea();
		currentChampionAtt.setFont(new Font ("Impact" , Font.BOLD , 14));
		currentChampionAtt.setEditable(false);
		pane=new JScrollPane(currentChampionAtt);
		pane.setBounds( 20 , 55 , 210 , 195);
		currentChampion.add(pane);
		
		currChamp = new JLabel("Current Champion");
		currChamp.setFont(new Font ("Impact" , 0 , 20));
		currChamp.setForeground(Color.GRAY);
		currChamp.setBounds(50 , 0 , 150 , 50);
		currentChampion.add(currChamp);
		
		this.add(currentChampion);
		
//		------------------------------------------------------------------------------------------------------------
		
		// Action Buttons Panel
		actionButtons = new JPanel ();
		actionButtons.setLayout(null);
		actionButtons.setBounds(250, 500, 900, 300);
		actionButtons.setBackground(new Color (10878976));
//		actionButtons.setBackground(Color.CYAN);
		
		
				// Attack Button
		attackUp = new JButton();
		attackUp.addActionListener(this);
		attackUp.setText("Attack Up");
		attackUp.setBounds(10 , 50 , 150 , 50);
		actionButtons.add(attackUp);
		
		attackDown=new JButton();
		attackDown.addActionListener(this);
		attackDown.setText("Attack Down");
		attackDown.setBounds(10 , 100 , 150 , 50);
		actionButtons.add(attackDown);
		
		attackRight=new JButton();
		attackRight.addActionListener(this);
		attackRight.setText("Attack Right");
		attackRight.setBounds(10 , 150 , 150 , 50);
		actionButtons.add(attackRight);
		
		attackLeft=new JButton();
		attackLeft.addActionListener(this);
		attackLeft.setText("Attack Left");
		attackLeft.setBounds(10 , 200 , 150 , 50);
		actionButtons.add(attackLeft);
		
		
		
				// Move Button
		moveUp = new JButton();
		moveUp.addActionListener(this);
		moveUp.setText("Move Up");
		moveUp.setBounds(170 , 50 , 150 , 50);
		actionButtons.add(moveUp);
		
		moveDown = new JButton();
		moveDown.addActionListener(this);
		moveDown.setText("Move Down");
		moveDown.setBounds(170 , 100 , 150 , 50);
		actionButtons.add(moveDown);
		
		moveRight = new JButton();
		moveRight.addActionListener(this);
		moveRight.setText("Move Right");
		moveRight.setBounds(170 , 150 , 150 , 50);
		actionButtons.add(moveRight);
		
		moveLeft = new JButton();
		moveLeft.addActionListener(this);
		moveLeft.setText("Move Left");
		moveLeft.setBounds(170 , 200 , 150 , 50);
		actionButtons.add(moveLeft);
		
		
				// Cast Ability Button
		castAbility = new JButton();
		castAbility.addActionListener(this);
		castAbility.setText("Cast Ability");
		castAbility.setBounds(350 , 100 , 200 , 50);
		actionButtons.add(castAbility);
		
		castLeaderAbility = new JButton();
		castLeaderAbility.addActionListener(this);
		castLeaderAbility.setText("Leader Ability");
		castLeaderAbility.setBounds(350 , 200 , 200 , 50);
		actionButtons.add(castLeaderAbility);
		
		
		
				// Current Champion Abilities
		currentAbilities = new JComboBox <String> (currentChampionAbilities);
		currentAbilities.setToolTipText("Current Champion's Available Abilities");
		currentAbilities.setBounds(350 , 40 , 200 , 50);
		actionButtons.add(currentAbilities);
		
			//x and y
		X = new JTextField();
		X.setBounds(570, 70, 60, 50);
		actionButtons.add(X);
		xCorr = new JLabel("X-Coordinate:");
		xCorr.setBounds(570, 30, 90, 50);
		xCorr.setForeground(Color.WHITE);
		actionButtons.add(xCorr);
		
		Y = new JTextField();
		Y.setBounds(570, 150, 60, 50);
		actionButtons.add(Y);
		yCorr = new JLabel("Y-Coordinate:");
		yCorr.setBounds(570, 110, 90, 50);
		yCorr.setForeground(Color.WHITE);
		actionButtons.add(yCorr);
		
		direction = new JTextField("direction");
		direction.setBounds(700, 100, 100, 50);
		actionButtons.add(direction);
		enterD = new JLabel("Enter Direction:");
		enterD.setBounds(700, 50, 120, 50);
		enterD.setForeground(Color.WHITE);
		actionButtons.add(enterD);
		
		
		
		

		this.add(actionButtons);
		
		
//		------------------------------------------------------------------------------------------------------------
		
		// Priority Queue Panel
		pQ = new JPanel();
		pQ.setLayout(null);
		pQ.setBounds(1150 , 500 , 250 , 300);
		pQ.setBackground(new Color (10878976));
//		pQ.setBackground(Color.GREEN);
		
		
				// End Turn Button
		endTurn = new JButton();
		endTurn.addActionListener(this);
		endTurn.setText("End Turn");
		endTurn.setBounds(50 , 200 , 150 , 70);
		pQ.add(endTurn);
		
				// Turn Order
		champTurnOrder = new JTextArea("TURN ORDER:"+"\n"+ mainView.game.getTurnOrder().toString());
		champTurnOrder.setBounds(50 , 10 , 150 , 180);
		champTurnOrder.setFont(new Font ("Impact" , Font.BOLD , 18));
		champTurnOrder.setEditable(false);
		champTurnOrder.setOpaque(false);
		champTurnOrder.setForeground(Color.WHITE);
		pQ.add(champTurnOrder);
		
		this.add(pQ);
		
		
		updatecurrentInfo();
//		------------------------------------------------------------------------------------------------------------
		
		this.addKeyListener(this);
		
		playerOneLeaderAbility.addKeyListener(this);
		
		
		
	}

	public void updatecurrentInfo() {
		currentChampionAtt.setText(mainView.CurrChampInfo());
			
	}
	
//	public void currentChampionAbilitiesUpdate () {
//		for (int i = 0; i < mainView.game.getCurrentChampion().getAbilities().size(); i++) {
//			currentChampionAbilities [i] = mainView.game.getCurrentChampion().getAbilities().get(i).getName();
//		}
//	}
	
	public boolean checkPlayerFirstPlayer () {
		
		if (mainView.game.getFirstPlayer().getTeam().contains(mainView.game.getCurrentChampion()))
			return true;
		else
			return false;
	}
	
	public boolean checkPlayerSecondPlayer () {
		
		if (mainView.game.getSecondPlayer().getTeam().contains(mainView.game.getCurrentChampion()))
			return true;
		else
			return false;
	}
	
	public void updateTeams() {
		
		currentAbilities.removeAllItems();
		
		currentChampionAbilities = new String [3];
		
		for (int i = 0; i < mainView.game.getCurrentChampion().getAbilities().size(); i++) {
			currentChampionAbilities [i] = mainView.game.getCurrentChampion().getAbilities().get(i).getName();
		}
		
		for(int i=0;i<currentChampionAbilities.length;i++) {
			currentAbilities.addItem(currentChampionAbilities[i]);
		}
		this.validate();
		this.repaint();
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == playerOneChamps) {
			playerOneChampsAtt.setText(ChampInfoP1(playerOneChamps.getSelectedIndex()));
		}
		if(e.getSource() == playerTwoChamps) {
			playerTwoChampsAtt.setText(ChampInfoP2(playerTwoChamps.getSelectedIndex()));
		}
	}
	
	public String ChampInfoP1(int selectedIndex) {
		
		ArrayList<Champion> l =mainView.game.getFirstPlayer().getTeam();
		Champion curr = l.get(selectedIndex);
		String s = "Name: " + curr.getName() + "\n" + "MaxHp: " + curr.getMaxHP() + "\n" + " Attack Damage: " + curr.getAttackDamage() 
		+ "\n" + " Attack Range: " + curr.getAttackRange() + "\n" + " Mana: " + curr.getMana() + "\n" + 
		" Action Points:  " + curr.getMaxActionPointsPerTurn() + "\n" + " Speed" + curr.getSpeed() + "\n" +
		 " Abilities:" + "\n" + "1. " + curr.getAbilities().get(0).getName() + "\n" + "2. " + curr.getAbilities().get(1).getName() +
		 "\n" + "3. " + curr.getAbilities().get(2).getName();
		return s;
	}
	
	public String ChampInfoP2(int selectedIndex) {
		
		ArrayList<Champion> l =mainView.game.getSecondPlayer().getTeam();
		Champion curr = l.get(selectedIndex);
		String s = "Name: " + curr.getName() + "\n" + "MaxHp: " + curr.getMaxHP() + "\n" + " Attack Damage: " + curr.getAttackDamage() 
		+ "\n" + " Attack Range: " + curr.getAttackRange() + "\n" + " Mana: " + curr.getMana() + "\n" + 
		" Action Points:  " + curr.getMaxActionPointsPerTurn() + "\n" + " Speed" + curr.getSpeed() + "\n" +
		 " Abilities:" + "\n" + "1. " + curr.getAbilities().get(0).getName() + "\n" + "2. " + curr.getAbilities().get(1).getName() +
		 "\n" + "3. " + curr.getAbilities().get(2).getName();
		return s;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//ATTACKING
		if (e.getSource() == attackUp) {
			try {
				mainView.game.attack(Direction.DOWN);
				updates();
			} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		if (e.getSource() == attackDown) {
			try {
				mainView.game.attack(Direction.UP);
				updates();
			} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		if (e.getSource() == attackRight) {
			try {
				mainView.game.attack(Direction.RIGHT);
				updates();
			} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		if (e.getSource() == attackLeft) {
			try {
				mainView.game.attack(Direction.LEFT);
				updates();
			} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		
		//MOVING up
		if (e.getSource() == moveUp) {
			try {
				mainView.game.move(Direction.DOWN);
				updates();
			} catch (NotEnoughResourcesException | UnallowedMovementException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
			
		}
		//down
		if (e.getSource() == moveDown) {
			try {
				mainView.game.move(Direction.UP);
				updates();
			} catch (NotEnoughResourcesException | UnallowedMovementException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
			
		}
		if (e.getSource() == moveRight) {
			try {
				mainView.game.move(Direction.RIGHT);
				updates();
			} catch (NotEnoughResourcesException | UnallowedMovementException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
			
		}
		if (e.getSource() == moveLeft) {
			try {
				mainView.game.move(Direction.LEFT);
				updates();
			} catch (NotEnoughResourcesException | UnallowedMovementException | IOException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
			
		}
		//CASTING LEADER ABILITY
		
		if (e.getSource() == castLeaderAbility) {
			
			try {
				mainView.game.useLeaderAbility();
			} catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
			if (mainView.game.isFirstLeaderAbilityUsed())
				playerOneLeaderAbility.setBackground(Color.RED);
			
			if (mainView.game.isSecondLeaderAbilityUsed())
				playerTwoLeaderAbility.setBackground(Color.RED);
			
			try {
				updates();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		//CASTING
		if (e.getSource() == castAbility) {
			int abilityIndex = currentAbilities.getSelectedIndex();
			Ability a = mainView.game.getCurrentChampion().getAbilities().get(abilityIndex);
			
			try {//DIRECTIONAL
				if(a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
					if(direction.getText().equals("up")) {
						d=Direction.DOWN;
					}
					if(direction.getText().equals("down")) {
						d=Direction.UP;
					}
					if(direction.getText().equals("right")) {
						d=Direction.RIGHT;
					}
					if(direction.getText().equals("left")) {
						d=Direction.LEFT;
					}
					mainView.game.castAbility(a,d);
					System.out.println("hi");
				}//SINGLE TARGET X Y
				else if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
					if(X.getText().equals("") || Y.getText().equals("")) {
						JOptionPane.showMessageDialog(this, "Choose Target" , "Error" , JOptionPane.ERROR_MESSAGE);
					}
					else
						mainView.game.castAbility(a, Integer.parseInt(Y.getText()),Integer.parseInt(X.getText()));
				}
				else {//SURROUND,SELF,TEAM
					mainView.game.castAbility(a);
				}
			}
			catch(InvalidTargetException | NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException | NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage() , "Error" , JOptionPane.ERROR_MESSAGE);
			}
			try {
				updates();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	
		//END TURN
		if (e.getSource() == endTurn) {
			mainView.game.endTurn();
					
					// Changes player turn color
			if (checkPlayerFirstPlayer()) {
				playerOneName.setForeground(Color.BLUE);
				playerTwoName.setForeground(Color.GRAY);
			}
			else {
				playerTwoName.setForeground(Color.RED);
				playerOneName.setForeground(Color.GRAY);
			}
			
			
			
					// combo box current champion abilities update
			
			
					// updates turn order text
			champTurnOrder.setText("TURN ORDER: \n"+mainView.game.getTurnOrder().toString());
			
			try {
				updates();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		}
		try {
			updates();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void updates() throws IOException {//updates all
		updatecurrentInfo();
//		currentChampionAbilitiesUpdate ();
		updateBoard();
		updateFirstPlayerUsedAbility();
		updateSecondPlayerUsedAbility();
		updateTeams();
	}
	
	public void updateBoard() throws IOException {
		this.remove(board);
		board=new JPanel();
		board.setLayout(new GridLayout(5, 5));
		board.setBounds(250, 0, 900, 500);
		board.setBackground(Color.black);
		for (int i = 0 ; i < 5 ; i++) {
			for (int j = 0 ; j < 5 ; j++) {
				Damageable d=(Damageable) mainView.game.getBoard()[i][j];
				if (d != null) {
					if (d instanceof Champion) {
						String champName = ((Champion) d).getName();
						int hp = ((Champion)d).getCurrentHP();
						JButton gridBtn = new JButton ();
						gridBtn.setText(champName);
						gridBtn.setToolTipText("Champion HP: " + hp);
						gridBtn.addActionListener(this);
						gridBtn.setFocusable(false);
						board.add(gridBtn);
						if(mainView.game.getFirstPlayer().getTeam().contains(d)) {
							gridBtn.setBackground(Color.blue);
							gridBtn.setOpaque(true);
							gridBtn.setBorderPainted(false);
						}
						else {
							gridBtn.setBackground(Color.red);
							gridBtn.setOpaque(true);
							gridBtn.setBorderPainted(false);
						}
					}
					else {
						JButton gridBtn = new JButton ();
						int hp = ((Cover)d).getCurrentHP();
//						gridBtn.setText("" + hp);
						gridBtn.setText("Cover " + hp);
						gridBtn.addActionListener(this);
						gridBtn.setFocusable(false);
						board.add(gridBtn);
					}
				}
				
				else {
					JButton gridBtn = new JButton();
					gridBtn.addActionListener(this);
					gridBtn.setFocusable(false);
					board.add(gridBtn);
				}
			}
		}
		if(mainView.game.checkGameOver()!=null) {
			mainView.changeTOGameOverPanel(mainView.game.checkGameOver());
		}
		//updateTeams();
		this.add(board);
		this.revalidate();
		this.repaint();
	}

	public void updateFirstPlayerUsedAbility () {
		if (mainView.game.isFirstLeaderAbilityUsed()) 
			playerOneLeaderAbility.setBackground(new Color(16729156));
		
		else
			playerOneLeaderAbility.setBackground(new Color(2293504));
		
	}
	
	public void updateSecondPlayerUsedAbility () {
		if (mainView.game.isSecondLeaderAbilityUsed()) 
			playerTwoLeaderAbility.setBackground(new Color(16729156));
		
		else
			playerTwoLeaderAbility.setBackground(new Color(2293504));
	}
	
	public void keyTyped(KeyEvent e) {
//		switch(e.getKeyChar()) {
//		//case 'a':label.setLocation(label.getX()-10,label.getY());break;
//		//case 'w':label.setLocation(label.getX(),label.getY()-10);break;
////		case 's':label.setLocation(label.getX(),label.getY()+10);break;
////		case 'd':label.setLocation(label.getX()+10,label.getY());break;
//		
//		}	
	}
	
	public void keyPressed(KeyEvent e) {
//		switch(e.getKeyCode()) {
//		case 37:System.out.println("up");break;
//		case 38:System.out.println("down");break;
//		case 40:System.out.println("right");break;
//		case 39:System.out.println("left");break;
//		
//		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			d = Direction.UP;
			System.out.println("UP");
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			d = Direction.DOWN;
			System.out.println("DOWN");
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			d = Direction.LEFT;
			System.out.println("LEFT");
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			d = Direction.RIGHT;
			System.out.println("RIGHT");
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}

}
