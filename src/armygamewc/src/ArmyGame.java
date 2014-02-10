

import java.awt.*;

import javax.swing.*;

public class ArmyGame extends JPanel implements Runnable {

	
	public final static int WIDTH = 800;
	public final static int HEIGHT = 500;
	
	
	public static boolean gameRunning = false;
	public static JLabel fieldLabel;
	
	public ArmyGame()
	{
		super();
		fieldLabel = new JLabel("Connect to a server or start your own before playing");
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(fieldLabel);
		
	}

	@Override
	public void run() {
		
		
	}
}


