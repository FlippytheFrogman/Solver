import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.PriorityQueue;

// Board specification.
public class Board implements Iterator<Square>{

	public enum DIRECTION {
		ROW, COLUMN 
	}

	DIRECTION mCol = DIRECTION.ROW;
	final static int MAX_DIMENSION = 15;
	Dictionary verifyWord;
	Square[][] mBoard = new Square[MAX_DIMENSION][MAX_DIMENSION];

	Board() {
		verifyWord = new Dictionary();
		//TODO: v.releaseConnection();

		//Create Squares
		for (int row = 0; row <  MAX_DIMENSION; row++) {
			for (int col = 0; col <  MAX_DIMENSION; col++) {
				Square square = new Square();
				mBoard[row][col] = square;
			}			
		}
		setLocationValues();	
	}

	private void setSquare(final int x, final int y, final Character c) {
		getSquare(x, y).setLetter(c);
	}

	private void setSymmetrix(int x, int y, SQUARETYPE eType) {
		getSquare(x, y).setAttribute(eType);
		mBoard[MAX_DIMENSION - x - 1][y].setAttribute(eType);
		mBoard[x][MAX_DIMENSION - y -1].setAttribute(eType);
		mBoard[MAX_DIMENSION - x - 1][MAX_DIMENSION - y - 1].setAttribute(eType);
	}

