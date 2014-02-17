
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class ArmyGame extends JPanel implements Runnable {

	public final static int PREGAME = 0;
	public final static int DURINGGAME = 1;
	public final static int FINISHINGGAME = 2;
	public final static int PAUSED = 3;
	public final static int MOVING = 0;
	public final static int ATTACKING = 1;
	
	public final static int SQFX = 80;
	public final static int SQFY = 50;
	public final static int LAYOFFX = 10;
	public final static int LAYOFFY = 15;
	public final static int WIDTH = 815;
	public final static int HEIGHT = 630;
	
	public static int action = MOVING;
	public static int currentState = PREGAME;
	public static boolean gameRunning = false;
	public static JLayeredPane fieldPane;
	public static JLabel fieldLabel;
	public static ImageIcon field;
	public static Piece piece;
	
	public static ImageIcon up_icon;
	public static ImageIcon down_icon;
	public static ImageIcon right_icon;
	public static ImageIcon left_icon;
	public static JLabel up_lbl;
	public static JLabel down_lbl;
	public static JLabel right_lbl;
	public static JLabel left_lbl;
	
	public boolean updateRival = false;
	public static boolean myTurn = false;
	
	public static JLayeredPane sidePanel;
	
	public static ImageIcon sel_frame_img;
	public static JLabel sel_frame_lbl;
	
	public static int pickedPiece = -1;
	
	public ArrayList<Piece> arrayOfPieces = new ArrayList<Piece>();
	public JLabel[] your_pieces = new JLabel[12];
	public JLabel[] rival_pieces = new JLabel[12];
	public int dead_pieces[] = {1, 1 , 8 , 5 , 4 , 4 , 4 , 3 , 2 , 1 , 1 , 6};
	public int r_dead_pieces[] = {1, 1 , 8 , 5 , 4 , 4 , 4 , 3 , 2 , 1 , 1 , 6};
	public int all_pieces[] = {1, 1 , 8 , 5 , 4 , 4 , 4 , 3 , 2 , 1 , 1 , 6};
	
	public ArmyGame()
	{
		super();
		setLayout(new BorderLayout());
		create_fieldLabel();
		create_sidePanel();
		
		
		fieldPane = new JLayeredPane();
		fieldPane.setPreferredSize(new Dimension(815 , 630));
		fieldPane.setBorder(BorderFactory.createTitledBorder(
                "Combate Field"));

		fieldPane.add(fieldLabel , new Integer(1));
		
		add(fieldPane , BorderLayout.CENTER);
		addButtonsToFieldPane();
		
		add(sidePanel , BorderLayout.EAST);
	}
	
	public void addButtonsToFieldPane()
	{
		up_icon = new ImageIcon(getClass().getResource("uparrow.png"));
		down_icon = new ImageIcon(getClass().getResource("downarrow.png"));
		right_icon = new ImageIcon(getClass().getResource("rightarrow.png"));
		left_icon = new ImageIcon(getClass().getResource("leftarrow.png"));
		up_lbl = new JLabel(up_icon);
		down_lbl = new JLabel(down_icon);
		right_lbl = new JLabel(right_icon);
		left_lbl = new JLabel(left_icon);
		up_lbl.setBounds(200, 0 , up_icon.getIconWidth() , up_icon.getIconHeight());
		down_lbl.setBounds(200, 0 , down_icon.getIconWidth() , down_icon.getIconHeight());
		right_lbl.setBounds(200, 0 , right_icon.getIconWidth() , right_icon.getIconHeight());
		left_lbl.setBounds(200, 0 , left_icon.getIconWidth() , left_icon.getIconHeight());
		up_lbl.setVisible(true);
		down_lbl.setVisible(true);
		right_lbl.setVisible(true);
		left_lbl.setVisible(true);
		fieldPane.add(up_lbl , new Integer(4));
		fieldPane.add(down_lbl , new Integer(4));
		fieldPane.add(right_lbl , new Integer(4));
		fieldPane.add(left_lbl , new Integer(4));
	}
	
	public void create_fieldLabel()
	{
		field = new ImageIcon(getClass().getResource("Tabuleiro.png"));
		fieldLabel = new JLabel(field);
		fieldLabel.setVisible(true);
		fieldLabel.setBounds(LAYOFFX, LAYOFFY, field.getIconWidth(), field.getIconHeight());
		fieldLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(currentState == PREGAME)
				{
					if(pickedPiece >= 0 && dead_pieces[pickedPiece]>0)
					{
						
						int tab_x =  arg0.getX()/SQFX;
						int tab_y =  arg0.getY()/SQFY;
						if(tab_y >=6 &&  tabIsFree(tab_x , tab_y))
						{
							int index = getDesiredPiece(pickedPiece);
							arrayOfPieces.get(index).live = true;
							arrayOfPieces.get(index).squaresx = tab_x;
							arrayOfPieces.get(index).squaresy = tab_y;
							dead_pieces[pickedPiece] = dead_pieces[pickedPiece]-1;
							updateRival = true;
						}
						
					}
				}
			}
		});
	}
	
	public boolean tabIsFree(int tab_x , int tab_y)
	{
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			if((arrayOfPieces.get(i).squaresx == tab_x)&&(arrayOfPieces.get(i).squaresy == tab_y))
				return false;
			else if(((tab_x == 2)||(tab_x==3))&&((tab_y == 4)||(tab_y==5))) // lake positions
			{
				return false;
			}
		}
		return true;
	}
	
	public void create_sidePanel()
	{
		sidePanel = new JLayeredPane();
		sidePanel.setPreferredSize(new Dimension(200 , 600));
		sidePanel.setBorder(BorderFactory.createTitledBorder("Out of the game"));
		JLabel player_status = new JLabel("Yours: ");
		player_status.setBounds(10, 10, player_status.getText().length()*7, 20);
		sidePanel.add(player_status , new Integer(0));
		JLabel rival_status = new JLabel("Rival's: ");
		rival_status.setBounds(140, 10, player_status.getText().length()*7, 20);
		sidePanel.add(rival_status , new Integer(0));
		for(int i = 0 ; i < 12 ; i++)
		{
			your_pieces[i] = new JLabel(""+dead_pieces[i]+"/"+all_pieces[i]);
			your_pieces[i].setBounds(60, i *40 + 40, 100 , 20);
			your_pieces[i].setVisible(false);
			sidePanel.add(your_pieces[i] , new Integer(15));
			rival_pieces[i] = new JLabel(""+dead_pieces[i]+"/"+all_pieces[i]);
			rival_pieces[i].setBounds(160, i *40 + 40, 100 , 20);
			rival_pieces[i].setVisible(false);
			sidePanel.add(rival_pieces[i] , new Integer(15));
		}
		
		sel_frame_img = new ImageIcon(getClass().getResource("frame.png"));
		sel_frame_lbl = new JLabel(sel_frame_img);
		
		sel_frame_lbl.setVisible(false);
		
		sidePanel.add(sel_frame_lbl , new Integer(17));
	}
	
	public void createPiecesRed()
	{
		for(int i = 0 ; i < 20 ; i++)
		{
			if(i%20 >= 12)
				arrayOfPieces.add(new Piece(Piece.SOLDADO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.BOMBA , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.CABOARMEIRO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.SARGENTO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.TENENTE , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.CAPITAO , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.MAJOR , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.CORONEL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.GENERAL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_general.png"))));
				arrayOfPieces.add(new Piece(Piece.MARECHAL , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_marshal.png"))));
				arrayOfPieces.add(new Piece(Piece.BANDEIRA , Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_flag.png"))));
				arrayOfPieces.add(new Piece(Piece.ESPIAO, Piece.TEAM.RED ,new ImageIcon(getClass().getResource("red_spy.png"))));
			}
		}
		
	}
	
	public int getDesiredPiece(int pickedPiece)
	{
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			if((arrayOfPieces.get(i).id == pickedPiece)&&(!arrayOfPieces.get(i).live))
			{
				return i;
			}
		}
		return -1;
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
	
	public void addPiecesToSidePanel()
	{
		Piece current;
		boolean added[] = {false , false , false , false , false , 
				false , false, false , false , false , false , false};
		JLabel cLabel;
		JLabel patente;
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			current = arrayOfPieces.get(i);
			if(!added[current.id])
			{
				cLabel = new JLabel(current.image);
				cLabel.setVisible(true);
				cLabel.setBounds(10 , current.id*40 + 25, current.image.getIconWidth(), current.image.getIconHeight());
				sidePanel.add(cLabel , new Integer(current.id));
				cLabel.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if(currentState == PREGAME)
							sel_frame_lbl.setVisible(true);
						Rectangle r = (arg0.getComponent()).getBounds();
						sel_frame_lbl.setBounds(r.x-3, r.y-3, r.width+6, r.height+6);
						switch((r.y - 20)/40)
						{
							case Piece.BANDEIRA:
								pickedPiece = Piece.BANDEIRA;
								break;
							case Piece.ESPIAO:
								pickedPiece = Piece.ESPIAO;
								break;
							case Piece.SOLDADO:
								pickedPiece = Piece.SOLDADO;
								break;
							case Piece.CABOARMEIRO:
								pickedPiece = Piece.CABOARMEIRO;
								break;
							case Piece.SARGENTO:
								pickedPiece = Piece.SARGENTO;
								break;
							case Piece.TENENTE:
								pickedPiece = Piece.TENENTE;
								break;
							case Piece.CAPITAO:
								pickedPiece = Piece.CAPITAO;
								break;
							case Piece.MAJOR:
								pickedPiece = Piece.MAJOR;
								break;
							case Piece.CORONEL:
								pickedPiece = Piece.CORONEL;
								break;
							case Piece.GENERAL:
								pickedPiece = Piece.GENERAL;
								break;
							case Piece.MARECHAL:
								pickedPiece = Piece.MARECHAL;
								break;
							case Piece.BOMBA:
								pickedPiece = Piece.BOMBA;
							default:
								break;
						}
						
					}
				});
				
				patente = new JLabel(current.details.getText());
				patente.setVisible(true);
				patente.setBounds(60, current.id*40 + 20, patente.getText().length()*8, 20);
				sidePanel.add(patente , new Integer(current.id + 1));
				added[current.id] = true;
			}
		}
	}
	
	
	public void updatePieces()
	{
		Piece current;
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			current = arrayOfPieces.get(i);
			if(current.live)
			{
				current.label.setVisible(true);
				current.update();
				
				current.label.setBounds(LAYOFFX + current.squaresx * SQFX +SQFX/2 - current.image.getIconWidth()/2, 
						LAYOFFY + current.squaresy*SQFY  + 2, current.image.getIconWidth(), current.image.getIconHeight());
				current.details.setBounds(LAYOFFX + current.squaresx * SQFX +SQFX/2 - current.details.getText().length()* 7/2, 
						LAYOFFY + current.squaresy*SQFY  + 2*SQFY/3 , current.details.getText().length()* 7 , current.image.getIconHeight()/3);
				
				if(current.clickedOn)
				{
					current.clickedOn = false;
					current.selected = !current.selected;
					Piece.highlighted = current;
				}
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
				arrayOfPieces.add(new Piece(Piece.SOLDADO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.BOMBA , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.CABOARMEIRO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.SARGENTO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.TENENTE , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.CAPITAO , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.MAJOR , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.CORONEL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.GENERAL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_general.png"))));
				arrayOfPieces.add(new Piece(Piece.MARECHAL , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_marshal.png"))));
				arrayOfPieces.add(new Piece(Piece.BANDEIRA , Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_flag.png"))));
				arrayOfPieces.add(new Piece(Piece.ESPIAO, Piece.TEAM.BLUE ,new ImageIcon(getClass().getResource("blue_spy.png"))));
			}
		}
	}
	
	public void checkRemovedPieces()
	{
		Piece current;
		//checa se o usuário quer remover uma peça que já está no tabuleiro.
		for(int i = 0; i < arrayOfPieces.size()/2 ; i++)
		{
			current = arrayOfPieces.get(i);
			if(current.selected)
			{
				current.selected = false;
				current.live = false;
				current.squaresx = -1;
				current.squaresy= -1;
				dead_pieces[current.id] ++;
				updateRival = true;
			}
		}
	}
	
	
	@Override
	public void run()
	{
		
		// update variables
		
		switch(currentState)
		{
			case PREGAME:
				updatePieces();
				drawPieces();
				updateSideLabels();
				checkRemovedPieces();
				if(all_pieces_positioned())
				{
					currentState = DURINGGAME;
					JOptionPane.showMessageDialog(null, "Jogo Iniciado");
					sel_frame_lbl.setVisible(false);
					if(arrayOfPieces.get(0).team == Piece.TEAM.RED)
						myTurn = true;
				}
				break;
			case DURINGGAME:
				updatePieces();
				drawPieces();
				updateSideLabels();
				if(myTurn)
				{
					boolean[] bmovs = new boolean[4];
					if(Piece.highlighted != null)
					{
						switch(action)
						{
							case MOVING:
								bmovs = checkGameMoves();
								showButtons(bmovs);
								break;
							case ATTACKING:
							//	checkGameAttacks();
								break;
							default:
									break;
						}
					}
				}
				break;
			case FINISHINGGAME:
				break;
			case PAUSED:
				break;
			default:
					break;
		}
	}
	
	public void showButtons(boolean[] movements)
	{
		Piece p = Piece.highlighted;
		if(movements[0])
		{
			up_lbl.setBounds(p.squaresx * SQFX , p.squaresy * SQFY , up_icon.getIconWidth() , up_icon.getIconHeight());
		}
		if(movements[1])
		{
			down_lbl.setBounds(p.squaresx * SQFX , p.squaresy * SQFY , down_icon.getIconWidth() , down_icon.getIconHeight());
		}
		if(movements[2])
		{
			right_lbl.setBounds(p.squaresx * SQFX , p.squaresy * SQFY , right_icon.getIconWidth() , right_icon.getIconHeight());
		}
		if(movements[3])
		{
			left_lbl.setBounds(p.squaresx * SQFX , p.squaresy * SQFY , left_icon.getIconWidth() , left_icon.getIconHeight());
		}
	}
	
	public boolean[] checkGameMoves()
	{
		Piece current;
		boolean[] nearbyMoves;

		nearbyMoves = nearbyField(Piece.highlighted.squaresx , Piece.highlighted.squaresy);
		updateRival = true;
		return nearbyMoves;
			
	}
	
	public boolean all_pieces_positioned()
	{
		for(int i = 0 ; i < dead_pieces.length ; i++)
		{
			if(dead_pieces[i]>0 || r_dead_pieces[i]>0)
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean[] nearbyField(int x , int y)
	{
		boolean [] retorno = new boolean[4];
		
		if(y-1 >= 0)
			retorno[0] = tabIsFree(x, y - 1);
		else
			retorno[0] = false;
		if(y+1 < 10)
			retorno[1] = tabIsFree(x, y + 1);
		else
			retorno[1] = false;
		
		return retorno;
	}
	
	public void updateSideLabels()
	{
		for(int i = 0; i < 12 ; i++)
		{
			your_pieces[i].setText(""+dead_pieces[i]+"/"+all_pieces[i]);
			rival_pieces[i].setText(""+r_dead_pieces[i]+"/"+all_pieces[i]);
		}
	}
}
