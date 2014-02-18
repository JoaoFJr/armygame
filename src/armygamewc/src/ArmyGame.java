
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
	public static int previousState = PREGAME;
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
	
	public int[] attackInfo = new int[6];
	public boolean updateRival = false;
	public boolean attackedRival = false;
	public boolean attackedByRival = false;
	public static boolean myTurn = false;
	public static boolean finishedTurn = false;
	
	public static JLayeredPane sidePanel;
	
	public static ImageIcon sel_frame_img;
	public static JLabel sel_frame_lbl;
	
	public static int pickedPiece = -1;
	public static String updtgame = "";
	
	public static Piece lastMovedPiece = null;
	
	public static String attackMsg = "";
	
	public static ImageIcon atk_icon;
	public static JLabel up_atk_lbl;
	public static JLabel down_atk_lbl;
	public static JLabel right_atk_lbl;
	public static JLabel left_atk_lbl;
	
	public static ImageIcon rival_blue_icon;
	public static ImageIcon rival_red_icon;
	
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
		
		rival_blue_icon = new ImageIcon(getClass().getResource("blue_blank.png"));
		rival_red_icon = new ImageIcon(getClass().getResource("red_blank.png"));
		
		fieldPane = new JLayeredPane();
		fieldPane.setPreferredSize(new Dimension(810 , 500));
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
		up_lbl.setBounds(-50, 0 , up_icon.getIconWidth() , up_icon.getIconHeight());
		down_lbl.setBounds(-50, 0 , down_icon.getIconWidth() , down_icon.getIconHeight());
		right_lbl.setBounds(-50, 0 , right_icon.getIconWidth() , right_icon.getIconHeight());
		left_lbl.setBounds(-50, 0 , left_icon.getIconWidth() , left_icon.getIconHeight());
		up_lbl.setVisible(true);
		down_lbl.setVisible(true);
		right_lbl.setVisible(true);
		left_lbl.setVisible(true);
		
		atk_icon = new ImageIcon(getClass().getResource("fighting.png"));
		up_atk_lbl = new JLabel(atk_icon);
		down_atk_lbl = new JLabel(atk_icon);
		right_atk_lbl = new JLabel(atk_icon);
		left_atk_lbl = new JLabel(atk_icon);
		up_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		down_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		right_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		left_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		up_atk_lbl.setVisible(true);
		down_atk_lbl.setVisible(true);
		right_atk_lbl.setVisible(true);
		left_atk_lbl.setVisible(true);
		
		MouseListener ml = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				boolean moved = false;
				Component c;
				c = e.getComponent();
				
				if(c == up_lbl)
				{
					Piece.highlighted.squaresy --;
					
					moved = true;
				}
				else if(c == down_lbl )
				{
					Piece.highlighted.squaresy ++;
					moved = true;
				}
				if(c == right_lbl)
				{
					Piece.highlighted.squaresx ++;
					moved =  true;
				}
				else if(c == left_lbl)
				{
					Piece.highlighted.squaresx --;
					moved = true;
				}
				
				if(moved)
				{
					lastMovedPiece = Piece.highlighted;
					if(Piece.highlighted.id == Piece.SOLDADO)
					{
						if(JOptionPane.showConfirmDialog(null, "Esta peça pode mover mais\n " +
								"de uma vez, deseja utilizar um possível\n" +
								" movimento adicional? (Seu rival saberá que esta peça é um soldado)") != JOptionPane.YES_OPTION)
						{
							lastMovedPiece = null;
							action = ATTACKING;
							hideMoveButtons();
						}
							
					}
					else
					{
						lastMovedPiece = null;
						action = ATTACKING;
						hideMoveButtons();
					}
					informUpdate();
				}
			}
		};
		
		up_lbl.addMouseListener(ml);
		down_lbl.addMouseListener(ml);
		right_lbl.addMouseListener(ml);
		left_lbl.addMouseListener(ml);
		
		
