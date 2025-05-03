package domain;

import java.util.NoSuchElementException;

public class Deck {

	public int draw() {
		throw new NoSuchElementException("Deck is empty");
	}
}
