package armygamewc;

import java.awt.*;

import javax.swing.*;

public class ArmyGame {

	public JFrame mainFrame;
	public JPanel mainPanel;
	public JPanel fieldPanel;
	public JPanel chatPanel;
	
	public ArmyGame()
	{
		mainFrame = new JFrame("Army Game");
		mainPanel = (JPanel) mainFrame.getContentPane();
		mainPanel.setPreferredSize(new Dimension(800,600));
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 800, 600);
		mainPanel.setIgnoreRepaint(true);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
}
