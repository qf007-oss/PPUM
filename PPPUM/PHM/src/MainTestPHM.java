import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;



/**
 * Example of how to use the PHM algorithm from the source code.
 * @author Philippe Fournier-Viger, 2016
 */
public class MainTestPHM {

	public static void main(String [] arg) throws IOException{

		//String output = ".//output.txt";
		//String outputPath = "PHM/output_B_";
		//String path = "database/afterDB/item_PHM/";

		String outputPath = "database/PHUIs/beforeDB/";

		String path = "database/beforeDB/item_PHM/";

		//String path = "C:\\study\\PPPUM\\PHM\\src\\";
		// =======================
		// EXAMPLE FROM THE ARTICLE : 
		//String input = fileToPath("DB_UtilityPerHUIs.txt");



		                                  // minUtil-minPer-maxPer-minAvg-maxAvg
		//TODO String input = path+"retail.txt"; // 100000-1-5000-1-2000  PHUIs: 1748
		//String input = path+"chess.txt"; // 7000000-1-2000-1-1000  PHUIs: 3356
		//TODO String input = path+"chainstore.txt"; // 10000-1-6000-1-3000  PHUIs: 182
		//TODO String input = path+"kosarak.txt"; // 5000000-1-2000-1-1000  PHUIs: 896, 8s
		//TODO String input = path+"foodmart.txt"; // 3000-1-5000-1-2000  PHUIs: 915
		//TODO String input = path+"mushrooms.txt"; // 5000000-1-2000-1-1000  PHUIs: 220
		//String input = path+"liquor.txt"; // 200000-1-2000-1-1000  PHUIs: 1457

		String filename = "liquor.txt";
		int min_utility = 200000; // 180000
		int minPeriodicity = 1;  // minimum periodicity parameter (a number of transactions)
		int maxPeriodicity = 2000;  // maximum periodicity parameter (a number of transactions)
		int minAveragePeriodicity = 1;  // minimum average periodicity (a number of transactions)
		int maxAveragePeriodicity = 1000;  // maximum average periodicity (a number of transactions)
		// =======================

		String input = path+filename;
		String output = outputPath+filename;
		
		//===== Optional parameters (new, 2017)==//
		// Minimum number of items that patterns should contain
		int minimumLength = 1;
		// Maximum number of items that patterns should contain
		int maximumLength = Integer.MAX_VALUE;
		//===========================//

		// Applying the PHM algorithm
		AlgoPHM algorithm = new AlgoPHM();
		// To disable some optimizations:
		//algorithm.setEnableEUCP(false); 
		//algorithm.setEnableESCP(false);
		
		// set the pattern length constraints
		algorithm.setMinimumLength(minimumLength);
		algorithm.setMaximumLength(maximumLength);
		
		// Run the algorithm
		algorithm.runAlgorithm(input, output, min_utility, 
				minPeriodicity, maxPeriodicity, minAveragePeriodicity, 
				maxAveragePeriodicity);

		System.out.println("database: "+filename);
		System.out.println("minutil: "+min_utility);
		System.out.println("minPer-maxPer-minAvg-maxAvg: "+minPeriodicity+"-"+maxPeriodicity+"-"+minAveragePeriodicity+"-"+maxAveragePeriodicity);
		
		// Print statistics about the execution of the algorithm
		algorithm.printStats();


	}

	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestPHM.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
