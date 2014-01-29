

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

import javax.swing.*;

public class ArmyGame {

	public final int WIDTH = 800;
	public final int HEIGHT = 400;
	
	private boolean gameRunning;
	private ServerSocket server ;
	private Socket client;
	private int counter = 1; // contador de conexões
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	private JFrame mainFrame;
	private JPanel menuPanel;
	private JPanel fieldPanel;
	private JPanel chatPanel;
	private JMenuBar menuBar;
	private JTextField chatTextField;
	private JTextArea chatTextArea;
	
	
	public ArmyGame()
	{
		gameRunning = false;
		
		mainFrame = new JFrame("Army Game");
		menuPanel = new JPanel();
		fieldPanel = new JPanel();
		chatPanel = new JPanel();
		menuBar = new JMenuBar();
		chatTextField = new JTextField();
		chatTextField.setEditable(false);
		chatTextField.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendData(e.getActionCommand());
				chatTextField.setText("");
			}
		});
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		
		mainFrame.setLayout(new BorderLayout());
		JLabel chatLabel = new JLabel("Chat should come here");
		JLabel fieldLabel = new JLabel("Game Field should come here");
		fieldPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		chatPanel.setPreferredSize(new Dimension (WIDTH, HEIGHT/3));
		chatPanel.setLayout(new BorderLayout());
		fieldPanel.add(fieldLabel);
		chatPanel.add(new JScrollPane(chatTextArea) , BorderLayout.CENTER);
		chatPanel.add(chatTextField , BorderLayout.NORTH);
		mainFrame.add(menuPanel, BorderLayout.NORTH);
		mainFrame.add(fieldPanel , BorderLayout.CENTER);
		mainFrame.add(chatPanel , BorderLayout.SOUTH);
		mainFrame.pack();
		//mainFrame.setSize(mainPanel.getSize());
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		JMenu menu = new JMenu("Game");
		JMenu startGame = new JMenu("Start Game");
		JMenuItem sMenuItem = new JMenuItem("as Server");
		JMenuItem cMenuItem = new JMenuItem("as Client");
		sMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				 System.out.println("Start as server: "+e.getActionCommand());
				 openSocket();
			}
		});
		cMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Start as client: "+e.getActionCommand());
				connectToSocket();
			}
		});
		
		startGame.add(sMenuItem);
		startGame.add(cMenuItem);
		menu.add(startGame);
		menuBar.add(menu);
		
		mainFrame.setJMenuBar(menuBar);
	}
	
	public void sendData(String message)
	{
		try
		{
			output.writeObject("SERVER >>> " + message);
			output.flush();
			displayMessage("\nSERVER >>> " + message);
		}
		catch(IOException ioException)
		{
			JOptionPane.showMessageDialog(mainFrame, "Error sending message");
		}
	}
	
	private void displayMessage(final String msgToDisplay) {
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						chatTextArea.append(msgToDisplay);
					}
				}
				);
	}

	public void openSocket()
	{
		
	}
	public void connectToSocket()
	{
		
	}
	
	public void runServer()
	{
		try
		{
			server = new ServerSocket(12345 , 100); // criar ServerSocket
			while(true)
			{
				try
				{
					waitForConnection();
					getStreams();
					processConnection();
				}
				catch(EOFException eofException)
				{
					displayMessage("\n Server terminated connection");
				}
				finally
				{
					closeConnection();
					++counter;
				}
			}
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	private void closeConnection() {
		displayMessage("\nTerminating connection\n");
		setTextFieldEditable(false);
		
		try
		{
			output.close();
			input.close();
			client.close();
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
		
	}

	private void setTextFieldEditable(final boolean b) 
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				chatTextField.setEditable(b);
			}
			
		});
	}

	private void processConnection() throws IOException
	{
		displayMessage("Waiting for connection\n");
		client = server.accept();
		displayMessage("Connection " + counter + "received from: " + client.getInetAddress().getHostName());
		
	}

	private void getStreams() throws IOException {
		output =  new ObjectOutputStream(client.getOutputStream());
		output.flush();
		input = new ObjectInputStream(client.getInputStream());
		
		
	}

	private void waitForConnection() throws IOException
	{
		displayMessage("Waiting for connection\n");
		client= server.accept();
		displayMessage("Connection " + counter + "received from: " + "connection.getInedAddress().getHostName()");
		
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
}


