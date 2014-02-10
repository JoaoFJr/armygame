import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;


public class Chat extends JPanel
{
	public JTextField chatTextField;
	public JTextArea chatTextArea;
	private ObjectOutputStream output = null;
	
	public Chat()
	{
		super();
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				chatTextField = new JTextField();
				chatTextField.setEditable(false);
				chatTextField.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						if(output != null)
						{
							try
							{
								output.writeObject(e.getActionCommand());
								System.out.println("Saiu: "+ e.getActionCommand());
							}
							catch(IOException ioe)
							{
								ioe.printStackTrace();
							}
						//sendData(e.getActionCommand());
						}
						chatTextField.setText("");
					}
				});
				
				chatTextArea = new JTextArea();
				chatTextArea.setEditable(false);
				
				setPreferredSize(new Dimension (800, 100));
				setLayout(new BorderLayout());
				
				add(new JScrollPane(chatTextArea) , BorderLayout.CENTER);
				add(chatTextField , BorderLayout.NORTH);
			}
		});
		
		
	}
	
	public void setOutput(ObjectOutputStream newOutput)
	{
		output = newOutput;
	}

	public void displayMessage(String msgToDisplay) {
		chatTextArea.append(msgToDisplay);
		
	}
}
