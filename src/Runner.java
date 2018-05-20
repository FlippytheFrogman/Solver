import java.util.ArrayList;
import java.util.List;

public class Runner implements Runnable {

	private int number;
	private Brd b;
	private String word;
	private Tiles tiles;
	private List<Solution> sl;
	
	public Runner(int number) {
		this.number = number;
		
	}

	public Runner(Brd b, String word, Tiles tiles, List<Solution> sl) {
		this.b = b;
		this.word = word;
		this.tiles = tiles;
		this.sl = sl;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.printf("%s: %d, %s\n",
		//		Thread.currentThread().getName(),number, word);
		ArrayList<Solution> c = b.lookFor(word, tiles);
		sl.addAll(c);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    for (int i=1; i<=10; i++){
	        Runner calculator=new Runner(i);
	        Thread thread=new Thread(calculator);
	        thread.start();
	      }
	}



}
