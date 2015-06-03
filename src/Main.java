/*Colin Ma 71323642
Gaby Guitierrez 66192133
Marwan Nazanda 69803074
*/

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String [] args) throws IOException{
		Map<String, Map<String,Double>> tfIDF =new HashMap<String,Map<String,Double>>();
		String subFolderOfDocuments = "/FileDump";//folder containing JSON files
		Dicts dicts = new Dicts(subFolderOfDocuments);
		Map<String, String> docIDToURL =new HashMap<String,String>(); 
		docIDToURL = dicts.getdocIDToURL(); //needed for part 2
		tfIDF = dicts.getTFIDF(); //needed for part 2
		String filepath = "tfIDF_sample.txt";
		dicts.writeTFIDFToFile(filepath); //write the tfIDF to a file
	}
}
