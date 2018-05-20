/******************************************************************************
 *  Compilation:  javac DoublyLinkedList.java
 *  Execution:    java DoublyLinkedList
 *  Dependencies: System.out.java
 *
 *  A list implemented with a doubly linked list. The elements are stored
 *  (and iterated over) in the same order that they are inserted.
 * 
 *  % java DoublyLinkedList 10
 *  10 random integers between 0 and 99
 *  24 65 2 39 86 24 50 47 13 4 
 *
 ******************************************************************************/

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<Item> implements Iterable<Item> {

	int mDimension;

	ArrayList<Node> preXNode = new ArrayList<>();
	ArrayList<Node> postXNode = new ArrayList<>();
	ArrayList<Node> preYNode = new ArrayList<>();
	ArrayList<Node> postYNode = new ArrayList<>();

	private int mRow = 0;
	private int mCol = 0;

	// linked list node helper data type
	private class Node {
		private Item item;
		private Node next;
		private Node prev;
		private Node down;
		private Node up;
	}
	
	public int getRow() {
		return mRow;
	}

	public int getCol() {
		return mCol;
	}

	public void setRow(int row) {
		mRow = row;
	}

	public void setCol(int col) {
		mCol = col;
	}

	public DoublyLinkedList(int dimension) {
		mDimension = dimension;
		for (int i = 0; i < mDimension; i++) {
			createNewRow();
			newCol();
		}
		for (int i = 0; i < mDimension - 1; i++) {
			linkYNodes(i, preYNode);
			linkYNodes(i, postYNode);

		}
		for (int i = 0; i < mDimension - 1; i++) {
			linkXNodes(i, preXNode);
			linkXNodes(i, postXNode);

		}
		
	}

	/**
	 * @param i
	 */
	private void linkXNodes(int i, ArrayList<Node> XNode) {
		Node u = XNode.get(i);
		Node d = XNode.get(i + 1);
		u.down = d;
		d.up = u;
	}

	/**
	 * @param i
	 */
	private void linkYNodes(int i, ArrayList<Node> YNode) {
		Node l = YNode.get(i);
		Node r = YNode.get(i + 1);
		l.next = r;
		r.prev = l;
	}
	
	public int getDimension() {
		return mDimension;
	}

	private void newCol() {
		Node preY;        // sentinel before first item
		Node postY;       // sentinel after last item

		preY  = new Node();
		postY = new Node();
		preY.down = postY;
		postY.up = preY;
		preYNode.add(preY);
		postYNode.add(postY);
	}

	private void createNewRow() {
		//mCol = 0;
		Node preX;        // sentinel before first item
		Node postX;       // sentinel after last item

		preX  = new Node();
		postX = new Node();
		preX.next = postX;
		postX.prev = preX;
		preXNode.add(preX);
		postXNode.add(postX);
		//mRow++;
	}

	// add the item to the list
	public void add(Square item) {
		Node postX = postXNode.get(mRow); ;
		Node postY = postYNode.get(mCol);

		Node lastX = postX.prev;
		Node lastY = postY.up;

		item.x(mCol);
		item.y(mRow);
		
		Node x = new Node();
		x.item = (Item) item;  //TODO

		x.next = postX;
		x.down = postY;

		x.prev = lastX;
		x.up = lastY;

		postX.prev = x;
		postY.up = x;
		lastX.next = x;
		lastY.down = x;
	}

	public BoardIterator<Item> iterator()  
	{ 
		return new DoublyLinkedListIterator(); 
	}

	// assumes no calls to DoublyLinkedList.add() during iteration
	private class DoublyLinkedListIterator implements BoardIterator<Item> {
		public Node current      = preXNode.get(0).next;  // the node that is returned by next()
		private Node lastAccessed = null;      // the last node to be returned by prev() or next()

		// reset to null upon intervening remove() or add()
		private boolean across = false;
		private int currX = 0;
		private int currY = 0;
		
		public boolean getAcross() {
			return across;
		}
		
		public DoublyLinkedListIterator() {
			current      = preXNode.get(0).next;  // the node that is returned by next()
			lastAccessed = null;      			  // the last node to be returned by prev() or next()
			// reset to null upon intervening remove() or add()
			across = false;
			currX = 0;
			currY = 0;
		}

		//public DoublyLinkedListIterator(BoardIterator<Square> boardIterator) {
		//	current = boardIterator.current;
		//	lastAccessed = boardIterator.lastAccessed; // the last node to be returned by prev() or next()
		//	across = boardIterator.across;
		//	currX = boardIterator.currX;
		//	currY = boardIterator.currY;
		//}

		public int getCurrX() {
			return currX;
		}
		
		public int getCurrY() {
			return currY;
		}
				
		public Node getCurrent()      { 
			return current;
		}

		public void setCurrent(Node x)      { 
			current = x;
		}

		public boolean hasNext()      { 
			if (across) return currX < mDimension; // mRow;
			else return currY < mDimension;        // mCol;
		}

		public boolean hasPrevious()  {
			if (across) return currX > 0;
			else return currY > 0;
		}

		public void row() {
			across = false;
		}

		public void column() {
			across = true;
		}
		
		public void swap() {
			across = (across)? false:true;
		}

		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			lastAccessed = current;
			Item item = current.item;
			if (across) {
				if (current.next == null) {
					//System.out.print("Stop in Next");
				}
				current = current.next; 
				currX++;
				//System.out.println("currX: " + currX);
			}
			else {
				if (current.down == null) {
					System.out.print("Stop in Next");
				}
				current = current.down; 
				currY++;
				//System.out.println("currY: " + currY);
			}
			return item;
		}

		public Square current() {
			Square item = (Square) current.item;
			return item;
		}

		public Item previous() {
			if (!hasPrevious()) throw new NoSuchElementException();
			//lastAccessed = current; //
			//Item item = current.item; //
			Item item = current.item;

			if (across) {
				if (current.prev == null) {
					System.out.println("Stop in previous");
				}
				current = current.prev;
				currX--;
				//System.out.println("currX: " + currX);
			}
			else {
				if (current.up == null) {
					System.out.print("Stop in Next");
				}
				
				current = current.up;
				currY--;
				//System.out.println("currY: " + currY);
			}
			lastAccessed = current;
			return item;
			//return item;
		}

		// replace the item of the element that was last accessed by next() or previous()
		// condition: no calls to remove() or add() after last call to next() or previous()
		public void set(Item item) {
			if (lastAccessed == null) throw new IllegalStateException();
			lastAccessed.item = item;
		}

		// remove the element that was last accessed by next() or previous()
		// condition: no calls to remove() or add() after last call to next() or previous()
		public void remove() { 
			if (lastAccessed == null) throw new IllegalStateException();
			Node x = lastAccessed.prev;
			Node y = lastAccessed.next;
			x.next = y;
			y.prev = x;
			if (current == lastAccessed)
				current = y;
			else
				lastAccessed = null;
		}

		// add element to list 
		public void add(Item item) {
			Node x = current.prev;
			Node y = new Node();
			Node z = current;
			y.item = item;
			x.next = y;
			y.next = z;
			z.prev = y;
			y.prev = x;
			mCol++;
			lastAccessed = null;
		}
		
		public void goToCol(int col) {
			//System.out.println("col" + col);
			if (across) goTo(col);
			else {
				column();
				goTo(col);
				row();
			}
		}

		public void goToRow(int row) {
			if (!across) goTo(row);
			else {
				row();
				goTo(row);
				column();
			}
		}
		
		public void goTo(int col, int row) {
			goToRow(col);
			goToCol(row);
		}
		
		private void goTo(int location) {
			//TODO fix throw
			if (location < 0 || location > mDimension) throw new NoSuchElementException();
			int navigate = (across) ? location - currX : location - currY;
			if (navigate < 0) {
				if (navigate == -15) {
					//System.out.println("stop in goto");
				}
				back(navigate);
			}
			else
				forward(navigate);
		}
		/**
		 * @param navigate
		 */
		private void back(int navigate) {
			//System.out.println("goBack: " + navigate);
			for (int i = 0; i > navigate; i--) {
				previous();
			}
		}

		private void forward(int navigate) {
			for (int i = 0; i < navigate; i++) {
				next();
			}
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			throw new NoSuchElementException();
		}

		@Override
		public int previousIndex() {
			throw new NoSuchElementException();
		}






	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Item item : this)
			s.append(item + " ");
		return s.toString();
	}

	// a test client
	public static void main(String[] args) {
		final int MAX_DIMENSION = 3;
		DoublyLinkedList<Square> dLL = new DoublyLinkedList<Square>(MAX_DIMENSION);

		// Set up board
		for (int i = 0; i < MAX_DIMENSION; i++) {
			dLL.mRow = i;
			for (int j = 0; j < MAX_DIMENSION; j++) {
				dLL.mCol = j;
				dLL.add(new Square());
			}
		}
		System.out.println(MAX_DIMENSION * MAX_DIMENSION  + " squares added.");

		// Set characters

		System.out.println("-> add character to each element");
		for (int i = 0; i < MAX_DIMENSION; i++) {
			{
				BoardIterator<Square> iterator = dLL.iterator();
				for ( int j = 0; j < i; j++) {
					iterator.next();
				}
				iterator.row(); // Column
				populate(dLL, iterator, i);
				printBackwards(iterator);
			}
		}
		// Print backward
	}

	/**
	 * @param l
	 * @param iterator
	 */
	private static void populate(DoublyLinkedList<Square> l, ListIterator<Square> iterator, int it) {
		Character[] ch = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'};
		int i = 0;
		while (iterator.hasNext()) {
			Square sq = iterator.next();
			if (sq == null) {
				System.out.println("Null Square found.");
			}
			sq.setLetter(ch[(it + i) % l.getDimension()]);
			i++;
		}
	}

	/**
	 * @param iterator
	 */
	private static void printBackwards(ListIterator<Square> iterator) {
		while (iterator.hasPrevious()) {
			Square sq = iterator.previous();
			if (sq == null) {
				System.out.println("Null Square found.");
			}
			//System.out.print(sq.getLetter());
		}
		//System.out.println();
	}

	private static void print(ListIterator<Square> iterator) {
		while (iterator.hasNext()) {
			Square sq = iterator.next();
			//System.out.print(sq.getLetter());
		}
		//System.out.println();
	}
}
