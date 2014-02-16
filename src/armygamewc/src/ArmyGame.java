
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class ArmyGame extends JPanel implements Runnable {

	public final static int PREGAME = 0;
	public final static int DURINGGAME = 1;
	public final static int FINISHINGGAME = 2;
	public final static int PAUSED = 3;
	
	public final static int SQFX = 80;
	public final static int SQFY = 50;
	public final static int LAYOFFX = 10;
	public final static int LAYOFFY = 15;
	public final static int WIDTH = 815;
	public final static int HEIGHT = 630;
	
	public static int currentState = PREGAME;
	public static boolean gameRunning = false;
	public static JLayeredPane fieldPane;
	public static JLabel fieldLabel;
	public static ImageIcon field;
	public static Piece piece;
	public static JPanel sidePanel;
	
	public ArrayList<Piece> arrayOfPieces = new ArrayList<Piece>();
	
	public ArmyGame()
	{
		super();
		setLayout(new BorderLayout());
		field = new ImageIcon(getClass().getResource("Tabuleiro.png"));
		fieldLabel = new JLabel(field);
		fieldLabel.setVisible(true);
		fieldLabel.setBounds(LAYOFFX, LAYOFFY, field.getIconWidth(), field.getIconHeight());
		
		createPiecesBlue();
		createPiecesRed();
		
		
		sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(200 , 600));
		
		fieldPane = new JLayeredPane();
		fieldPane.setPreferredSize(new Dimension(815 , 630));
		fieldPane.setBorder(BorderFactory.createTitledBorder(
                "Combate Field"));

		fieldPane.add(fieldLabel , new Integer(1));
		addPiecesToPanel();
		
		add(fieldPane , BorderLayout.CENTER);
		add(sidePanel , BorderLayout.EAST);
	}
	
	public void createPiecesRed()
	{
		for(int i = 0 ; i < 20 ; i++)
		{
			if(i%20 >= 12)
				arrayOfPieces.add(new Piece(Piece.PATENTE.SOLDADO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.PATENTE.BOMBA , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.PATENTE.CABOARMEIRO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.PATENTE.SARGENTO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.TENENTE , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.CAPITAO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.PATENTE.MAJOR , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.PATENTE.CORONEL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.PATENTE.GENERAL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_general.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.MARECHAL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_marshal.png"))));
			}
		}
		
	}
	
	public void addPiecesToPanel()
	{
		Piece current;
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			current = arrayOfPieces.get(i);
			fieldPane.add(current.label , new Integer(2));
			fieldPane.add(current.details , new Integer(3));
		}
	}
	
	public void updatePieces()
	{
		Piece current;
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			current = arrayOfPieces.get(i);
			if(!current.live)
			{
				current.label.setVisible(true);
				current.update();
				
				current.label.setBounds(LAYOFFX + current.squaresx * SQFX +SQFX/2 - current.image.getIconWidth()/2, 
						LAYOFFY + current.squaresy*SQFY  + 2, current.image.getIconWidth(), current.image.getIconHeight());
				current.details.setBounds(LAYOFFX + current.squaresx * SQFX +SQFX/2 - current.details.getText().length()* 7/2, 
						LAYOFFY + current.squaresy*SQFY  + 2*SQFY/3 , current.details.getText().length()* 7 , current.image.getIconHeight()/3);
			}
		}
	}
	
	public void drawPieces()
	{
		Piece current;
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			current = arrayOfPieces.get(i);
			if(current.live)
				current.label.setVisible(true);
			else
				current.label.setVisible(false);
		}
	}
	
	public void createPiecesBlue()
	{
		for(int i = 0 ; i < 20 ; i++)
		{
			if(i%20 >= 12)
				arrayOfPieces.add(new Piece(Piece.PATENTE.SOLDADO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.PATENTE.BOMBA , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.PATENTE.CABOARMEIRO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.PATENTE.SARGENTO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.TENENTE , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.CAPITAO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.PATENTE.MAJOR , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.PATENTE.CORONEL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.PATENTE.GENERAL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_general.png"))));
				arrayOfPieces.add(new Piece(Piece.PATENTE.MARECHAL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_marshal.png"))));
			}
		}
	}
	
	@Override
	public void run()
	{
		while(gameRunning)
		{
			// update variables
			
			switch(currentState)
			{
				case PREGAME:
					break;
				case DURINGGAME:
					updatePieces();
					drawPieces();
					break;
				case FINISHINGGAME:
					break;
				case PAUSED:
					break;
				default:
						break;
			}
		}
		
	}
}
