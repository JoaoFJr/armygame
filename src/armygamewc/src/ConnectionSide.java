import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingWorker;


public class ConnectionSide implements Runnable
{
	public static String hostIP = "localhost";
	
	public final static int NULL = 0;
	public final static int DISCONNECTED = 1;
	public final static int DISCONNECTING = 2;
	public final static int BEGIN_CONNECT = 3;
	public final static int CONNECTED = 4;
	public static int connectionStatus = DISCONNECTED;
	public static final int PORT = 1800;
	
	public static boolean isHost = true;
	
	public static ServerSocket server ;
	public static Socket connection;
	public static int counter = 1; 
	public  ObjectOutputStream output;
	public static  ObjectInputStream input;
	
	public static Object inputObj = null;
	
	public static String message;
	public static String toShow;

	
	
	public ObjectOutputStream getOutput()
	{
		return output;
	}
	
	public void sendData(String message)
	{
		try
		{
			output.writeObject(message);
			output.flush();
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	
	public void runServer()
	{
		try{
				server = new ServerSocket(ConnectionSide.PORT, 1);
				
				try
				{
					connection = server.accept();
					getStreams();
					processConnection();
					connectionStatus = CONNECTED;
				}
				catch(EOFException eofException)
				{
					System.out.println("\n Server terminated connection");
				}
				finally
				{
					closeConnection();
					connectionStatus = DISCONNECTED;
					++counter;
				}
			}
			catch(IOException ioException)
			{
				System.out.println("\n Error");
				ioException.printStackTrace();
			}
	}
	
	public void runClient()
	{
		try
		{
			connection = new Socket(hostIP, ConnectionSide.PORT);
			try
			{
				getStreams();
				processConnection();
				connectionStatus = CONNECTED;
			}
			catch(EOFException eofException){
				System.out.println("Could not get IO streams");
			}
			finally
			{
				closeConnection();
				connectionStatus = DISCONNECTED;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("\n Error");
			ioe.printStackTrace();
			
		}
	}
	
		
		
	private void closeConnection()
	{
			//displayMessage("\nTerminating connection\n");
			
			try
			{
				output.close();
				input.close();
				connection.close();
			}
			
			catch(IOException ioException)
			{
				System.out.println("\n Error in Close Connection");
				//ioException.printStackTrace();
			}
			
	}
	
	

	private void processConnection() throws IOException
	{
		message=  "Connection Successful\n";
		sendData(message);
		
		do
		{
			try
			{
				message = input.readObject().toString();
				toShow = message;
				connectionStatus = CONNECTED;
			}
			catch(ClassNotFoundException cnfe)
			{
				
			}
			
		}while(!message.equals("CLIENT >>> TERMINATE"));
	}

	private void getStreams() throws IOException {
		output =  new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}

	private void waitForConnection() throws IOException
	{
		//displayMessage("Waiting for connection\n");
		//connection= server.accept();
		//displayMessage("Connection " + counter + "received from: " + "connection.getInedAddress().getHostName()");
	}
	public Socket doInBackground()
	{
		runServer();
		return connection;
		
	}
	
	
	public  void cleanConnection()
	{
	      try
	      {
	         if (server != null)
	         {
	            server.close();
	            server = null;
	         }
	      }
	      catch (IOException e)
	      { 
	    	  server = null;
	      }

	      try
	      {
	         if (connection != null)
	         {
	            connection.close();
	            connection = null;
	         }
	      }
	      catch (IOException e)
	      { 
	    	  connection = null;
	      }

	      try
	      {
	         if (input != null)
	         {
	            input.close();
	            input = null;
	         }
	      }
	      catch (IOException e)
	      { 
	    	  input = null;
	      }
	      try
	      {
		      if (output != null)
		      {
		         output.close();
		         output = null;
		      }
	      }
	      catch(IOException e)
	      {
	    	  output = null;
	      }
	}
	
	protected void done()
	{
		
	}

	@Override
	public void run() {
		switch (connectionStatus) {
	      case DISCONNECTED:
	         break;

	      case DISCONNECTING:
	         break;

	      case CONNECTED:
	         break;

	      case BEGIN_CONNECT:
	         break;
	      }

	      // Make sure that the button/text field states are consistent
	      // with the internal states
	      
	      //mainFrame.repaint();
	}
	

}
