import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class Chat extends JPanel implements Runnable
{
	public JTextField chatTextField;
	public JTextArea chatTextArea;
	public ObjectOutputStream output = null;
	public  StringBuffer toAppend = new StringBuffer("");
	public  StringBuffer toSend = new StringBuffer("");
	
	public static String toShow = "";
	
	public static boolean chatIsEnabled = false;
	
	
	public Chat()
	{
		super();
		chatTextField = new JTextField();
		chatTextField.setEditable(chatIsEnabled);
		
		chatTextField.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				String s = chatTextField.getText();
				
                if (!s.equals(""))
                {
                	appendToChatBox("VOCÊ: " + s + "\n");
                	chatTextField.selectAll();
                	// Send the string
                	sendString(s);
                }
				chatTextField.setText("");
			}
		});
		
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		chatTextArea.setAutoscrolls(true);
		setPreferredSize(new Dimension (800, 100));
		setLayout(new BorderLayout());
		
		add(new JScrollPane(chatTextArea) , BorderLayout.CENTER);
		add(chatTextField , BorderLayout.NORTH);
	}

	   // Acrescenta o texto ao chatbox
	   private void appendToChatBox(String s)
	   {
	         toAppend.append(s);
	   }

	   private  void sendString(String s)
	   {
	         toSend.append(s + "\n");
	   }
	
	public void setOutput(ObjectOutputStream newOutput)
	{
		output = newOutput;
	}

	public void displayMessage(String msgToDisplay) {
		chatTextArea.append(msgToDisplay);
		
	}
	
	public void update()
	{
		if(chatIsEnabled)
		{
			chatTextField.setEditable(true);
		}
		else
			chatTextField.setEditable(false);
		
		if(toShow.length() > 0)
		{
			chatTextArea.append("RIVAL: "+toShow);
		}
		toShow = "";
	}

	@Override
	public void run() {
		
		update();
		
	}

}
