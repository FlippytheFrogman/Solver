import java.util.ListIterator;

public interface BoardIterator<E> extends ListIterator<E> {
	public void column();
	public void row();
	public void goTo(int row, int col);
	public void goToRow(int row);
	public void goToCol(int col);
	public int getCurrX();
	public int getCurrY();
	public Square current();
	public void swap();
	public boolean getAcross();
	// reset to null upon intervening remove() or add()
	//public BoardIterator<?> iteratorCopy(DoublyLinkedList<Square> it);
}
