import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Card> deck;
	public Deck() {
		deck=new ArrayList<Card>();
		makeNew();
	}
	public void makeNew() {
		Color[] temp= {Color.red,Color.yellow,Color.green,Color.blue};
		for(Color c:temp) {
			for(int i=0;i<10;i++) {
				deck.add(new Card(c,i+""));
			}
			deck.add(new Card(c,"skip"));
			deck.add(new Card(c,"reverse"));
			deck.add(new Card(c,"+2"));
			for(int i=1;i<10;i++) {
				deck.add(new Card(c,i+""));
			}
			deck.add(new Card(c,"skip"));
			deck.add(new Card(c,"reverse"));
			deck.add(new Card(c,"+2"));
		}
		for(int i=0;i<4;i++) {
			deck.add(new Card(Color.black,"wild"));
			deck.add(new Card(Color.black,"+4"));
		}
		shuffle();
	}
	public void shuffle() {
		Collections.shuffle(deck);
	}
	public Card getCard() {
		return deck.remove(deck.size()-1);
	}
	public ArrayList<Card> getCard(int num) {
		ArrayList<Card> temp=new ArrayList<Card>();
		for(int i=0;i<num;i++) {
			temp.add(deck.remove(deck.size()-1));
		}
		return temp;
	}
	public int left() {
		return deck.size();
	}
}
