
public class Tiles {

	char[] tiles; 
	char[] origTiles;


	public Tiles(String strTiles) {
		tiles = origTiles = strTiles.toCharArray();
	}

	public Tiles(Tiles tiles2) {
		this(new String(tiles2.origTiles));
	}

	public void setTiles(String strTiles) {
		tiles = origTiles = strTiles.toCharArray();		
	}

	public void reset() {
		tiles = origTiles;
	}

	public char removeTile(char ch) {
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] == ch || tiles[i] == ' ') {
				char act = tiles[i];
				char[] newTiles = new char[tiles.length - 1];

				for (int j = 0; j < i; j++) {
					newTiles[j] = tiles[j];
				}
				for (int j = i + 1; j < tiles.length; j++) {
					newTiles[j - 1] = tiles[j];
				}
				tiles = newTiles;
				return act;
			}
		}
		return '!'; //TODO
	}

	public static int getPointValue(char ch) {
		if (Character.isUpperCase(ch)) {
			return 0;
		}        
		switch (ch) {
		case 'a':
			return 1;
		case 'b':
			return 4;
		case 'c':
			return 4;
		case 'd':
			return 2;
		case 'e':
			return 1;
		case 'f':
			return 4;
		case 'g':
			return 3;
		case 'h':
			return 3;
		case 'i':
			return 1;
		case 'j':
			return 10;
		case 'k':
			return 5;
		case 'l':
			return 2;
		case 'm':
			return 4;
		case 'n':
			return 2;
		case 'o':
			return 1;
		case 'p':
			return 4;
		case 'q':
			return 10;
		case 'r':
			return 1;
		case 's':
			return 1;
		case 't':
			return 1;
		case 'u':
			return 2;
		case 'v':
			return 5;
		case 'w':
			return 4;
		case 'x':
			return 8;
		case 'y':
			return 3;
		case 'z':
			return 10;
		case ' ':
			return 0;
		default:
			System.out.println(ch);
			throw new IllegalArgumentException();
		}			
	}

	public static void main(String arg[]) {
		System.out.println(Tiles.getPointValue('z'));
	}

}
