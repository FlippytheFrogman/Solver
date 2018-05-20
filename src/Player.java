import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//import Board.DIRECTION;

public class Player {
	Brd b = new Brd(new Hashtable<Long, BrdData>());
	Tiles tiles = new Tiles("");
	Dictionary dictionary = new Dictionary();
	Vocabulary vocab = new Vocabulary("/Users/sheehy/Documents/workspace/Solver/enable1.txt");

	public Player() {
		// TODO Auto-generated constructor stub
		//Get tiles
		//View Board
		//Reset tiles
		//Get a word to try
		//Look for words on first axis, then second
		//Look for crosswords
		//Calculate Points
		//Add Solution
		//View Solutions
	}

	public void takeTurn() {
		long startTime = System.nanoTime();
		b.setBoard();
		b.setLocationValues();
		b.printBoard();
		b.printAttrBoard();
		String word = "";
		// Creating a Hashtable

		List<Solution> sl = Collections.synchronizedList(new ArrayList<Solution>());

		ThreadPoolExecutor tpe = new ThreadPoolExecutor(
				250, 300, 50000L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>( ));
		Collection<Future<?>> futures = new LinkedList<Future<?>>();

		while(word != null) {
			try {
				word = vocab.getWord();
			} catch (IOException e) {
				System.out.println("Could not get word");
				e.printStackTrace();
			}
			//StdOut.println("Looking for word: " + word);
			if (word != null) {
				Tiles tiles2 = new Tiles(tiles);
				boolean single = true;
				if (single) {
					sl.addAll(b.lookFor(word, tiles2));
				}
				else {
					Runner runner = new Runner(b, new String(word), tiles2, sl);
					futures.add(tpe.submit(runner));
				}
			}
		}
		System.out.println("Waiting to shutdown.");
		try {
			for (Future<?> future:futures) {
				future.get();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Shutdown.");

		Collections.sort(sl);
		int i = 0;
		for(Solution s: sl) {
			System.out.println(s.mPoints + " " + s.mDirection + " " + new String(s.mWord) + "\t" + s.mX + " " + s.mY + " " + s.mCrosswords);
			if (i++ > 20) break;
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		System.out.println("Duration in ms:" + duration / 1000000);
		//TODO  Release dictionary
	}

	private void sleep(int i) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player p = new Player();
		p.tiles.setTiles("oectoo ");
		p.takeTurn();
		System.out.println("*** END ***");
	}

}
