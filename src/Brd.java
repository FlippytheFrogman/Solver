import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

class Brd {

	private int MAX_DIMENSION = 15;
	DoublyLinkedList<Square> dll = new DoublyLinkedList<>(MAX_DIMENSION);
	public Dictionary verifyWord;
	Hashtable<Long, BrdData> hashtable;

	public Brd(Hashtable<Long, BrdData> hashtable) {
		this.hashtable = hashtable;
		verifyWord = new Dictionary();

		//Create a board from DoublyLinkedList.
		final int MAX_DIMENSION = 15;

		// Set up board
		// Add squares
		for (int i = 0; i < MAX_DIMENSION; i++) {
			dll.setRow(i);
			for (int j = 0; j < MAX_DIMENSION; j++) {
				dll.setCol(j);
				dll.add(new Square());
			}
		}

		System.out.println(MAX_DIMENSION * MAX_DIMENSION  + " squares added.");

	}

	public boolean setBoard() {
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
			String line = "";
			int row = 0;
			BoardIterator<Square> it = dll.iterator();
			it.column();
			//setDirection(DIRECTION.COLUMN);

			while ((line = reader.readLine()) != null) {
				//System.out.println("row:" + row);
				it.goToCol(0);
				it.goToRow(row++);
				//System.out.println("new line: " + line);
				int i = 0;

				for (char c : line.toCharArray()) {
					//if (c != ' ') {
					//System.out.println("i: " + i + " c:" + c);

					if (i == 12) {
						System.out.println("i: " + i);
					}
					i++;
					Square sq = it.next();
					if (c != ' ') sq.setLetter(c);
					//}
				}
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

	public void printBoard() {
		System.out.print(".");
		for (int col = 0; col < MAX_DIMENSION; col++) {
			System.out.print("." + Integer.toHexString(col));
		}
		System.out.println();
		for (int row = 0; row < MAX_DIMENSION; row++) {
			BoardIterator<Square> it = dll.iterator();
			it.column();
			System.out.print(Integer.toHexString(row));
			it.goToRow(row);

			for (int col = 0; col < MAX_DIMENSION; col++) {
				System.out.print("." + getCurrentLetter(it));
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printAttrBoard() {
		System.out.print(".");
		for (int col = 0; col < MAX_DIMENSION; col++) {
			System.out.print("." + Integer.toHexString(col));
		}
		System.out.println();
		for (int row = 0; row < MAX_DIMENSION; row++) {
			BoardIterator<Square> it = dll.iterator();
			it.column();
			System.out.print(Integer.toHexString(row));
			it.goToRow(row);

			for (int col = 0; col < MAX_DIMENSION; col++) {
				System.out.print("." + it.next().mType.toString());
			}
			System.out.println();
		}
		System.out.println();
	}

	private Character getCurrentLetter(BoardIterator<Square> it) {
		Character c = it.next().getLetter();
		if (c == null) return ' ';
		else return c;
	}

	void setLocationValues() {
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

	private void setSymmetrix(int x, int y, SQUARETYPE eType) {
		BoardIterator<Square> it = dll.iterator();
		it.goTo(x, y);	it.next().setAttribute(eType);
		it.goTo(MAX_DIMENSION - x - 1, y);	it.next().setAttribute(eType);
		it.goTo(x, MAX_DIMENSION - y - 1);	it.next().setAttribute(eType);
		it.goTo(MAX_DIMENSION - x - 1, MAX_DIMENSION - y - 1);	it.next().setAttribute(eType);
	}

	public ArrayList<Solution> lookFor(String word, Tiles tiles) {
		hashtable.put(new Long(Thread.currentThread().getId()), new BrdData());
		BrdData directionData = hashtable.get(new Long(Thread.currentThread().getId()));
		//TODO need to do rows and columns.
		ArrayList<Solution> pq = new ArrayList<>();
		for (DIRECTION direction : DIRECTION.values()) {
			BoardIterator<Square> it = dll.iterator();
			directionData.mDirection = direction;
			//System.out.println("*** lookFor direction " + mDirection + " ***");
			setDirection(it);
			while(it.hasNext()) { // More columns
				char[] wordChar = word.toCharArray();
				pq.addAll(applyWordToAxis(it, wordChar, tiles));
				if (pq.size() > 0) {
					//System.out.println(pq);
				}
				setDirection(it);
				if (it.hasNext()) {
					it.next();
				}
			}		
		}
		if (pq.size() > 0) {
		//System.out.println(pq);
		}
		return pq;
	}

	private void setDirection(BoardIterator<Square> it) {
		BrdData directionData = hashtable.get(new Long(Thread.currentThread().getId()));
		if (directionData.mDirection == DIRECTION.COL)	it.column();
		else it.row();
	}

	private void setOrthagonalDirection(BoardIterator<Square> it) {
		BrdData directionData = hashtable.get(new Long(Thread.currentThread().getId()));
		if (directionData.mDirection == DIRECTION.COL) it.row();
		else it.column();
	}

	private ArrayList<Solution> applyWordToAxis(
			BoardIterator<Square> it, 
			char[] word, 
			final Tiles tiles) {
		ArrayList<Solution> pq = new ArrayList<Solution>();
		int iterate = MAX_DIMENSION - word.length + 1;
		int x = it.getCurrX();
		int y = it.getCurrY();
		BrdData directionData = hashtable.get(new Long(Thread.currentThread().getId()));
		for(int startRow = 0; startRow <  iterate ; startRow++) {
			tiles.reset();
			if (directionData.mDirection == DIRECTION.COL) {
				it.row();
				it.goToRow(startRow);
			}
			else {
				it.column();
				it.goToCol(startRow);
			}
			Solution s = checkLocation(it, word, tiles);
			it.goTo(y, x);
			if (s != null) pq.add(s);


		}
		return pq;
	}

	//TODO Add a solution when processing the column.
	private Solution checkLocation(final BoardIterator<Square> it, 
			char[] word, 
			final Tiles tiles) {
	
		int x = it.getCurrX();
		int y = it.getCurrY();

		if (!isDelimitedBefore(it)) {
			return null;
		}
		boolean anchored = false;
					
		for(int posInWord = 0; posInWord < word.length; posInWord++) {
			Character ch = word[posInWord];
			Square sq = it.next();
			Character squareLetter = sq.getLetter();

			if (squareLetter == null) {
				char retC = tiles.removeTile(ch);  // If character is '!' no tile.
				if (retC == '!') break;
			}
			else if (squareLetter == ch) {
				anchored = true;
			}
			else {
				return null;  // Didn't have a tile in word.
			}

			if (posInWord == ( word.length - 1) ) {
				ArrayList<String> crossWords = new ArrayList<>();
				if (!isDelimitedAfter(it)) return null;  // Didn't have needed space.
				it.goTo(y,  x);
				for (int i = 0; i < word.length; i++) {
					int x1 = it.getCurrX();
					String crossWord = collectAdjacentCharacters(it, word[i]);
					if (x1 != it.getCurrX()) 
					{
						System.out.println("x1: " + x1 + " now: " + it.getCurrX());
					}
					if (it.hasNext()) it.next();
					if (crossWord.length() > 1) {
						if (!verifyWord.isWord(crossWord)) {
							return null; // Not a valid word
						}
						crossWords.add(crossWord);
						//System.out.println("Word: " + new String(word) + " crossWord: " + crossWord);
						
						anchored = true;
					}
				}
				if (anchored) {
					// Use the actual tiles used to calculate value.
					// This handles blank tile 0 point issue.
					it.goTo(y,x);
					BrdData directionData = hashtable.get(new Long(Thread.currentThread().getId()));

					return new Solution(x, y, word, sumPoints(word, it), directionData.mDirection, crossWords);
				}

			}
		}
		return null;
	}

	private int sumPoints(char[] word, BoardIterator<Square> it) {
		int tot = 0;
		Points pts = new Points();
		Square sq;
		for(int i = 0; i < word.length; i++) {
			sq = it.current();
			//System.out.println(sq.getLetter());
			pts.addCharacter(sq, word[i]);
			if (sq.getLetter() == null) {
				int temp = sumAdjacentValues(word[i], it);
				//System.out.println("Adjacent: " + temp);
				tot += temp;
			}
			if (it.hasNext()) it.next();
		}
		tot += pts.wordPoints();
		return tot;
	}

	private int sumAdjacentValues(Character ch, BoardIterator<Square> it) {
		int x = it.getCurrX();
		int y = it.getCurrY();		String crossWord = "";
		Points pts = new Points();
		it.swap();
		Square sq = it.current();
		pts.addCharacter(sq, ch);  // SQ will be null, but 'ch' will be counted
		crossWord = Character.toString(ch);

		Character ach;
		if (it.hasPrevious()) it.previous();  // position to previous
		while(it.hasPrevious()) {
			sq = it.previous();
			if (sq == null) {
				System.out.println("Ouch");
			}
			ach = sq.getLetter();
			if (ach == null) break;
			crossWord = Character.toString(ach) + crossWord;
			pts.addCharacter(sq, ach);
		}

		it.goTo(y, x);
		if (it.hasNext()) it.next();
		while(it.hasNext()) {
			sq = it.next();
			ach = sq.getLetter();
			if (ach == null) break;
			crossWord = crossWord + Character.toString(ach) ;
			pts.addCharacter(sq, ch);
		}
		it.goTo(y, x);

		//System.out.println("Sum value: " + crossWord);
		it.swap();
		
		return pts.wordPoints();
	}

	private String collectAdjacentCharacters(BoardIterator<Square> it, Character ch) {
		String crossWord = "";
		it.swap();
		int x = it.getCurrX();
		int y = it.getCurrY();

		crossWord += getPre(it);
		it.goTo(y, x);

		crossWord += Character.toString(ch);
		it.goTo(y, x);
		crossWord += getPost(it);
		it.goTo(y, x);

		it.swap();
		return crossWord;
	}

	private String getPre(final BoardIterator<Square> it) {
		String str = "";
		int x = it.getCurrX();
		int y = it.getCurrY();
		if (it.hasPrevious()) it.previous();
		while(it.hasPrevious()) {
			Square sq = it.previous();
			Character ach = sq.getLetter();
			if (ach == null) break;
			str = ach + str;	
		}
		it.goTo(y, x);
		return str;
	}

	private String getPost(final BoardIterator<Square> it) {
		String str = "";
		int x = it.getCurrX();
		int y = it.getCurrY();
		if (it.hasNext()) it.next();
		while(it.hasNext()) {
			Square sq = it.next();
			if (sq == null) {
				//System.out.println(it.getCurrX());
			}
			Character ach = sq.getLetter();
			if (ach == null) break;
			str += ach;	
		}
		it.goTo(y, x);
		return str;
	}

	private boolean isDelimitedAfter(BoardIterator<Square> it) {
		if (!it.hasNext()) return true;
		Square sq = it.next();
		if (sq.getLetter() == null)	{
			it.previous();
			return true;
		}
		it.previous();
		return false;
	}

	private boolean isDelimitedBefore(final BoardIterator<Square> it) {
		if (!it.hasPrevious()) return true;
		it.previous();
		Square sq = it.current();
		if (sq.getLetter() == null) {
			it.next();
			return true;
		}
		it.next();
		return false;
	}

	public static void main(String[] args) {
		Hashtable<Long, BrdData> hashtable = null;
		Brd b = new Brd(hashtable);
		b.setBoard();
		b.setLocationValues();
		b.printBoard();
		b.printAttrBoard();
		System.out.println();
	}
}
