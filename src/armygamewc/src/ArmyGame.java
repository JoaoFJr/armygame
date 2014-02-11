
import java.awt.*;

import javax.swing.*;

public class ArmyGame extends JPanel implements Runnable {

	public final static int LAYOFFX = 10;
	public final static int LAYOFFY = 15;
	public final static int WIDTH = 815;
	public final static int HEIGHT = 630;
	public static boolean gameRunning = false;
	public static JLayeredPane fieldPane;
	public static JLabel fieldLabel;
	public static ImageIcon field;
	public static JPanel sidePanel;
	
	public ArmyGame()
	{
		super();
		setLayout(new BorderLayout());
		field = new ImageIcon(getClass().getResource("Tabuleiro.png"));
		
		fieldLabel = new JLabel(field);
		fieldLabel.setVisible(true);
		fieldLabel.setBounds(LAYOFFX, LAYOFFY, field.getIconWidth(), field.getIconHeight());
		sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(200 , 600));
		
		fieldPane = new JLayeredPane();
		fieldPane.setPreferredSize(new Dimension(815 , 630));
		fieldPane.setBorder(BorderFactory.createTitledBorder(
                "Combate Field"));

		fieldPane.add(fieldLabel , new Integer(1));
		add(fieldPane , BorderLayout.CENTER);
		add(sidePanel , BorderLayout.EAST);
	}
	
	@Override
	public void run()
	{
	}
}