	private void setLocationValues() {
		setSymmetrix(3, 0, SQUARETYPE.TRIPLE_WORD);
		setSymmetrix(6, 0, SQUARETYPE.TRIPLE_LETTER);
		setSymmetrix(2, 1, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(5, 1, SQUARETYPE.DOUBLE_WORD);
		setSymmetrix(1, 2, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(4, 2, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(0, 3, SQUARETYPE.TRIPLE_WORD);
		setSymmetrix(3, 3, SQUARETYPE.TRIPLE_LETTER);
		setSymmetrix(7, 3, SQUARETYPE.DOUBLE_WORD);
		setSymmetrix(2, 4, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(6, 4, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(1, 5, SQUARETYPE.DOUBLE_WORD);
		setSymmetrix(5, 5, SQUARETYPE.TRIPLE_LETTER);
		setSymmetrix(0, 6, SQUARETYPE.TRIPLE_LETTER);
		setSymmetrix(4, 6, SQUARETYPE.DOUBLE_LETTER);
		setSymmetrix(3, 7, SQUARETYPE.DOUBLE_WORD);
	}

	private PriorityQueue<SolutionList> applyWordtoCols(char[] word, 
			Tiles tiles, 
			PriorityQueue<SolutionList> pq) {

		for (DIRECTION op : DIRECTION.values()) {
			setDirection(op);
			for(int col = 0; col < MAX_DIMENSION; col++) {
				pq = applyWordToCol(col, word, tiles, pq);
			}		
		}
		return pq;
	}

	private PriorityQueue<SolutionList> applyWordToCol(
			int col, 
			char[] word, 
			final Tiles tiles, 
			PriorityQueue<SolutionList> pq) {
		int iterate = MAX_DIMENSION - word.length;
		for(int startRow = 0; startRow <= iterate; startRow++) {
			tiles.reset();
			processRow(col, word, tiles, pq, startRow);
		}
		return pq;
	}

	/**
	 * @param col
	 * @param word
	 * @param allTiles
	 * @param pq
	 * @param startRow
	 * @return 
	 * @return
	 */
	private void processRow(int col, char[] word, final Tiles tiles, PriorityQueue<SolutionList> pq,
			int startRow) {
		if (!isDelimitedBefore(startRow, col))
			return;
		boolean anchored = false;
		char[] tileUsed = new char[word.length]; 
		for(int posInWord = 0; posInWord <= word.length - 1; posInWord++) {
			Character ch = word[posInWord];
			int boardRowPos = startRow + posInWord;
			Square sq = getSquare(boardRowPos, col);
			Character squareLetter = sq.getLetter();
			if (squareLetter == null) {
				char retC = tiles.removeTile(ch);
				if (retC == '!') break;
				tileUsed[posInWord] = retC; //TODO
			}
			else if (squareLetter == ch) {
				tileUsed[posInWord] = ch;
				anchored = true;
			}
			else {
				return;  // Didn't have a tile in word.
			}

			if (posInWord == ( word.length - 1) ) {
				if (isDelimitedAfter(boardRowPos, col)) {  // Didn't have needed space.
					for (int i = 0; i < word.length; i++) {
						String crossWord = collectAdjacentCharacters(col, word[i], i + startRow);
						if (crossWord.length() > 1) {
							if (!verifyWord.isWord(crossWord)) {
								return; // Not a valid word
							}
							anchored = true;
						}
					}
					if (anchored) {
						// Use the actual tiles used to calculate value.
						// This handles blank tile 0 point issue.
						addSolution(startRow, col, tileUsed, pq);
					}
				}
			}
		}
		return;
	}

	/**
	 * @param startRow
	 * @param col
	 * @param word
	 * @param pq
	 */
	private void addSolution(int startRow, int col, char[] word, PriorityQueue<SolutionList> pq) {
		int tot = sumPoints(col, word, startRow);

		SolutionList possible = new SolutionList();
		if (getDirection() == DIRECTION.COLUMN) {
			possible.addSolution(tot, col, startRow, getDirection(), new String(word));
		}
		else {
			possible.addSolution(tot, startRow, col, getDirection(), new String(word));
		}
		pq.add(possible);
	}

	/**
	 * @param col
	 * @param word
	 * @param ch
	 * @param startRow
	 * @return
	 */
	private int sumPoints(int col, char[] word, int startRow) {
		int tot = 0;
		Points pts = new Points();

		for(int i = 0; i < word.length; i++) {
			// 
			Square sq = getSquare(startRow + i, col);
			pts.addCharacter(sq, word[i]);
			if (sq.getLetter() == null) {
				tot += sumAdjacentValues(col, word[i], startRow + i);
			}
		}
		tot += pts.wordPoints();
		return tot;
	}

	private int sumAdjacentValues(int col, Character ch, int row) {
		Points pts = new Points();
		Square sq = getSquare(row, col);
		pts.addCharacter(sq, ch);
		Character ach;
		for (int colPos = col - 1; colPos >= 0; colPos--) {
			sq = getSquare(row, colPos);
			ach = sq.getLetter();
			if (ach == null) break;
			pts.addCharacter(sq, ach);
		}
		for (int colPos = col + 1; colPos < MAX_DIMENSION; colPos++) {
			sq = getSquare(row, colPos);
			ach = sq.getLetter();
			if (ach == null) break;
			pts.addCharacter(sq, ch);
		}
		return pts.wordPoints();
	}

	/**
	 * @param col
	 * @param ch
	 * @param location
	 * @return
	 */
	private String collectAdjacentCharacters(int col, Character ch, int location) {
		String crossWord = "";
		for (int colPos = col - 1; colPos >= 0; colPos--) {
			Character ach = getSquare(location, colPos).getLetter();
			if (ach == null) break;
			crossWord = ach + crossWord;
		}
		crossWord = crossWord + Character.toString(ch);
		for (int colPos = col + 1; colPos < MAX_DIMENSION; colPos++) {
			Character ach = getSquare(location, colPos).getLetter();
			if (ach == null) break;
			crossWord = crossWord + ach;
		}
		return crossWord;
	}

	/**
	 * @param location
	 * @param colPos
	 * @return
	 */
	private Square getSquare(int location, int colPos) {
		if (getDirection() == DIRECTION.COLUMN) {
			return mBoard[location][colPos];
		}
		else {
			return mBoard[colPos][location];
		}
	}

	private DIRECTION getDirection() {
		return mCol;
	}

	private void setDirection(DIRECTION dir) {
		mCol = dir;
	}

	/**
	 * @param location
	 * @param asq
	 * @param found
	 * @return
	 */
	private boolean isDelimitedAfter(int row, int col) {
		Square sq;
		if (getDirection() == DIRECTION.COLUMN) {
			col++;
			if (col == MAX_DIMENSION) return true;
		}
		else {
			row++;
			if (row == MAX_DIMENSION) return true;
		}
		sq = getSquare(row, col);
		if (sq.getLetter() == null) {
			return true;
		}
		return false;
	}

	private boolean isDelimitedBefore(int row, int col) {
		Square sq;
		if (row == 0) return true;
		sq = getSquare(row - 1, col);
		if (sq.getLetter() != null) {
			return false;
		}
		return true;
	}

	private boolean setBoard() {
		String file = "Board.txt";
		URL path = ClassLoader.getSystemResource(file);
		if(path==null) {
			System.out.println("The file '" +  file + "' was not found.");
		}
		File f = null;
		try {
			f = new File(path.toURI());
		} catch (URISyntaxException e) {
			System.out.println("URI Syntax Exception: " + path);
			e.printStackTrace();
		}

		try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String aux = "";
			int row = 0;
			while ((aux = reader.readLine()) != null) {
				int col = 0;    
				for (char c : aux.toCharArray()) {
					if (c != ' ') {
						setSquare(row,  col,  c);
					}
					col++;
				}
				row++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("The file '" +  file + "' was not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("The file '" +  file + "' had an IO Exception.");
			e.printStackTrace();
		}

		return false;
	}
	private void printBoard() {
		System.out.print(".");
		for (int col = 0; col < MAX_DIMENSION; col++) {
			System.out.print("." + Integer.toHexString(col));
		}
		System.out.println();
		for (int row = 0; row < MAX_DIMENSION; row++) {
			System.out.print(Integer.toHexString(row));

			for (int col = 0; col < MAX_DIMENSION; col++) {
				System.out.print("." + getSquare(row, col).toString());
			}
			System.out.println();
		}
		System.out.println();
	}

	private void printValuesBoard() {
		System.out.print(".");
		for (int col = 0; col < MAX_DIMENSION; col++) {
			System.out.print("." + Integer.toHexString(col));
		}
		System.out.println();
		for (int row = 0; row < MAX_DIMENSION; row++) {
			System.out.print(Integer.toHexString(row));

			for (int col = 0; col < MAX_DIMENSION; col++) {
				System.out.print("." + getSquare(row, col).mType.toString());
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) throws IOException {
		Board b = new Board();
		b.setBoard();
		b.printBoard();
		b.printValuesBoard();

		PriorityQueue<SolutionList> pq = new PriorityQueue<SolutionList>();
		
		Vocabulary wl = new Vocabulary(new Props().getWordFileName());
		Tiles tiles = new Tiles("bcevsuo");
		try {
			String word = "";
			while(word != null) {
				word = wl.getWord();
				if (word != null) {
					word = word.toLowerCase();
					char[] caWord = word.toCharArray();
					if (word.equals("tinct")) {
						System.out.println(word);
					}
					pq = b.applyWordtoCols(caWord, tiles, pq);
				}
			}
		} catch (IOException e1) {
			System.out.println("Exception collecting words.");
			e1.printStackTrace();
		}

		int limit = Math.min(pq.size(), 20);
		for (int i = 0; i < limit; i++) 
		{
			SolutionList ps = pq.remove();
			System.out.println(ps);
		}
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Square next() {
		// TODO Auto-generated method stub
		return null;
	}

}

