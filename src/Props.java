import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Props {
	private Properties appProps = new Properties();


	public Props()  {
		String rootPath = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource("")
				.getPath();
		String appConfigPath = rootPath 
				+ "app.properties"; 
		System.out.println(appConfigPath);
		FileInputStream in;
		try {
			in = new FileInputStream(appConfigPath);

			appProps.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDbUrl() {
		return appProps.getProperty("db_url");
	}

	public String getDbPassword() {
		return appProps.getProperty("db_password");
	}

	public String getDbUserName() {
		return appProps.getProperty("db_user_name");
	}

	public String getWordFileName() {	
		return appProps.getProperty("word_file", "not found");
	}


	public static void main(String[] args) throws IOException {
		System.out.println(new Props().getWordFileName());
	}	
}