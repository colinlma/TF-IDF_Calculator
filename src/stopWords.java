import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class stopWords {
	public static List<String> initstopWords() throws IOException{
		String currentLine;
		String stopWord;
		List<String> stopWords = new ArrayList<String>();
		BufferedReader swBR = new BufferedReader(new FileReader("stopwords.txt"));
		while ((currentLine = swBR.readLine()) != null) {
			stopWord = currentLine.trim();
			stopWords.add(stopWord);
		}
		try {
			if (swBR != null)swBR.close();
		} catch (IOException ex) {
			System.out.println("ERROR");
			ex.printStackTrace();
		}
		return stopWords;
	}
}
