import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Vocabulary {

	BufferedReader br;

	Vocabulary(String dictionary) {
		try{
			//String path was read from file
			System.out.println(dictionary); //file with exactly same visible path exists on disk
			File file = new File(dictionary); 
			System.out.println("File exist: " + file.exists());  //false
			System.out.println("File can be read: " + file.canRead());  //false
			FileInputStream fis = new FileInputStream(file);  // FileNotFoundExeption 

			br = new BufferedReader(new FileReader(dictionary));
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}

	public char[] getNextWord() {
		try {
			String word = "";
			while(word != null) {
				word = this.getWord();
				if (word != null) {
					word = word.toLowerCase();
					char[] caWord = word.toCharArray();
					return caWord;
				}
			}
		} catch (IOException e1) {
			System.out.println("Exception collecting words.");
			e1.printStackTrace();
		}
		return null;		
	}
	
	public  String getWord() throws IOException {
		String word = br.readLine();
		if (word == null) return null;
		while (word.length() < 2) {
			word = br.readLine();
		}
		word = word.toLowerCase();
		//System.out.println(word);
		return word;
	}

}


