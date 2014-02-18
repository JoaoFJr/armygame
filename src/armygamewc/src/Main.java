import javax.swing.JFrame;
import javax.swing.JLabel;
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
import java.util.ArrayList;

//TODO layeredpane

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
	public static int previousState = ConnectionSide.connectionStatus;
	public static String updtgame;
	
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
	         SwingUtilities.invokeLater(game);
	         SwingUtilities.invokeLater(application);
		 }
	}
	@Override
	public void run() {
		
		// Lógica de funcionamento da aplicação, de acordo com o estado atual do jogo.
		switch(ConnectionSide.connectionStatus)
		{
			case ConnectionSide.DISCONNECTED:
				startGame.setEnabled(true);
				stopMenu.setEnabled(false);
				chat.chatTextField.setEditable(false);
				previousState = ConnectionSide.DISCONNECTED;
				break;
			case ConnectionSide.BEGIN_CONNECT:
				if(previousState != ConnectionSide.BEGIN_CONNECT)
				{
					if(ConnectionSide.isHost)
						chat.displayMessage("Aguardando conexão com o jogador cliente...\n");
					else
						chat.displayMessage("Procurando conexão com o servidor...\n");
				}
				startGame.setEnabled(false);
				stopMenu.setEnabled(true);
				chat.chatTextField.setEditable(false);
				previousState = ConnectionSide.BEGIN_CONNECT;
				break;
			case ConnectionSide.CONNECTED:
				if(previousState != ConnectionSide.CONNECTED)
				{
					chat.displayMessage("Conectado!\n---------------------------------\n");
					if(ConnectionSide.isHost)
					{
						game.createPiecesRed();
						game.createPiecesBlue();
						game.addPiecesToPanel();
						game.addPiecesToSidePanel();
						for(int i = 0 ; i < 12 ; i++)
						{
							game.your_pieces[i].setVisible(true);
							game.rival_pieces[i].setVisible(true);
						}
					}
					else
					{
						game.createPiecesBlue();
						game.createPiecesRed();
						game.addPiecesToPanel();
						game.addPiecesToSidePanel();
						for(int i = 0 ; i < 12 ; i++)
						{
							game.your_pieces[i].setVisible(true);
							game.rival_pieces[i].setVisible(true);
						}
					}
					
				}
				startGame.setEnabled(false);
				stopMenu.setEnabled(true);
				chat.chatTextField.setEditable(true);
				if (chat.toSend.length() != 0) {
					try
					{
					  connectionSide.output.writeObject((String)chat.toSend.toString());
	                  connectionSide.output.flush();
	                  chat.toSend.setLength(0);
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}
	            }
				if(ConnectionSide.toShow.length()!=0)
				{
					if(ConnectionSide.toShow.startsWith("command#"))
					{
						updateFromRival(ConnectionSide.toShow);
						ConnectionSide.toShow = "";
					}
					else if(ConnectionSide.toShow.startsWith("autogen#"))
					{
						autoPositionGame();
						ConnectionSide.toShow = "";
					}
					else
					{
						chat.chatTextArea.append("RIVAL: "+ConnectionSide.toShow);
						ConnectionSide.toShow = "";
					}
				}
				
				buildCommandMsg();
				if(game.updateRival)
				{
					game.updateRival = false;
					try 
					{
						connectionSide.output.writeObject(updtgame);
						connectionSide.output.flush();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
                
				
				// Atualizar o panel do jogo!
				//
				//
				//
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
		
		
		if(chat.toAppend.length() != 0)
			chat.chatTextArea.append(chat.toAppend.toString());
		chat.chatTextArea.setCaretPosition(chat.chatTextArea.getDocument().getLength());
		//System.out.println(chat.toAppend +"s");
		chat.toAppend.setLength(0);
		//application.repaint();
		
	}
	
	public void buildCommandMsg()
	{
		updtgame = "command#";
		updtgame = updtgame+sendIntArrayAsString(game.dead_pieces);
		updtgame = updtgame+"@";
		updtgame = updtgame+sendPieceArrayListAsString(game.arrayOfPieces);
		updtgame = updtgame+"\n";
	}
	
	// Envia apenas a parte que contém as peças deste jogador (metade do arraylist)
	public String sendPieceArrayListAsString(ArrayList<Piece> ap)
	{
		String s = "";
		for(int i = 0 ; i < ap.size()/2 ; i ++)
		{
			s=s+"("+ap.get(i).squaresx+","+ap.get(i).squaresy+",";
			if(ap.get(i).live)
				s = s+"1);";
			else
				s = s+"0);";
		}
		return s;
	}
	
	public String sendIntArrayAsString(int[] a)
	{
		String s = "";
		for (int i = 0 ; i < a.length ; i++)
		{
			s = s + a[i] + ',';
		}
		return s;
	}
	
	public void updateFromRival(String rcvd)
	{
		String[] str;
		String dpieces;
		String fpieces;
		str = rcvd.split("#"); // divide a string e pega a parte interessante (lado direito)
		dpieces = str[1];
		
		str = dpieces.split("@");// divide novamente
		dpieces = str[0];		// lado contendo dead_pieces
		dpieces = dpieces.replace("," , ""); // remove as virgulas para iniciar a atribuição
		for(int i = 0; i < game.r_dead_pieces.length ; i++)
		{
			game.r_dead_pieces[i] = Integer.parseInt(dpieces.substring(i , i+1));
		}
		fpieces = str[1];
		fpieces = fpieces.replace("\n" , "");
		str = fpieces.split(";");
		for(int i=0 ; i < str.length; i ++)
		{
			String[] substr;
			substr = str[i].replace("(", "").replace(")", "").split(",");
			game.arrayOfPieces.get(i+40).squaresx = 9 - Integer.parseInt(substr[0]);
			game.arrayOfPieces.get(i+40).squaresy = 9 - Integer.parseInt(substr[1]);
			if(Integer.parseInt(substr[2]) == 1 )
			{
				game.arrayOfPieces.get(i+40).live = true;
				game.arrayOfPieces.get(i+40).label.setVisible(true);
			}
			else
			{
				game.arrayOfPieces.get(i).live = false;
				game.arrayOfPieces.get(i).label.setVisible(false);
			}
		}
		
		
	}
	
	public void autoPositionGame()
	{
		for(int i = 0 ; i < game.arrayOfPieces.size()/2 ; i++)
		{
			game.arrayOfPieces.get(i).squaresx = i%10;
			game.arrayOfPieces.get(i).squaresy = i/10 + 6;
			game.arrayOfPieces.get(i).live = true;
			game.arrayOfPieces.get(i).label.setVisible(true);
			game.updateRival = true;
		}
		for(int i = 0 ; i < 12 ; i++)
		{
			game.dead_pieces[i] = 0;
		}
	}
}