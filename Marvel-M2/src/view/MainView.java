package view;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import engine.Game;
import engine.Player;
import model.abilities.Ability;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.Champion;

public class MainView extends JFrame{
	public Game game;
	private WelcomePanel welcomePanel;
	private String player1name;
	private String player2name;
	private ChampsSelectionPanel champsSelectionPanel;
	private GamePanel gamePanel;
	private GameOverPanel gameOverPanel;
	
	
	public MainView() {
		welcomePanel = new WelcomePanel(this);
		
		this.getContentPane().add(welcomePanel);
		this.setSize(1400,800);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.validate();
		this.addKeyListener(gamePanel);

	}

	public void changeToSelectionOfChamps(String firstName, String secondName) {
		player1name = firstName;
		player2name = secondName;
		try {
			Game.loadAbilities("Abilities.csv");
			Game.loadChampions("Champions.csv");
			this.remove(welcomePanel);
			
			champsSelectionPanel = new ChampsSelectionPanel(this);
			this.getContentPane().add(champsSelectionPanel);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		champsSelectionPanel.getFirstPlayer().setText(player1name + " Choose Your Players:");
		
		champsSelectionPanel.getSecondPlayer().setText(player2name + " Choose Your Players:");
		
	}
	
	public String [] availableChampions() {
		
		ArrayList<Champion> l = Game.getAvailableChampions();
		String [] r = new String[l.size()];
		for(int i = 0 ; i < l.size() ; i++) {
			r[i] = l.get(i).getName();
		}
		return r;
	}

	public String ChampInfo(int selectedIndex) {
		
		ArrayList<Champion> l = Game.getAvailableChampions();
		Champion curr = l.get(selectedIndex);
		String s = "\n" + "Name: " + curr.getName() + "\n" + "MaxHp: " + curr.getMaxHP() + "\n" + "Champion's Attack Damage: " + curr.getAttackDamage() 
				+ "\n" + "Champion's Attack Range: " + curr.getAttackRange() + "\n" + "Champion's Mana: " + curr.getMana() + "\n" + 
				"Champion's Action Points:  " + curr.getMaxActionPointsPerTurn() + "\n" + "Champion's Speed: " + curr.getSpeed() + "\n" +
				 "Champion's Abilities:" + "\n" + "1. " + curr.getAbilities().get(0).getName() + "\n" + "2. " + curr.getAbilities().get(1).getName() +
				 "\n" + "3. " + curr.getAbilities().get(2).getName();
		return s;
	}

	public void changeToGamePanel(int selectedIndex, int selectedIndex2, int selectedIndex3, int selectedIndex4,
			int selectedIndex5, int selectedIndex6, int leader1, int leader2) {
		this.remove(champsSelectionPanel);
		
		Player Player1 = new Player(player1name);
		Player1.getTeam().add(Game.getAvailableChampions().get(selectedIndex));
		Player1.getTeam().add(Game.getAvailableChampions().get(selectedIndex2));
		Player1.getTeam().add(Game.getAvailableChampions().get(selectedIndex3));
		Player1.setLeader(Game.getAvailableChampions().get(leader1));
		
		Player Player2 = new Player(player2name);
		Player2.getTeam().add(Game.getAvailableChampions().get(selectedIndex4));
		Player2.getTeam().add(Game.getAvailableChampions().get(selectedIndex5));
		Player2.getTeam().add(Game.getAvailableChampions().get(selectedIndex6));
		Player2.setLeader(Game.getAvailableChampions().get(leader2));
			
		game = new Game(Player1,Player2);
		
		try {
			gamePanel = new GamePanel(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getContentPane().add(gamePanel);
		
		this.validate();
		this.repaint();
	}
	
	public void changeTOGameOverPanel(Player p) throws IOException {
		
		this.remove(gamePanel);
		
		gameOverPanel=new GameOverPanel(this,p);
		
		this.getContentPane().add(gameOverPanel);
	
		this.validate();
		this.repaint();
	}
	
	public String CurrChampInfo() {
		Champion curr = (Champion) game.getCurrentChampion();
		String s = "Name: " + curr.getName() + "\n" + "MaxHp: " + curr.getMaxHP() + "\n" + "Champion's Attack Damage: " + curr.getAttackDamage() 
				+ "\n" + "Champion's Attack Range: " + curr.getAttackRange() + "\n" + "Champion's Mana: " + curr.getMana() + "\n" + 
				"Champion's Action Points:  " + curr.getMaxActionPointsPerTurn() + "\n" + "Current Action Point: " + curr.getCurrentActionPoints() + "\n" + "Champion's Speed" + curr.getSpeed() + "\n" +
				 "Champion's Abilities:" ;
		
		for (int i = 0; i < curr.getAbilities().size(); i++) {
			if(curr.getAbilities().get(i) instanceof CrowdControlAbility) {
				CrowdControlAbility cc = (CrowdControlAbility) curr.getAbilities().get(i);
				s = s + "\n" +  "1. " + cc.getName() + "\n" + " Type: CrowdControlAbility" +  "\n" + " Effect: " + cc.getEffect().getName();
			}
			else if(curr.getAbilities().get(i) instanceof DamagingAbility){
				DamagingAbility d = (DamagingAbility) curr.getAbilities().get(i);
				s = s + "\n" + "1. " + d.getName() + "\n" + " Type: DamagingAbility" +  "\n" + " Damage Amount: " + d.getDamageAmount();
			}
			else {
				HealingAbility h = (HealingAbility) curr.getAbilities().get(i);
				s = s + "\n" + "1. " + h.getName() + "\n" + " Type: HealingAbility" +  "\n" + " Heal Amount: " + h.getHealAmount();
			}
			Ability a = curr.getAbilities().get(i);
			s = s + "\n" + "  1) AreaOfEffect: " +  a.getCastArea()
				+ "\n" + "  2) Cast Range: "  + a.getCastRange()
				+ "\n" + "  3) Mana: " +  a.getManaCost()
				+ "\n" + "  4) Costs :" +  a.getRequiredActionPoints()
				+ "\n" + "  5) Current Cooldown: " + a.getCurrentCooldown()
				+ "\n" + "  6) Base Cooldown: " + a.getBaseCooldown();
		}
		for (int i = 0; i < curr.getAppliedEffects().size(); i++) {
			Effect e = curr.getAppliedEffects().get(i);
			s = s + "\n" + "Applied Effects: " + "\n" + " Name: " + e.getName() + "\n" + " Duration: " + e.getDuration()
				+ " Type: " + e.getType();
		}
		return s;
		
	}
	
	public static void main(String [] args) {
		new MainView();
	}
}


