

import java.awt.*;

import javax.swing.*;

public class ArmyGame extends JPanel {

	
	public final int WIDTH = 800;
	public final int HEIGHT = 500;
	
	private boolean gameRunning;
	private JLabel fieldLabel;
	
	public ArmyGame()
	{
		super();
		setGameRunning(false);
		fieldLabel = new JLabel("Connect to a server or start your own before playing");
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(fieldLabel);
		
	}



	public boolean isGameRunning() {
		return gameRunning;
	}



	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
}


