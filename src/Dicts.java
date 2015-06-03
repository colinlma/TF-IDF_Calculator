import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


public class Dicts {
	/*
	 * This class is used to hold all of the necessary dicts
	 * to create our modified TF-IDF
	 */
	int No_Files = 49996;
	
	//term:freq
	Map<String, Integer> termFreq = new HashMap<String,Integer>();
	//{term : {doc:termFrequency}}
	Map<String, Map<String,Integer>> invertedIndex =new HashMap<String,Map<String,Integer>>();		
	//term : # of occurrence in documents
	Map<String, Integer> termInDocFreq = new HashMap<String, Integer>();		
	//mapsdocId to docURL
	Map<String, String> docIDToURL =new HashMap<String,String>();		
	//term : docsContainingTerminTitle
	Map<String, List<String>> terminTitle =new HashMap<String,List<String>>();
	//docId : wordsinDoc
	Map<String, Integer> wordsInDoc = new HashMap<String, Integer>();
	//term : {doc:tf_idfScore} 
	//look at lec 10 for example
	Map<String, Map<String,Double>> tfIDF =new HashMap<String,Map<String,Double>>();
	BufferedReader br;
	Gson gson;
	List<String> stopWordsList = new ArrayList<String>();

	
	public Dicts(String subFolderOfDocuments) throws IOException{
		stopWordsList = stopWords.initstopWords();
		String currentDir = System.getProperty("user.dir");
		File dir=new File(currentDir + subFolderOfDocuments);
		int x = 0;
		File[] listofFiles = dir.listFiles();
		for(File file : listofFiles){
			x++;
			br = new BufferedReader(new FileReader(file));
			gson = new Gson();
			parsedFile documentFile = gson.fromJson(br, parsedFile.class);
			docIDToURL.put(documentFile.id, documentFile._id);
	
			termFreq = new HashMap<String,Integer>();
			termFreq = documentFile.makeTermFreq(stopWordsList);//create term:freq dict for the single document
			initTitleDict(termFreq,documentFile);
			updateTermInDoc(termFreq);
			updateInvertedIndex(documentFile.id, termFreq);
			wordsInDoc.put(documentFile.getId(), documentFile.wordAmount);//# of total words in a doc.
		}
		initTFIDF();
	}
	public Map<String, Map<String,Double>> getTFIDF(){
		return tfIDF;
	}
	public Map<String, String> getdocIDToURL(){
		return docIDToURL;
	}
	private void initTFIDF(){
		/*
		 * intializes TF-IDF dict
		 */
		for(String token: invertedIndex.keySet()){
			for(String docID: invertedIndex.get(token).keySet()){
				double tfIDFScore;
				tfIDFScore = (invertedIndex.get(token)).get(docID) 
						/wordsInDoc.get(docID) *  Math.log(No_Files / termInDocFreq.get(token));
				if(tfIDF.containsKey(token)){
					if(terminTitle.get(token).contains(docID)){
						tfIDFScore = 1.4 * (invertedIndex.get(token)).get(docID) 
								/wordsInDoc.get(docID) * Math.log(No_Files / termInDocFreq.get(token));
						System.out.println(tfIDFScore);
						tfIDF.get(token).put(docID, tfIDFScore);
					}
				}
				else{
					Map<String,Double> temp = new HashMap<String,Double>();
					if(terminTitle.get(token).contains(docID)){
						tfIDFScore = 1.4 * (invertedIndex.get(token)).get(docID) 
								/wordsInDoc.get(docID)
								*  Math.log(No_Files / termInDocFreq.get(token));
						temp.put(docID, tfIDFScore);
						tfIDF.put(token, temp);
					}
					else{
						temp.put(docID, tfIDFScore);
						tfIDF.put(token, temp);
						}
					}
				}
			}
	}
	private void updateInvertedIndex(String documentFileid, Map<String, Integer> termFreq){
		for(String token: termFreq.keySet()){
			if(invertedIndex.containsKey(token)){
				invertedIndex.get(token).put(documentFileid, termFreq.get(token));
			}
			else{
				Map<String,Integer> postings = new HashMap<String,Integer>();
				postings.put(documentFileid, termFreq.get(token));
				invertedIndex.put(token, postings);
			}
	
		}
	}
	private void updateTermInDoc(Map<String, Integer> termFreq){
		for(String token: termFreq.keySet()){
			if(termInDocFreq.containsKey(token)){
				termInDocFreq.put(token, termInDocFreq.get(token)+1);//this
			}
			else{
				termInDocFreq.put(token,1);
			}
		}
	}
	public void initTitleDict(Map<String, Integer> termFreq, parsedFile documentFile ){
		/*initializes the Title Dictionary, mapping a term to the list of documents whose title
		 * contain that term*/
		for(String token: termFreq.keySet()){
			if(terminTitle.containsKey(token)){
				terminTitle.get(token).add(documentFile.id);
			}
			else{
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(documentFile.id);
				terminTitle.put(token, temp);
			}
		}
	}
	public void writeTFIDFToFile(String filepath) throws FileNotFoundException, UnsupportedEncodingException{
		/*
		 * Writes the provided tfIDF out to the specified filepath 
		 */
		PrintWriter writer = new PrintWriter("tfIDF_sample.txt", "UTF-8");
		for(String token : tfIDF.keySet()){
			writer.print(token + ":{");
			for(String docID: tfIDF.get(token).keySet()){
				writer.print(docID + ":" + tfIDF.get(token).get(docID) + ", ");	
			}
			writer.println("}");
		}
		writer.close();
	}
}
