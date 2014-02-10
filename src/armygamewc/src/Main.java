import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class Main extends JFrame implements Runnable{

	public static JPanel menuPanel;
	public static JMenu menu;
	public static JMenuItem sMenuItem;
	public static JMenuItem cMenuItem;
	public static JMenuItem stopMenu;
	public static JMenu startGame;
	public static ArmyGame game;
	public static Chat 	chat;
	public static JMenuBar menuBar;
	public static ConnectionSide connectionSide = new ConnectionSide();
	public final static Main application = new Main();
	public static int previousState = connectionSide.connectionStatus;
	
	public Main()
	{
		super("Combate Game");
		initGui();
						
	}
	public void initGui()
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
		
		menu = new JMenu("Game");
		startGame = new JMenu("Start Game");
		sMenuItem = new JMenuItem("as Server");
		cMenuItem = new JMenuItem("as Client");
		stopMenu = new JMenuItem("Stop");
		stopMenu.setEnabled(false);
		
		sMenuItem.addActionListener(new ActionListener() 
		{
			
			public void actionPerformed(ActionEvent e) {
			//	connectionSide.runServer();
			//	chat.setOutput(connectionSide.getOutput());
			//	startGame.setEnabled(false);
			//	stopMenu.setEnabled(true);
				
				System.out.println("Iniciar como servidor: "+e.getActionCommand());
				ConnectionSide.connectionStatus = ConnectionSide.BEGIN_CONNECT;
				ConnectionSide.isHost = true;
				
				Runnable myRunnable = new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						connectionSide.runServer();
						
					}
				};
				Thread myThread = new Thread(myRunnable);
				myThread.start();
				
				//SwingUtilities.invokeLater(connectionSide);
				SwingUtilities.invokeLater(application);
				
			}
		});
		
		cMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Iniciar como cliente: "+e.getActionCommand());
				ConnectionSide.connectionStatus = ConnectionSide.BEGIN_CONNECT;
				ConnectionSide.isHost = false;
				Runnable myRunnable = new Runnable()
				{
					public void run()
					{
						connectionSide.runClient();
					}
				};
				Thread myThread = new Thread(myRunnable);
				myThread.start();
				SwingUtilities.invokeLater(connectionSide);
			}
		});
		
		stopMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Connection attempt stopped");
				ConnectionSide.connectionStatus = ConnectionSide.DISCONNECTING;
				connectionSide.cleanConnection();
				SwingUtilities.invokeLater(connectionSide);
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
	
	public static void main (String args[])
	{
		 while (true) {
	         try { // Poll every ~10 ms
	            Thread.sleep(10);
	         }
	         catch (InterruptedException e) {}
	         SwingUtilities.invokeLater(application);
		 }
	}
	@Override
	public void run() {
		switch(ConnectionSide.connectionStatus)
		{
			case ConnectionSide.DISCONNECTED:
				startGame.setEnabled(true);
				previousState = ConnectionSide.DISCONNECTED;
				break;
			case ConnectionSide.BEGIN_CONNECT:
				if(previousState != ConnectionSide.BEGIN_CONNECT)
				{
					if(connectionSide.isHost)
						chat.displayMessage("Aguardando conexão com o jogador cliente...\n");
					else
						chat.displayMessage("Procurando conexão com o servidor...\n");
				}
				startGame.setEnabled(false);
				stopMenu.setEnabled(true);
				previousState = ConnectionSide.BEGIN_CONNECT;
				break;
			case ConnectionSide.CONNECTED:
				if(previousState != ConnectionSide.CONNECTED)
					chat.displayMessage("Conectado!");
				startGame.setEnabled(false);
				stopMenu.setEnabled(true);
				chat.chatTextField.setEditable(true);
				
				previousState = ConnectionSide.CONNECTED;
				break;
			case ConnectionSide.DISCONNECTING:
				previousState = ConnectionSide.DISCONNECTING;
				break;
			case ConnectionSide.NULL:
				break;
			default:
				break;
		}
		application.repaint();
		
	}
}