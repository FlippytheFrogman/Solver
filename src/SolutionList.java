
public class SolutionList implements Comparable<SolutionList> {

	public SolutionList() {
	}
	
	int mValue;
	int mX;	
	int mY;
	Board.DIRECTION mDirection;
	String mCandidateStr;
	
	public void addSolution(int value, int row, int column, Board.DIRECTION direction, String word) {
		mValue = -value;
		mX = row;
		mY = column;
		mDirection = direction;
		mCandidateStr = word;
	}

	public String toString() {
		return new String(-mValue + "\t" +  
				"Row: " + mX +
				"\tCol: " + mY +
				"\t " + mDirection +
				 "\t" + mCandidateStr);
	}
	
	public String getString() {
		return mCandidateStr;
	}
	
	@Override
	public int compareTo(SolutionList o) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    if (this == o) return EQUAL;
	    if (this.mValue < o.mValue) return BEFORE;
	    if (this.mValue > o.mValue) return AFTER;
	    return EQUAL;
	}
}