MouseListener mal = new MouseListener() {
			
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
				if(myTurn == true)
				{
					Component c;
					c = arg0.getComponent();
					Piece used = Piece.highlighted;
					Piece.highlighted = null;
					Piece enemy;
					if(c == up_atk_lbl)
					{
						used.squaresy--;
						enemy = getEnemyPiece(used.squaresx , used.squaresy);
						hideAttackLabels();
						
						used.attack(enemy);
						
						informUpdate();
						
						JOptionPane.showMessageDialog(null, "Você Atacou este inimigo.\nA(s) peça(s) será(ão) removida(s) de acordo",
								"Ataque ao inimigo!", JOptionPane.INFORMATION_MESSAGE, enemy.image);
						
						informAttack(used.id , used.squaresx , used.squaresy , enemy.id , enemy.squaresx , enemy.squaresy);
						
					}
					else if(c == down_atk_lbl)
					{
						Piece.highlighted.squaresy ++;
					}
					else if(c == right_atk_lbl)
					{
						Piece.highlighted.squaresx ++;
					}
					else if(c == left_atk_lbl)
					{
						Piece.highlighted.squaresx --;
					}
				}
			}
		};
		
		up_atk_lbl.addMouseListener(mal);
		down_atk_lbl.addMouseListener(mal);
		right_atk_lbl.addMouseListener(mal);
		left_atk_lbl.addMouseListener(mal);
		
		fieldPane.add(up_lbl , new Integer(4));
		fieldPane.add(down_lbl , new Integer(4));
		fieldPane.add(right_lbl , new Integer(4));
		fieldPane.add(left_lbl , new Integer(4));
		fieldPane.add(up_atk_lbl , new Integer(4));
		fieldPane.add(down_atk_lbl , new Integer(4));
		fieldPane.add(right_atk_lbl , new Integer(4));
		fieldPane.add(left_atk_lbl , new Integer(4));
	}
	
	
	public void attackPiece(Piece p , Piece e)
	{
		arrayOfPieces.get(getIndexOfPiece(p)).attack(arrayOfPieces.get(getIndexOfPiece(e)));
	}
	
	public Piece getAllyPiece(int x , int y)
	{
		Piece p = null;
		for(int i = 0 ; i < arrayOfPieces.size()/2 ; i++)
		{
			if((arrayOfPieces.get(i).squaresx == x) && (arrayOfPieces.get(i).squaresy == y))
				p = arrayOfPieces.get(i);
		}
		return p;
	}
	
	public Piece getEnemyPiece(int x , int y)
	{
		Piece p = null;
		for(int i = 40 ; i < arrayOfPieces.size() ; i++)
		{
			if((arrayOfPieces.get(i).squaresx == x) && (arrayOfPieces.get(i).squaresy == y))
				p = arrayOfPieces.get(i);
		}
		return p;
	}
	
	public void hideAttackLabels()
	{
		up_atk_lbl.setVisible(false);
		up_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		down_atk_lbl.setVisible(false);
		down_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		right_atk_lbl.setVisible(false);
		right_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
		left_atk_lbl.setVisible(false);
		left_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
	}
	public void informAttack(int uid , int ux , int uy , int eid , int ex , int ey)
	{
		buildAttackMsg(uid , ux , uy , eid , ex , ey );
		attackedRival = true;
		
	}
	public void buildAttackMsg(int uid , int ux , int uy , int eid , int ex , int ey )
	{
		attackMsg = "gotattack#";
		attackMsg = attackMsg+"("+uid+","+(9-ux)+","+(9-uy)+");";
		attackMsg = attackMsg+"("+eid+","+(9-ex)+","+(9-ey)+")";
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
							informUpdate();
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
			else if(((tab_x == 2)||(tab_x==3)||(tab_x==6)||(tab_x==7))&&((tab_y == 4)||(tab_y==5))) // lake positions
			{
				return false;
			}
		}
		return true;
	}
	
	public void create_sidePanel()
	{
		sidePanel = new JLayeredPane();
		sidePanel.setPreferredSize(new Dimension(200 , 540));
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
				arrayOfPieces.add(new Piece(Piece.SOLDADO , Piece.RED ,new ImageIcon(getClass().getResource("red_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.BOMBA , Piece.RED ,new ImageIcon(getClass().getResource("red_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.CABOARMEIRO , Piece.RED ,new ImageIcon(getClass().getResource("red_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.SARGENTO , Piece.RED ,new ImageIcon(getClass().getResource("red_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.TENENTE , Piece.RED ,new ImageIcon(getClass().getResource("red_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.CAPITAO , Piece.RED ,new ImageIcon(getClass().getResource("red_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.MAJOR , Piece.RED ,new ImageIcon(getClass().getResource("red_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.CORONEL , Piece.RED ,new ImageIcon(getClass().getResource("red_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.GENERAL , Piece.RED ,new ImageIcon(getClass().getResource("red_general.png"))));
				arrayOfPieces.add(new Piece(Piece.MARECHAL , Piece.RED ,new ImageIcon(getClass().getResource("red_marshal.png"))));
				arrayOfPieces.add(new Piece(Piece.BANDEIRA , Piece.RED ,new ImageIcon(getClass().getResource("red_flag.png"))));
				arrayOfPieces.add(new Piece(Piece.ESPIAO, Piece.RED ,new ImageIcon(getClass().getResource("red_spy_2.png"))));
			}
		}
		
	}
	public int getIndexOfPiece(Piece p)
	{
		for(int i = 0; i < arrayOfPieces.size() ; i++)
		{
			if(arrayOfPieces.get(i).equals(p))
			{
				return i;
			}
		}
		return -1;
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
	
	public void hideMoveButtons()
	{
		up_lbl.setVisible(false);
		up_lbl.setBounds(-50, 0 , up_icon.getIconWidth() , up_icon.getIconHeight());
		down_lbl.setVisible(false);
		down_lbl.setBounds(-50, 0 , down_icon.getIconWidth() , down_icon.getIconHeight());
		right_lbl.setVisible(false);
		right_lbl.setBounds(-50, 0 , right_icon.getIconWidth() , right_icon.getIconHeight());
		left_lbl.setVisible(false);
		left_lbl.setBounds(-50, 0 , left_icon.getIconWidth() , left_icon.getIconHeight());
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
						LAYOFFY + current.squaresy*SQFY  + 2*SQFY/3 , current.details.getText().length()* 8 , current.image.getIconHeight()/3);
				
				if(current.mouseIsOn && i < 40)
					current.details.setVisible(true);
				else
					current.details.setVisible(false);
				
				if(current.clickedOn)
				{
					current.clickedOn = false;
					current.selected = !current.selected;
					if(lastMovedPiece == null)
						Piece.highlighted = current;
				}
			}
			if(i > 39)
			{
				switch(current.team)
				{
					case Piece.RED:
						current.label.setIcon(rival_red_icon);
						break;
					case Piece.BLUE:
						current.label.setIcon(rival_blue_icon);
						break;
					default:
							break;
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
				arrayOfPieces.add(new Piece(Piece.SOLDADO , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_soldier.png"))));
			if(i%20 >= 14)
				arrayOfPieces.add(new Piece(Piece.BOMBA , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_bomb.png"))));
			if(i%20 >= 15)
				arrayOfPieces.add(new Piece(Piece.CABOARMEIRO , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_bomber.png"))));
			if(i%20 >= 16)
			{
				arrayOfPieces.add(new Piece(Piece.SARGENTO , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_sergeant.png"))));
				arrayOfPieces.add(new Piece(Piece.TENENTE , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_leautenant.png"))));
				arrayOfPieces.add(new Piece(Piece.CAPITAO , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_captain.png"))));
			}
			if(i%20 >= 17)
				arrayOfPieces.add(new Piece(Piece.MAJOR , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_major.png"))));
			if(i%20 >= 18)
				arrayOfPieces.add(new Piece(Piece.CORONEL , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_colonel.png"))));
			if(i%20 == 19)
			{
				arrayOfPieces.add(new Piece(Piece.GENERAL , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_general.png"))));
				arrayOfPieces.add(new Piece(Piece.MARECHAL , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_marshal.png"))));
				arrayOfPieces.add(new Piece(Piece.BANDEIRA , Piece.BLUE ,new ImageIcon(getClass().getResource("blue_flag.png"))));
				arrayOfPieces.add(new Piece(Piece.ESPIAO, Piece.BLUE ,new ImageIcon(getClass().getResource("blue_spy_2.png"))));
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
				informUpdate();
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
					sel_frame_lbl.setVisible(false);
					if(arrayOfPieces.get(0).team == Piece.RED)
					{
						myTurn = true;
						JOptionPane.showMessageDialog(null, "Você Iniciará o jogo! Boa sorte!");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Seu rival iniciará o jogo! Boa sorte!");
					}
				}
				previousState = PREGAME;
				break;
			case DURINGGAME:
				if(previousState != DURINGGAME)
					informUpdate();
				updatePieces();
				drawPieces();
				updateSideLabels();
				
				if(myTurn)
				{
					boolean[] bmovs = new boolean[4];
					if(Piece.highlighted != null && isAlly(Piece.highlighted))
					{
						switch(action)
						{
							case MOVING:
								bmovs = checkGameMoves();
								showButtons(bmovs);
								break;
							case ATTACKING:
								showAttackLabels(checkGameAttacks(Piece.highlighted.squaresx , Piece.highlighted.squaresy));
								break;
							default:
									break;
						}
					}
					
				}
				else
				{
					if(attackedByRival)
					{
						attackedByRival = false;
						
						showAttackMessage(attackInfo[0] , attackInfo[1] , attackInfo[2] , attackInfo[3] , attackInfo[4] , attackInfo[5]);
						getEnemyPiece(attackInfo[1], attackInfo[2]).attack(getAllyPiece(attackInfo[4], attackInfo[5]));
					}
				}
				previousState = DURINGGAME;
				break;
			case FINISHINGGAME:
				break;
			case PAUSED:
				break;
			default:
					break;
		}
	}
	
	public void showAttackMessage(int eid, int ex , int ey , int mid , int mx , int my)
	{
		ImageIcon icon = new ImageIcon(getClass().getResource("blue_blank.png"));
		String off = "";
		String def = "";
		int team;
		
		if(arrayOfPieces.get(0).team == Piece.RED)
			team = Piece.RED;
		else
			team = Piece.BLUE;
		switch(eid)
		{
		case Piece.BANDEIRA:
			off = "Bandeira";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_flag.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_flag.png"));
			break;
		case Piece.ESPIAO:
			off = "Espião(1)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_spy_2.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_spy_2.png"));
			break;
		case Piece.SOLDADO:
			off = "Soldado(2)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_soldier.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_soldier.png"));
			break;
		case Piece.CABOARMEIRO:
			off = "Cabo Armeiro(3)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_bomber.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_bomber.png"));
			break;
		case Piece.SARGENTO:
			off = "Sargento(4)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_sergeant.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_sergeant.png"));
			break;
		case Piece.TENENTE:
			off = "Tenente(5)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_leautenant.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_leautenant.png"));
			break;
		case Piece.CAPITAO:
			off = "Capitão(6)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_captain.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_captain.png"));
			break;
		case Piece.MAJOR:
			off = "Major(7)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_major.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_major.png"));
			break;
		case Piece.CORONEL:
			off = "Coronel(8)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_colonel.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_colonel.png"));
			break;
		case Piece.GENERAL:
			off = "General(9)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_general.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_general.png"));
			break;
		case Piece.MARECHAL:
			off = "Marechal(10)";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_marshal.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_marshal.png"));
			break;
		case Piece.BOMBA:
			off = "Bomba";
			if(team == Piece.RED)
				icon = new ImageIcon(getClass().getResource("blue_bomb.png"));
			else
				icon = new ImageIcon(getClass().getResource("red_bomb.png"));
			break;
		}
		
		switch(eid)
		{
		case Piece.BANDEIRA:
			def = "Bandeira";
			break;
		case Piece.ESPIAO:
			def = "Espião(1)";
			break;
		case Piece.SOLDADO:
			def = "Soldado(2)";
			break;
		case Piece.CABOARMEIRO:
			def = "Cabo Armeiro(3)";
		case Piece.SARGENTO:
			def = "Sargento(4)";
			break;
		case Piece.TENENTE:
			def = "Tenente(5)";
			break;
		case Piece.CAPITAO:
			def = "Capitão(6)";
			break;
		case Piece.MAJOR:
			def = "Major(7)";
			break;
		case Piece.CORONEL:
			def = "Coronel(8)";
			break;
		case Piece.GENERAL:
			def = "General(9)";
			break;
		case Piece.MARECHAL:
			def = "Marechal(10)";
			break;
		case Piece.BOMBA:
			def = "Bomba";
			break;
		}
		
				
		JOptionPane.showMessageDialog(fieldPane, "Um "+off+" inimigo atacou um dos "+def+" aliados" , "Inimigo atacou", JOptionPane.INFORMATION_MESSAGE, icon);
		
		
	}
	
	public void showAttackLabels(boolean[] b)
	{
		Rectangle r = Piece.highlighted.label.getBounds();
		if((Piece.highlighted.id != Piece.BOMBA)&&(Piece.highlighted.id != Piece.BANDEIRA))
		{
			if(b[0])
			{
				up_atk_lbl.setBounds(r.x - (atk_icon.getIconWidth()-Piece.highlighted.image.getIconWidth())/2 , r.y - atk_icon.getIconHeight() , atk_icon.getIconWidth() , atk_icon.getIconHeight());
				up_atk_lbl.setVisible(true);
			}
			else
			{
				up_atk_lbl.setVisible(false);
				up_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
			}
		
			if(b[1])
			{
				down_atk_lbl.setBounds(r.x - (atk_icon.getIconWidth()-Piece.highlighted.image.getIconWidth())/2, r.y + Piece.highlighted.image.getIconHeight() , atk_icon.getIconWidth() , atk_icon.getIconHeight());
				down_atk_lbl.setVisible(true);
			}
			else
			{
				down_atk_lbl.setVisible(false);
				down_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
			}
			if(b[2])
			{
				right_atk_lbl.setBounds(r.x+Piece.highlighted.image.getIconWidth() , r.y - (atk_icon.getIconHeight()-Piece.highlighted.image.getIconHeight())/2  , atk_icon.getIconWidth() , atk_icon.getIconHeight());
				right_atk_lbl.setVisible(true);
			}
			else
			{
				right_atk_lbl.setVisible(false);
				right_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
			}
			if(b[3])
			{
				left_atk_lbl.setBounds(r.x-atk_icon.getIconWidth() , r.y - (atk_icon.getIconHeight()-Piece.highlighted.image.getIconHeight())/2, atk_icon.getIconWidth() , atk_icon.getIconHeight());
				left_atk_lbl.setVisible(true);
			}
			else
			{
				left_atk_lbl.setVisible(false);
				left_atk_lbl.setBounds(-50, 0 , atk_icon.getIconWidth() , atk_icon.getIconHeight());
			}
		}
		else
		{
			hideAttackLabels();
		}
	}
	
	public boolean[] checkGameAttacks( int x , int y)
	{
		boolean[] nearbyMoves;

		nearbyMoves = nearbyField(x, y);
		for(int i = 0 ; i < nearbyMoves.length ; i++)
		{
			nearbyMoves[i] = !nearbyMoves[i];
			if(nearbyMoves[i] == true)
			{
				nearbyMoves[i] = checkForEnemy(x , y , i);
			}
		}
		return nearbyMoves;
	}
	
	public boolean checkForEnemy(int x , int y , int i)
	{
		switch(i)
		{
			case 0:
				y = y-1;
				break;
			case 1:
				y = y+1;
				break;
			case 2:
				x = x+1;
				break;
			case 3:
				x = x-1;
				break;
			default:
				break;
		}
		for(int j = 40 ; j < arrayOfPieces.size() ; j++)
		{
			if((arrayOfPieces.get(j).squaresx == x)&&(arrayOfPieces.get(j).squaresy == y)&&(arrayOfPieces.get(j).live))
				return true;
		}
		return false;
	}
	
	public boolean isAlly(Piece p)
	{
		if(arrayOfPieces.indexOf(p) < 40)
			return true;
		else
			return false;
	}
	
	public boolean noPieceCanMove()
	{
		
		boolean[] b = new boolean[4];
		{
			for(int i = 0; i < arrayOfPieces.size()/2 ; i++)
			{
				b = checkGameMoves(arrayOfPieces.get(i));
				if(b[0] || b[1] || b[2] || b[3])
					return false;
			}
		}
		return true;
	}
	
	public void showButtons(boolean[] movements)
	{
		Rectangle r = Piece.highlighted.label.getBounds();
		if((Piece.highlighted.id != Piece.BOMBA)&&(Piece.highlighted.id != Piece.BANDEIRA))
		{
			if(movements[0])
			{
				up_lbl.setBounds(r.x - (up_icon.getIconWidth()-Piece.highlighted.image.getIconWidth())/2 , r.y - up_icon.getIconHeight() , up_icon.getIconWidth() , up_icon.getIconHeight());
				up_lbl.setVisible(true);
			}
			else
			{
				up_lbl.setVisible(false);
				up_lbl.setBounds(-50, 0 , up_icon.getIconWidth() , up_icon.getIconHeight());
			}
		
			if(movements[1])
			{
				down_lbl.setBounds(r.x - (down_icon.getIconWidth()-Piece.highlighted.image.getIconWidth())/2, r.y + Piece.highlighted.image.getIconHeight() , down_icon.getIconWidth() , down_icon.getIconHeight());
				down_lbl.setVisible(true);
			}
			else
			{
				down_lbl.setVisible(false);
				down_lbl.setBounds(-50, 0 , down_icon.getIconWidth() , down_icon.getIconHeight());
			}
			if(movements[2])
			{
				right_lbl.setBounds(r.x+Piece.highlighted.image.getIconWidth() , r.y - (right_icon.getIconHeight()-Piece.highlighted.image.getIconHeight())/2  , right_icon.getIconWidth() , right_icon.getIconHeight());
				right_lbl.setVisible(true);
			}
			else
			{
				right_lbl.setVisible(false);
				right_lbl.setBounds(-50, 0 , right_icon.getIconWidth() , right_icon.getIconHeight());
			}
			if(movements[3])
			{
				left_lbl.setBounds(r.x-left_icon.getIconWidth() , r.y - (left_icon.getIconHeight()-Piece.highlighted.image.getIconHeight())/2, left_icon.getIconWidth() , left_icon.getIconHeight());
				left_lbl.setVisible(true);
			}
			else
			{
				left_lbl.setVisible(false);
				left_lbl.setBounds(-50, 0 , left_icon.getIconWidth() , left_icon.getIconHeight());
			}
		}
		else
		{
			hideMoveButtons();
		}
	}
	
	public boolean[] checkGameMoves()
	{
		boolean[] nearbyMoves;

		nearbyMoves = nearbyField(Piece.highlighted.squaresx , Piece.highlighted.squaresy);
		return nearbyMoves;
			
	}
	
	public boolean[] checkGameMoves(Piece p)
	{
		boolean[] nearbyMoves;

		nearbyMoves = nearbyField(p.squaresx , p.squaresy);
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
		if(x+1 < 10)
			retorno[2] = tabIsFree(x+1 , y);
		else
			retorno[2] = false;
		if(x-1 >= 0)
			retorno[3] = tabIsFree(x-1 , y);
		else
			retorno[3] = false;
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
	
	public void buildCommandMsg()
	{
		updtgame = "command#";
		updtgame = updtgame+sendIntArrayAsString(dead_pieces);
		updtgame = updtgame+"@";
		updtgame = updtgame+sendPieceArrayListAsString(arrayOfPieces , 0);
		updtgame = updtgame+"\n";
	}
	
	public String sendPieceArrayListAsString(ArrayList<Piece> ap , int type)
	{
		String s = "";
		if (type == 0)
		{
			for(int i = 0 ; i < ap.size()/2 ; i ++)
			{
				s=s+"("+ap.get(i).squaresx+","+ap.get(i).squaresy+",";
				if(ap.get(i).live)
					s = s+"1);";
				else
					s = s+"0);";
			}
		}
		else
		{
			for(int i = ap.size()/2 ; i < ap.size() ; i ++)
			{
				s=s+"("+ap.get(i).squaresx+","+ap.get(i).squaresy+",";
				if(ap.get(i).live)
					s = s+"1);";
				else
					s = s+"0);";
			}
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
	
	public void informUpdate()
	{
		buildCommandMsg();
		updateRival = true;
	}
}
