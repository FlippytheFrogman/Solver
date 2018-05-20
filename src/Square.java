public class Square {
	
	SQUARETYPE mType;
	Character mLetter = null;
	int mPoints;
	int x;
	int y;
	//Square left = null;
	//Square right = null;
	//Square up = null;
	//Square down = null;
	
	Square() {
		mType = SQUARETYPE.NORMAL_LETTER;
	}

	public void x(int x) {
		this.x = x;		
	}
	
	public void y(int y) {
		this.y = y;		
	}

	public void setAttribute(SQUARETYPE eType) {
		mType = eType;
	}
	
	public SQUARETYPE getAttribute() {
		return mType;
	}
	
	public void setLetter(Character letter) {
		mPoints = Tiles.getPointValue(letter);
		mLetter = Character.toLowerCase(letter.charValue());
		//mLetter = letter;
		}
	
	public int getPoints() {
		return mPoints;
	}
	
	public Character getLetter() {
		return(mLetter);
	}
	
	public String toString() { 
		Character letter = getLetter();
		if (letter == null) letter = ' ';
		if (getPoints() == 0) {
			letter = Character.toUpperCase(letter.charValue());
			return Character.toString(letter);	
		}
		return Character.toString(letter);	
	}
}
