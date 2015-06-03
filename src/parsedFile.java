import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class parsedFile {
	/*
	 * this class is needed to parse the provided JSON files
	 */
	String text;
	String _id;
	String title;
	String id;
	String html;
	int wordAmount;

	public Map<String, Integer> getTitleTokens() throws IOException {
		/*
		 * Gets the tokens of the title
		 */
		if(title == null){
			return null;
		}
		String alphaNumericFilter = "[^a-zA-z0-9]+";
		ArrayList<String> tokenList = new ArrayList<String>();
			String[] words = title.split(alphaNumericFilter);
			System.out.println(words.length);
			for(String word: words){
				System.out.println(id + " : " + word);
				if(word.compareTo("") != 0){
					word = word.toLowerCase();
					tokenList.add(word);
				}
			}
		Map<String, Integer> docTitleTokens = new HashMap<String,Integer>();
		for(String token : tokenList){
			if(docTitleTokens.containsKey(token)){
				docTitleTokens.put(token, docTitleTokens.get(token)+1);
			}
			else{
				docTitleTokens.put(token, 1);
			}
		}
		return docTitleTokens;
	}
	public Map<String,Integer>  makeTermFreq(List<String> stopWordsList){
		/*
		 * filters out the stop words from the list to minimize unnecessary terms
		 * and maximize space
		 */
		ArrayList<String> currentFileTokens =  new ArrayList<String>(Arrays.asList(this.getText().split("\\s+")));
		Map<String, Integer> termFreq = new HashMap<String,Integer>();
		this.wordAmount = 0;
		for(String token : currentFileTokens){
			token = token.toLowerCase().trim();
			if(token.length() > 1 && !stopWordsList.contains(token)){
				if(termFreq.containsKey(token)){
					termFreq.put(token,termFreq.get(token)+1);
				}
				else{
					termFreq.put(token, 1);
				}
				this.wordAmount+=1;
			}
		}
		return termFreq;
	}
	public void setHtml(String html){
		this.html = html;
	}
	public String getHtml(){
		return html;
	}
	public void setText(String text){
		this.text = text;
	}
	public void set_id(String id){
		this._id = id;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getText(){
		return text;
	}
	public String get_id(){
		return _id;
	}
	public String getTitle(){
		return title;
	}
	public String getId(){
		return id;
	}
	
}
