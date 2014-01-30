import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Chat extends JPanel
{
	private JTextField chatTextField;
	private JTextArea chatTextArea;
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
						//sendData(e.getActionCommand());
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

}
