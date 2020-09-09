import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class Game extends JPanel{
	private Deck deck;
	private Hand player;
	private Hand[] computer;
	private Card discard;
	private int[] points;
	private int turn;
	private boolean clockwise;
	private boolean add4;
	private boolean drew;
	public Game(int n) {
		deck=new Deck();
		computer=new Hand[n];
		player=new Hand();
		for(int j=0;j<computer.length;j++)
			computer[j]=new Hand();
		for(int i=0;i<7;i++) {
			player.get(deck.getCard());
			for(int j=0;j<computer.length;j++)
				computer[j].get(deck.getCard());
		}
		points=new int[computer.length+1];
		turn=0;
		clockwise=true;
		add4=false;
		drew=false;
		do {
			discard=deck.getCard();
		}while(discard.getColor()==Color.black);
		if(discard.getType().equals("skip"))
			nextTurn(1);
		else if(discard.getType().equals("reverse")) {
			clockwise=!clockwise;
			nextTurn(1);
		}else if(discard.getType().equals("+2")) {
			player.get(deck.getCard());
			player.get(deck.getCard());
			nextTurn(1);
		}		
	}
	public void newRound() {
		turn=0;
		clockwise=true;
		deck.makeNew();
		player=new Hand();
		for(int j=0;j<computer.length;j++)
			computer[j]=new Hand();
		for(int i=0;i<7;i++) {
			player.get(deck.getCard());
			for(int j=0;j<computer.length;j++)
				computer[j].get(deck.getCard());
		}
		do {
			discard=deck.getCard();
		}while(discard.getColor()==Color.black);
		if(discard.getType().equals("skip"))
			nextTurn(1);
		else if(discard.getType().equals("reverse")) {
			clockwise=!clockwise;
			nextTurn(1);
		}else if(discard.getType().equals("+2")) {
			player.get(deck.getCard());
			player.get(deck.getCard());
			nextTurn(1);
		}		
	}
	public void restartGame() {
		newRound();		
		add4=false;
		drew=false;
		for(int i=0;i<points.length;i++) {
			points[i]=0;
		}
	}
	public void nextTurn(int n) {
		if(clockwise)
			turn+=n;
		else
			turn-=n;
		if(clockwise)
			turn=turn%(computer.length+1);
		else
			turn=(turn+computer.length+1)%(computer.length+1);
	}
	public int totalPoints() {
		int p=0;
		for(Card c:player.getHand())
			p+=c.points();
		for(int i=0;i<computer.length;i++) {
			for(Card c:computer[i].getHand())
				p+=c.points();
		}
		if(player.getHand().size()==0) {
			System.out.println("Player wins round with "+p+" points.");
			points[0]=p;
		}else {
			for(int i=0;i<computer.length;i++) {
				if(computer[i].getHand().size()==0) {
					System.out.println("Computer "+(i+1)+" wins round with "+p+" points.");
					points[i+1]+=p;
				}
			}
		}
		return p;
	}
	public void playerPlay(int index) {
		Card check=player.getHand().get(index);
		if(legal(check)) {
			Color temp=discard.getColor();
			discard=player.play(index);
			drew=false;			
			if(discard.getType().equals("+4")) {
				nextTurn(1);
				System.out.println(turn);
				discard.setColor(temp);
				for(int i=0;i<4;i++)
					computer[turn-1].get(deck.getCard());
				nextTurn(1);
			}else if(discard.special()&&!add4)
				specialEffect();
			else {				
				nextTurn(1);
			}
		}
	}
	public void playerDraw() {
		Card draw=deck.getCard();
		player.get(draw);
		System.out.println("You drew "+draw+" "+possible());
		if(!possible())
			nextTurn(1);
		else 
			drew=true;
	}
	public void computerPlay(int n) {
		drew=false;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < computer[n].getHand().size(); i++) {
			if (computer[n].getHand().get(i).compatible(discard)) {
				int temp;
				if(map.containsKey(computer[n].getHand().get(i))) {
					temp = map.get(computer[n].getHand().get(i));
					map.put(computer[n].getHand().get(i).getColorString(), temp++);
				}
				else map.put(computer[n].getHand().get(i).getColorString(), 1);
				// trying to find the color with most frequency that is also compatible
			}
		}
		int max = 0;
		String maxtype = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > max) {
				max = Math.max(max, entry.getValue());
				maxtype = entry.getKey();
			}
		}
		for (int i = 0; i < computer[n].getHand().size(); i++) {
			if (computer[n].getHand().get(i).compatible(discard) && !maxtype.equals("")
					&& computer[n].getHand().get(i).getColorString().equals(maxtype)) {
				discard = computer[n].play(i);
				System.out.println("Computer " + (n + 1) + " plays " + discard);
				if (discard.special()) {
					compSpecialEffect();
				} else
					nextTurn(1);
				return;
			}
		}

		System.out.println("Computer " + (n + 1) + " draws a card");
		computer[n].get(deck.getCard());
		if (!possible())
			nextTurn(1);
		else
			computerPlay(n);
	}
	public void specialEffect() {
		if(discard.getType().equals("skip"))
			nextTurn(2);
		else if(discard.getType().equals("reverse")) {
			clockwise=!clockwise;
			if(computer.length>1)
				nextTurn(1);
		}else if(discard.getType().equals("+2")) {
			nextTurn(1);
			if(turn==0) {
				player.get(deck.getCard());
				player.get(deck.getCard());
			}else {
				computer[turn-1].get(deck.getCard());
				computer[turn-1].get(deck.getCard());
			}
			nextTurn(1);
		}else {			
			nextTurn(1);
		}
	}
	public void compSpecialEffect() {
		if(discard.getType().equals("skip"))
			nextTurn(2);
		else if(discard.getType().equals("reverse")) {
			clockwise=!clockwise;
			if(computer.length>1)
				nextTurn(1);
		}else if(discard.getType().equals("+2")) {
			nextTurn(1);
			if(turn==0) {
				player.get(deck.getCard());
				player.get(deck.getCard());
			}else {
				computer[turn-1].get(deck.getCard());
				computer[turn-1].get(deck.getCard());
			}
			nextTurn(1);
		}else if(discard.getType().equals("wild")) {
			if(computer[turn-1].getHand().size()>0) {
				Color[] temp=new Color[computer[turn-1].getHand().size()];
				for(int i=0;i<computer[turn-1].getHand().size();i++) {
					temp[i]=computer[turn-1].getHand().get(i).getColor();
				}		
				discard.setColor(mostFrequent(temp));
				System.out.println("The color is "+discard.toString().split(" ")[0]);
			}
			nextTurn(1);
		}else if(discard.getType().equals("+4")) {
			if(computer[turn-1].getHand().size()>0) {
				Color[] temp=new Color[computer[turn-1].getHand().size()];
				for(int i=0;i<computer[turn-1].getHand().size();i++) {
					temp[i]=computer[turn-1].getHand().get(i).getColor();
				}		
				discard.setColor(mostFrequent(temp));
				System.out.println("The color is "+discard.toString().split(" ")[0]);
			}
			nextTurn(1);
			if(turn==0) {
				for(int i=0;i<4;i++)
					player.get(deck.getCard());
			}else {
				for(int i=0;i<4;i++)
					computer[turn-1].get(deck.getCard());
			}
			nextTurn(1);
		}
	}
	public Color mostFrequent(Color arr[]) { 
        Map<Color, Integer> hp = new HashMap<Color, Integer>();           
        for(int i = 0; i < arr.length; i++) { 
            Color key = arr[i]; 
            if(hp.containsKey(key)) { 
                int freq = hp.get(key)+1; 
                hp.put(key, freq); 
            } 
            else { 
                hp.put(key, 1); 
            } 
        } 

        int max_count = 0;
        Color res = Color.red;           
        for(Entry<Color, Integer> val : hp.entrySet()) 
        { 
            if (max_count < val.getValue()) 
            { 
                res = val.getKey(); 
                max_count = val.getValue(); 
            } 
        } 
        if(res!=Color.black)
        	return res; 
        else
        	return Color.red;
    } 
	public boolean possible() {
		if(turn==0) {
			for(Card c:player.getHand())
				if(c.compatible(discard))
					return true;
			return false;
		}else {			
			for(Card c:computer[turn-1].getHand())
				if(c.compatible(discard))
					return true;
			return false;
		}
	}
	public boolean legal(Card check) {
		if(check.getType().equals("+4")) {
			for(Card c:player.getHand()) {
				if(c.getColor()==discard.getColor()) {
					player.get(deck.getCard(4));
					System.out.println("Penalty: +4 cards");
					add4=true;
					return true;
				}
			}
			return true;
		}else if(check.compatible(discard)||check.getType().equals("wild"))
			return true;
		else 
			return false;
	}	
	public boolean roundOver() {
		if(player.left()==0)
			return true;
		else {
			for(Hand h:computer)
				if(h.left()==0)
					return true;
			return false;
		}
	}
	public boolean gameOver() {
		if(roundOver())
			totalPoints();
		for(int i:points)
			if(i>=500)
				return true;
		return false;
	}
	public ArrayList<Card> getPlayerHand() {
		return player.getHand();
	}
	public int getComputerNum(int n) {
		return computer[n].getHand().size();
	}
	public Card getDiscard() {
		return discard;
	}
	public int getPlayers() {
		return computer.length+1;
	}
	public int getTurn() {
		return turn;
	}
	public boolean getDrew() {
		return drew;
	}
	public int[] getPoints() {
		return points;
	}
	public void unoPenalty() {
		player.get(deck.getCard());
		player.get(deck.getCard());
	}
	public boolean added4() {
		if(add4) {
			add4=false;
			return true;
		}
		return false;
	}
}
