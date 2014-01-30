import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Main extends JFrame {

	private JPanel menuPanel;
	private ArmyGame game;
	private Chat 	chat;
	private JMenuBar menuBar;
	private ServerSide server;
	
	public Main()
	{
		super("Combate Game");
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setLayout(new BorderLayout());
				menuPanel = new JPanel();
				game = new ArmyGame();
				menuBar = new JMenuBar();
				chat = new Chat();
				
				add(menuPanel, BorderLayout.NORTH);
				add(game , BorderLayout.CENTER);
				add(chat , BorderLayout.SOUTH);
				pack();
				
				setResizable(false);
				setLocationRelativeTo(null);
				setVisible(true);
				
				JMenu menu = new JMenu("Game");
				final JMenu startGame = new JMenu("Start Game");
				JMenuItem sMenuItem = new JMenuItem("as Server");
				JMenuItem cMenuItem = new JMenuItem("as Client");
				final JMenuItem stopMenu = new JMenuItem("Stop");
				stopMenu.setEnabled(false);
				
				sMenuItem.addActionListener(new ActionListener() 
				{
					
					public void actionPerformed(ActionEvent e) {

						System.out.println("Start as server: "+e.getActionCommand());
						startGame.setEnabled(false);
						stopMenu.setEnabled(true);
					}
				});
				
				cMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.out.println("Start as client: "+e.getActionCommand());
						//connectToSocket();
					}
				});
				
				stopMenu.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.out.println("Connection attempt stopped");
						startGame.setEnabled(true);
						stopMenu.setEnabled(false);
					}
				});
				
				startGame.add(sMenuItem);
				startGame.add(cMenuItem);
				menu.add(startGame);
				menu.add(stopMenu);
				menuBar.add(menu);
				setJMenuBar(menuBar);
				setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		});
		
		
		
		
		
		
	}
	
	public static void main (String args[])
	{
		Main m = new Main();
		
		System.out.println("Main method is running");
		
	}
}