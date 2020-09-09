import javax.imageio.ImageIO;
import javax.swing.*;
/*Everything in game progresses by clicks
 * Reminders:
 * with 2 players, drawing a reverse and playing reverse with no possible moves left freezes game
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class Interface extends JPanel implements ActionListener, MouseListener{
	private Game uno;
	private static JFrame frame;
	private static JPanel start;
	private static JPanel rules;
	private static JPanel numPlayers;
	private static boolean started;
	private static boolean pressed;
	private static boolean choosing;
	private static boolean canceled;
	private static Timer t;
	private static JPanel top;
	private static JButton ruleButton;
	private static JButton[] colors= {new JButton("Red"),new JButton("Yellow"),
			new JButton("Green"),new JButton("Blue")};
	public Interface() {
		ruleButton=new JButton();
		ruleButton.setText("Rules");
	    ruleButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); 
	    ruleButton.setBackground(Color.white);
	    ruleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(rules);
				frame.revalidate();
			}});
		start=new Startscreen();
		rules=new JPanel();
		numPlayers=new JPanel();
		top=new JPanel();
		makeRules();
		makeNumPlayers();
		started=false;
		pressed=false;
		choosing=false;
		canceled=true;
		this.addMouseListener(this);
		t=new Timer(2000,new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) {	
				  	if(!choosing&&uno.getPlayerHand().size()==1&&!pressed) {
						uno.unoPenalty();
					}
				  	if(uno.getTurn()==0)
						t.stop();
				  	else {
				  		uno.computerPlay(uno.getTurn()-1);
				  	}
				  	if(!uno.gameOver()&&uno.roundOver()) {
						repaint();
						message("Computer "+(uno.getPlayers()-1)+" wins round with "
						+uno.totalPoints()+" points.");
						uno.newRound();
						makeTop();
						frame.revalidate();
					}
				  	repaint();
			  }
			});
		uno=new Game(2);
		colors[0].setBackground(Color.red);
		colors[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					uno.getDiscard().setColor(Color.red);
					JOptionPane.getRootFrame().dispose();
					canceled=false;
					t.start();}});
		colors[1].setBackground(Color.yellow);
		colors[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					uno.getDiscard().setColor(Color.yellow);
					JOptionPane.getRootFrame().dispose();
					canceled=false;
					t.start();}});
		colors[2].setBackground(Color.green);
		colors[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					uno.getDiscard().setColor(Color.green);
					JOptionPane.getRootFrame().dispose();
					canceled=false;
					t.start();}});
		colors[3].setBackground(Color.blue);
		colors[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					uno.getDiscard().setColor(Color.blue);
					JOptionPane.getRootFrame().dispose();
					canceled=false;
					t.start();}});
	}
	public void makeRules() {
		JLabel title=new JLabel("Rules");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
		title.setForeground(Color.darkGray);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton back=new JButton();
		back.setText("Back");
	    back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); 
	    back.setBackground(Color.white);
	    back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(started)
					frame.setContentPane(Interface.this);
				else
					frame.setContentPane(Interface.start);
				frame.revalidate();
			}});
	    JPanel p=new JPanel();
	    p.add(back);
	    p.setOpaque(false);
	    p.setBorder(BorderFactory.createEmptyBorder(10,0,0,900));	
	    p.setMaximumSize(new Dimension(Short.MAX_VALUE,100));	    
		JLabel text=new JLabel();
		text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		text.setText("<html>Every player starts with seven cards, and the rest of the cards are placed "
				+ "in a Draw Pile face down. The top card is placed in Discarded Pile and the game begins. "
				+ "You go first. Click your card that matches the color or type to play the card. Click "
				+ "the Draw Pile when there "
				+ "are no matches or you choose not to play any of their cards to draw a new card. "
				+ "If that card can be played, play it. Otherwise, the game moves on to the next "
				+ "person in turn. You can also play a Wild card or a Wild Draw Four card anytime. "
				+ "If the first card turned up from the Draw Pile is an action card, the action is applied "
				+ "to you. The game continues until a player has one card left. The moment a "
				+ "you have just one card you must yell “UNO” by clicking the uno button. If you do not do so before "
				+ "another player has their next turn, you must draw two new cards. "
				+ "Once a player has no cards remaining, the game round is over, points are scored, and "
				+ "the game begins over again. The goal of uno is to be the first to reach "
				+ "500 points, obtained by adding the worth of all opponents’ cards. Numbered cards are "
				+ "worth their number while action cards are worth more (see below).<br><br>"
				+ "Action Cards:<br>"
				+ "Reverse – If going clockwise, switch to counterclockwise or vice versa (20 pt)<br>"
				+ "Skip – When a player places this card, the next player has to skip their turn (20 pt)<br>"
				+ "Draw Two – When a player places this card, the next player will have to pick up two cards and forfeit turn (20 pt)<br>"
				+ "Wild – The player has to state which color it will represent for the next player, which will be displayed in the colored square. It can be played regardless of whether another card is available (50 pt)<br>"
				+ "Wild Draw Four – This acts just like the wild card except that the next player also has to draw four cards. With this card, you must have no other alternative cards to play that matches the color of the card previously played. If you play this card illegally, you need to draw 4 cards (50 pt)</html>");
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		rules.setBackground(Color.white);
	    rules.add(p);
		rules.add(title);
		rules.add(text);
		rules.setLayout(new BoxLayout(rules, BoxLayout.Y_AXIS));
		rules.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));	
	}
	public void makeNumPlayers() {
		numPlayers.setLayout(new GridLayout(1,3,50,50));
		JButton play2=new JButton("2 players");
		play2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
	    play2.setBackground(Color.white);	
		play2.addActionListener(this);	
		JButton play3=new JButton("3 players");
		play3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
	    play3.setBackground(Color.white);
		play3.addActionListener(this);	
		JButton play4=new JButton("4 players");
		play4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
	    play4.setBackground(Color.white);
		play4.addActionListener(this);	
		numPlayers.add(play2);
		numPlayers.add(play3);
		numPlayers.add(play4);
		numPlayers.setBorder(BorderFactory.createEmptyBorder(350,50,350,50));		    
	}
	public void makeTop() {
		top.removeAll();
		JPanel p1=new JPanel();
		p1.setLayout(new GridLayout(2,2,850,10));
		JPanel p2=new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		top.setBorder(BorderFactory.createEmptyBorder(20,50,50,50));	
		JButton exit=new JButton("Exit");
		exit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); 
	    exit.setBackground(Color.white);
	    exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				started=false;
				start.repaint();
				frame.setContentPane(start);
				frame.revalidate();
			}});	
		JButton replay=new JButton("Replay");
		replay.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); 
	    replay.setBackground(Color.white);
	    replay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				uno.restartGame();
				makeTop();
				if(uno.getTurn()!=0)
					t.start();
				repaint();
				frame.revalidate();
			}});	
	    JButton one=new JButton(" Uno ");
		one.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); 
	    one.setBackground(Color.white);
	    one.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(uno.getPlayerHand().size()==1) {
					pressed=true;
					repaint();
					frame.revalidate();
				}
			}});	
	    one.setAlignmentX(Component.RIGHT_ALIGNMENT);
	    String[] column = {"Player","Points"};
	    for(int i=0;i<column.length;i++) {
	    	column[i]=uno.getPoints()[i]+"";
	    }
	    Object[][] data = new Object[uno.getPoints().length][2];
	    for(int i=0;i<uno.getPoints().length;i++) {
	    	if(i==0)	    		
	    		data[i][0]="Player";
	    	else
	    		data[i][0]="Computer "+i;
	    	data[i][1]=uno.getPoints()[i]+"";
	    }	
	    JTable table = new JTable(data, column);
	    top.setOpaque(false);
	    p1.setOpaque(false);
		p1.add(exit);
		p1.add(ruleButton);
		p1.add(replay);
		top.add(p1);
		top.add(Box.createRigidArea(new Dimension(0,550)));
		p2.add(table);
		p2.add(Box.createRigidArea(new Dimension(800,0)));
		p2.add(one);
		p2.setOpaque(false);
		if(!uno.gameOver())
			top.add(p2);
	}
	public void chooseColor(String s,String title) {
		JPanel temp=new JPanel();
		JLabel text=new JLabel();
		text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		text.setText(s);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		temp.add(text);
	    JOptionPane.showOptionDialog(null, temp, title,
	    		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, colors, null);
	    if(canceled)
	    	uno.getDiscard().setColor(Color.red);
	}
	public void message(String s) {
		JOptionPane.showMessageDialog(null, s);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().split(" ")[1].equals("players")) {
			uno=new Game(Integer.parseInt(e.getActionCommand().split(" ")[0])-1);
			makeTop();
			started=true;			
			this.add(top);
			if(uno.getTurn()!=0)
				t.start();
			repaint();
			frame.setContentPane(Interface.this);
		}		
		frame.revalidate();
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(0, 0, getWidth(), getHeight());	
		paintBack(g,600-144,300,true);
		uno.getDiscard().update(600-144,300);
		g.setColor(uno.getDiscard().getColor());
		g.fillRect(750, 500, 50, 50);//color square
		paintCard(g,uno.getDiscard(),600,300,1);
		g.setFont(new Font("Arial Bold", Font.PLAIN, 50));
		g.setColor(Color.black);
		for(int i=0;i<uno.getPlayerHand().size();i++) {
			paintCard(g,uno.getPlayerHand().get(i),300+75*i,getHeight()-250,1);
			uno.getPlayerHand().get(i).update(300+75*i,getHeight()-250);
		}
		drawName("Player",520, 560,g);		
		if(uno.getPlayers()==2) {
			for(int i=0;i<uno.getComputerNum(0);i++) {
				paintBack(g,340+75*i,50,false);
			}
			drawName("Comp 1", 520, 280,g);
		}else if(uno.getPlayers()==3) {		
			for(int i=0;i<uno.getComputerNum(0);i++) {
				paintBack(g,70,150+50*i,false);
			}
			drawName("Comp 1", 200, getHeight()/2,g);
			for(int i=0;i<uno.getComputerNum(1);i++) {
				paintBack(g,980,150+50*i,false);
			}
			drawName("Comp 2", 770, getHeight()/2,g);
		}else {
			for(int i=0;i<uno.getComputerNum(0);i++) {
				paintBack(g,70,150+50*i,false);
			}
			drawName("Comp 1", 200, getHeight()/2,g);
			for(int i=0;i<uno.getComputerNum(1);i++) {
				paintBack(g,340+75*i,50,false);
			}
			drawName("Comp 2", 520, 280,g);
			for(int i=0;i<uno.getComputerNum(2);i++) {
				paintBack(g,980,150+50*i,false);
			}
			drawName("Comp 3",770, getHeight()/2,g);
		}	
		if(uno.gameOver()) {
			drawGameOver(g);
			frame.revalidate();
			t.stop();
		}
	}
	public void drawGameOver(Graphics g) {
		if(uno.getPlayerHand().size()==0) {
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());	
			g.setColor(Color.cyan);
			g.setFont(new Font("Arial Bold", Font.PLAIN, 80));
			g.drawString("Player wins game!",250, getHeight()/2);
			g.drawString(uno.totalPoints()+"",getWidth()/2-50, getHeight()/2+100);
		}else {
			for(int i=0;i<uno.getPlayers()-1;i++) {
				if(uno.getComputerNum(i)==0){
					g.setColor(Color.white);
					g.fillRect(0, 0, getWidth(), getHeight());	
					g.setColor(Color.cyan);
					g.setFont(new Font("Arial Bold", Font.PLAIN, 80));
					g.drawString("Computer "+(i+1)+" wins game!",170, getHeight()/2);
					g.drawString(uno.totalPoints()+"",getWidth()/2-50, getHeight()/2+100);
				}
			}
		}
	}
	public void drawName(String name,int x,int y,Graphics g) {
		if(name.equals("Player")) {
			if(uno.getTurn()==0) {
				g.setColor(Color.white);
				if(pressed)
					g.setColor(new Color(255,100,100));
				g.drawString(name,x, y);	
				g.setColor(Color.black);
			}else {
				g.setColor(Color.black);
				if(pressed) 
					g.setColor(Color.red);				
				g.drawString(name,x, y);	
			}
		}else {
			if(name.equals("Comp "+uno.getTurn())) {
				g.setColor(Color.white);
				if(uno.getComputerNum(uno.getTurn()-1)==1)
					g.setColor(new Color(255,100,100));
				g.drawString(name,x, y);	
				g.setColor(Color.black);
			}else {
				g.setColor(Color.black);
				if(uno.getComputerNum(Integer.parseInt(name.split(" ")[1])-1)==1) 
					g.setColor(Color.red);				
				g.drawString(name,x, y);	
			}
		}
	}
	public void paintCard(Graphics g,Card c,int x,int y,double scale) {
		g.drawImage(c.getImage(),x,y,(int)(c.getImage().getWidth(this)*scale),
				(int)(c.getImage().getHeight(this)*scale),this);
		if(!c.special()) {
			g.setFont(new Font("Arial Bold", Font.PLAIN, 100));
			g.setColor(c.getColor());
			if(c.getColor()==Color.yellow)
				g.setColor(new Color(255,170,0));//yellow
			else if(c.getColor()==Color.green)
				g.setColor(new Color(85,170,85));//green
			g.drawString(c.getType(),x+c.getImage().getWidth(this)/2-25,
					y+c.getImage().getHeight(this)/2+35);
			g.setFont(new Font("Arial Bold", Font.PLAIN, 50));
			g.setColor(Color.white);
			g.drawString(c.getType(),x+c.getImage().getWidth(this)/4-20,
					y+c.getImage().getHeight(this)/4+5);
			g.drawString(c.getType(),x+c.getImage().getWidth(this)-45,
					y+c.getImage().getHeight(this)-20);
		}
	}
	public void paintBack(Graphics g,int x,int y,boolean large) {
		Image i=new ImageIcon(getClass().getResource("images/back.png")).getImage();
		if(large)
			i=new ImageIcon(getClass().getResource("images/largeback.png")).getImage();
		g.drawImage(i,x,y,this);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x=arg0.getX();
		int y=arg0.getY();	
		if(!t.isRunning()) {
		if(!uno.getDrew()&&uno.getDiscard().contains(x, y, true))
			uno.playerDraw();	
		
		for(int i=0;i<uno.getPlayerHand().size();i++) {
			if(uno.getPlayerHand().get(i).contains(x, y,i==uno.getPlayerHand().size()-1)) {					
				uno.playerPlay(i);
				if(uno.getDiscard().getType().equals("wild")
						||uno.getDiscard().getType().equals("+4")) {
					choosing=true;
					if(!uno.added4()) {
						repaint();
						canceled=true;
						chooseColor("choose a color","color");
						repaint();
					}
				}
				i=uno.getPlayerHand().size();					
			}
		}
		if(!uno.gameOver()&&uno.getPlayerHand().size()==0) {
			repaint();
			message("Player wins round with "+uno.totalPoints()+" points.");
			uno.newRound();
			makeTop();
			frame.revalidate();
		}
		if(uno.getTurn()!=0||!uno.possible())
			t.start();
		if(choosing)
			choosing=false;
		if(pressed&&uno.getPlayerHand().size()>1)
			pressed=false;       
		repaint();
		}
	}
	public static void main(String[] args) throws InterruptedException {
        Interface panel = new Interface();
        frame = new JFrame( "Animation" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane(start);
        frame.setSize(1200,1000);
        frame.setVisible( true );
    }
	class Startscreen extends JPanel {
		private JButton startButton;
		private JPanel p;
		public Startscreen() {
			startButton=new JButton();
			startButton.setText("   Start   ");
		    startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
		    startButton.setBackground(Color.red);
		    startButton.setForeground(Color.yellow);	
		    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		    startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					started=true;
					frame.setContentPane(numPlayers);
					frame.revalidate();
				}});	
		    p=new JPanel();
		    p.add(ruleButton);
		    p.setOpaque(false);
		    p.setBorder(BorderFactory.createEmptyBorder(50,900,0,0));	
		    p.setMaximumSize(new Dimension(Short.MAX_VALUE,400));	
		    this.add(p);
		    this.add(startButton);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.orange);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(new ImageIcon(getClass().getResource("images/background.png")).getImage(),0,50,this);
			p.setBorder(BorderFactory.createEmptyBorder(50,900,0,0));	
			p.add(ruleButton);
			p.setOpaque(false);	
			p.setMaximumSize(new Dimension(Short.MAX_VALUE,400));	
		}
	}	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}