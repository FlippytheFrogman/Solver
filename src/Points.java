public class Points {
	int totPoints = 0;
	int mult = 1;
	int calls = 0;
	int usedTiles = 0;

	public Points() {
	}

	public void addCharacter(Square sq, Character ach) {
		calls++;
		if (sq.getLetter() == ach) {
			reuseSquare(sq);
		}
		else {
			addSquare(sq, ach);
		}
	}

	private void addSquare(Square sq, Character ach) {
		usedTiles++;
		int points = Tiles.getPointValue(ach);
		//System.out.println("addSquare -- letter: " + ach + " points " + points);
		calcPoints(sq, points);
	}

	private void reuseSquare(Square sq) {
		Character ach = sq.getLetter();
		int points = Tiles.getPointValue(ach);
		//int points = sq.getPoints();
		totPoints += points;	
		//System.out.println("reuseSquare -- getLetter: " + sq.getLetter() + " points" + points);
	}

	/**
	 * @param sq
	 * @param points
	 */
	private void calcPoints(Square sq, int points) {
		SQUARETYPE st = sq.getAttribute();
		//System.out.println("calcPoints -- st: " + st.name() + " Points for tile: " + points );
		switch(st) {
		case NORMAL_LETTER: {
			totPoints += (points * 1);
			break;
		}
		case DOUBLE_LETTER: {
			totPoints += (points * 2);
			break;
		}
		case TRIPLE_LETTER: {
			totPoints += (points * 3);
			break;	
		}
		case DOUBLE_WORD: {
			totPoints += (points * 1);
			mult = Math.max(2, mult);
			break;
		}
		case TRIPLE_WORD: {
			totPoints += (points * 1);
			mult = Math.max(3, mult);
			break;
		}
		default: {
			throw new RuntimeException("Unrecognized square type");
		}
		}
	}

	public int wordPoints() {
		if (calls > 1) {
			if (usedTiles == 7) return totPoints * mult + 35;
			return totPoints * mult;
		}
		else return 0;
	}
}
