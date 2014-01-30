import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class ServerSide
{
	private ServerSocket server ;
	private Socket client;
	private int counter = 1; 
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public ServerSide()
	{
		
		
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
			ioException.printStackTrace();
		}
	}
	
	public void runServer()
	{
		try{
				server = new ServerSocket(1800 , 2); // criar ServerSocket
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
		
	private void closeConnection()
	{
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
	
	private void displayMessage(final String msgToDisplay)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						//chatTextArea.append(msgToDisplay);
					}
				}
		);
	}
	
	private void setTextFieldEditable(final boolean b) 
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				//chatTextField.setEditable(b);
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

}
