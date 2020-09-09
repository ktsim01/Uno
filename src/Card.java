import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Card{
	private Color color;
	private String type;
	private Image image;
	private int x;
	private int y;
	public Card(Color c,String t) {
		color=c;
		type=t;
		String path="images/"+toString().split(" ")[0];
		if(!special())
			path+="base";
		else if(type.equals("reverse"))
			path+="rev";
		else if(!type.equals("wild")&&!type.equals("+4"))
			path+=type;
		image=new ImageIcon(getClass().getResource(path+".png")).getImage();
		x=0;
		y=0;
	}
	public void update(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public boolean contains(int a,int b,boolean first) {
		if(first)
			return (a>x&&(a-x)<=144&&b>y&&(b-y)<=215);
		else
			return (a>x&&(a-x)<=75&&b>y&&(b-y)<=215);
	}
	public void setColor(Color c) {
		color=c;
	}
	public Color getColor() {
		return color;
	}
	public String getType() {
		return type;
	}
	public String toString() {
		String c="red";
		if(color==Color.yellow)
			c="yellow";
		else if(color==Color.green)
			c="green";
		else if(color==Color.blue)
			c="blue";
		else if(color==Color.black)
			return type;
		return c+" "+type;
	}
	public String getColorString() {
		return toString().split(" ")[0];
	}
	public boolean compatible(Card c) {
		if(color==Color.black)
			return true;
		else {
			return type.equals(c.getType())||color==c.getColor();
		}
	}
	public boolean special() {
		if(color==Color.black)
			return true;
		else if(type.equals("skip")||type.equals("reverse")||type.equals("+2")||type.equals("+4")
				||type.equals("wild"))
			return true;
		return false;
	}
	public int points() {
		if(!special())
			return Integer.parseInt(type);
		else if(type.equals("skip")||type.equals("reverse")||type.equals("+2"))
				return 20;
		else
			return 50;
	}
	public Image getImage() {
		return image;
	}
}
