import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import javax.swing.ImageIcon;
public class Piece
{
	public static enum PATENTE { BANDEIRA , ESPIAO , SOLDADO , CABOARMEIRO ,
		SARGENTO , TENENTE , CAPITAO , MAJOR , CORONEL , GENERAL , MARECHAL , BOMBA
	}
	public static enum TEAM { RED , BLUE };
	
	public static Piece highlighted = null;
	PATENTE id;
	TEAM team;
	public boolean live;
	public int squaresx = -1;
	public int squaresy = -1;
	public float x;
	public float y;
	public float velx;
	public float vely;
	public ImageIcon image;
	public boolean gotFlag = false;
	public JLabel label;
	public JLabel details;
	
	public boolean mouseIsOn = false;
	public boolean selected = false;
	public boolean clickedOn = false;
	
	public Piece(PATENTE p , TEAM t , ImageIcon im)
	{
		id = p;
		team = t;
		image = im;
		switch(p)
		{
			case BANDEIRA:
				details = new JLabel("Bandeira");
				break;
			case ESPIAO:
				details = new JLabel("Espião");
				break;
			case SOLDADO:
				details = new JLabel("Soldado");
				break;
			case CABOARMEIRO:
				details = new JLabel("Cabo Armeiro");
				break;
			case SARGENTO:
				details = new JLabel("Sargento");
				break;
			case TENENTE:
				details = new JLabel("Tenente");
				break;
			case CAPITAO:
				details = new JLabel("Capitão");
				break;
			case MAJOR:
				details = new JLabel("Major");
				break;
			case CORONEL:
				details = new JLabel("Coronel");
				break;
			case GENERAL:
				details = new JLabel("General");
				break;
			case MARECHAL:
				details = new JLabel("Marechal");
				break;
			case BOMBA:
				details = new JLabel("Bomba");
				break;
		}
		
		details.setBackground(Color.WHITE);
		details.setOpaque(true);
		details.setAlignmentX(SwingConstants.CENTER);
		details.setVisible(false);
		
		label = new JLabel(im);
		label.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				clickedOn = true;
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mouseIsOn = true;
				details.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mouseIsOn = false;
				details.setVisible(false);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		
		label.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		live = true;
		
	}
	
	public void update()
	{
		if(this.live)
		{
			x += velx;
			y += vely;
			label.setVisible(true);
		}
		else
			label.setVisible(false);
	}
	
	public void attack(Piece enemy)
	{
		switch(this.id)
		{
			case BANDEIRA:
				switch(enemy.id){
					default:
						enemy.gotFlag = true;
						break;
				}
				break;
			case ESPIAO:
				switch(enemy.id)
				{
					case MARECHAL:
						enemy.live = false;
						break;
					case ESPIAO:
						enemy.live = false;
						this.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						this.live = false;
						break;
				}
				break;
			case SOLDADO:
				switch(enemy.id)
				{
					case SOLDADO:
						this.live = false;
						enemy.live = false;
						break;
					case ESPIAO:
						enemy.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						this.live = false;
						break;	
				}
				break;
			case CABOARMEIRO:
				switch(enemy.id)
				{
					
					case CABOARMEIRO:
						this.live = false;
						enemy.live = false;
						break;
					case ESPIAO:
						enemy.live = false;
						break;
					case SOLDADO:
						enemy.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					case BOMBA:
						enemy.live = false;
						break;
					default:
						this.live = false;
						break;
				}
				break;
			case SARGENTO:
				switch(enemy.id)
				{
					case SARGENTO:
						this.live = false;
						enemy.live = false;
						break;
					case ESPIAO:
						enemy.live = false;
						break;
					case SOLDADO:
						enemy.live = false;
						break;
					case CABOARMEIRO:
						enemy.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						this.live = false;
						break;
				}
				break;
			case TENENTE:
				switch(enemy.id)
				{
					case TENENTE:
						this.live = false;
						enemy.live = false;
						break;
					case ESPIAO:
						enemy.live = false;
						break;
					case SOLDADO:
						enemy.live = false;
						break;
					case CABOARMEIRO:
						enemy.live = false;
						break;
					case SARGENTO:
						enemy.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						this.live = false;
						break;
				}
				break;
			case CAPITAO:
				switch(enemy.id)
				{
					case CAPITAO:
						this.live = false;
						enemy.live = false;
						break;
					case MAJOR:
						this.live = false;
						break;
					case CORONEL:
						this.live = false;
						break;
					case GENERAL:
						this.live = false;
						break;
					case MARECHAL:
						this.live = false;
						break;
					case BOMBA:
						this.live = false;
						break;
					default:
						enemy.live = false;
						break;
				}
				break;
			case MAJOR:
				switch(enemy.id)
				{
					case MAJOR:
						this.live = false;
						enemy.live = false;
						break;
					case CORONEL:
						this.live = false;
						break;
					case GENERAL:
						this.live = false;
						break;
					case MARECHAL:
						this.live = false;
						break;
					case BOMBA:
						this.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						enemy.live = false;
						break;
				}
				break;
			case CORONEL:
				switch(enemy.id)
				{
					case CORONEL:
						this.live = false;
						enemy.live = false;
						break;
					case GENERAL:
						this.live = false;
						break;
					case MARECHAL:
						this.live = false;
						break;
					case BOMBA:
						this.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						break;
				}
				break;
			case GENERAL:
				switch(enemy.id)
				{
					case GENERAL:
						this.live = false;
						enemy.live = false;
						break;
					case MARECHAL:
						this.live = false;
						break;
					case BOMBA:
						this.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						enemy.live = false;
						break;
				}
				break;
			case MARECHAL:
				switch(enemy.id)
				{
					case MARECHAL:
						this.live = false;
						enemy.live = false;
						break;
					case BOMBA:
						this.live = false;
						break;
					case BANDEIRA:
						this.gotFlag = true;
						break;
					default:
						enemy.live = false;
						break;
				}
				break;
		default:
			break;
		}
	}
	
}
