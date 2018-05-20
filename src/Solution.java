import java.util.ArrayList;

public class Solution implements Comparable<Solution> {

	int mX;
	int mY;
	char[] mWord;
	DIRECTION mDirection;
	int mPoints;
	ArrayList<String> mCrosswords;

	public Solution(int x, int y, char[] word, int points, DIRECTION direction, ArrayList<String> crosswords) {
		// TODO Auto-generated constructor stub
		mWord = word; 
		mX = x;
		mY = y;
		mDirection = direction;
		mPoints = points;
		mCrosswords = crosswords;
	}

	public String toString() {
		return new String( "Row: " + mX +
				"\tCol: " + mY +
				"\t " + mDirection +
				"\t " + mPoints +
				"\t" + mWord.toString() + 
				"\t" + mCrosswords);
	}

	public String getString() {
		return mWord.toString();
	}

	@Override
	public int compareTo(Solution o) {
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;
			if (this == o) return EQUAL;
			if (this.mPoints > o.mPoints) return BEFORE;
			else if (this.mPoints < o.mPoints) return AFTER;
			else return EQUAL;
			//if (this.mX > o.mX) return BEFORE;
			//if (this.mX > o.mX) return AFTER;

			//return EQUAL;
	}

}
