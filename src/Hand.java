import java.util.ArrayList;
import java.util.Arrays;

public class Hand{
	private ArrayList<Card> hand;
	public Hand() {
		hand=new ArrayList<Card>();
	}
	public Hand(Card[] test) {
		hand=new ArrayList<Card>(Arrays.asList(test));
	}
	public int left() {
		return hand.size();
	}
	public ArrayList<Card> getHand() {
		return hand;
	}
	public Card play(int index) {
		return hand.remove(index);
	}
	public void get(Card add) {
		hand.add(add);
	}
	public void get(ArrayList<Card> add) {
		for(Card c:add)
			hand.add(c);
	}
}
