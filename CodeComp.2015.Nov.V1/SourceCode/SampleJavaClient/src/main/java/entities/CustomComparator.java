package entities;

import java.util.Comparator;

public class CustomComparator implements Comparator<Card>{

	public int compare(Card o1, Card o2) {
		return o1.getNumber() - o2.getNumber()  ;
	}

}
