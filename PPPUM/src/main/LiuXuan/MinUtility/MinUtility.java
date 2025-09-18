//import EFIM.ItemsetsEFIM;
//import EFIM.MainTestEFIM;

import java.io.*;
import java.util.*;

public class MinUtility {
	static int min_utility; // 180000
	static int minPeriodicity;  // minimum periodicity parameter (a number of transactions)
	static int maxPeriodicity;  // maximum periodicity parameter (a number of transactions)
	static int minAveragePeriodicity;  // minimum average periodicity (a number of transactions)
	static int maxAveragePeriodicity;  // maximum average periodicity (a number of transactions)
	static double sensitiveThreshold;
	public MinUtility(int min_utility,int minPeriodicity,int maxPeriodicity,
					  int minAveragePeriodicity,int maxAveragePeriodicity,double sensitiveThreshold){
		this.min_utility = min_utility;
		this.minPeriodicity = minPeriodicity;
		this.maxPeriodicity = maxPeriodicity;
		this.minAveragePeriodicity=minAveragePeriodicity;
		this.maxAveragePeriodicity=maxAveragePeriodicity;
		this.sensitiveThreshold=sensitiveThreshold;
	}
	public void test(List<Integer> senIDs, String filename) throws IOException{

		String after = "database/afterDB/";
		String before = "database/beforeDB/";

		String outputAfterDB = after+"item_count/"+filename+".txt";

		String inputAfterDBPHM = after+"item_PHM/"+filename+".txt";

		String outputAfterPHUI = "database/PHUIs/afterDB/"+sensitiveThreshold+"_"+filename+".txt";

		String inputBeforeDB = before+"item_count/"+filename+".txt";

		String inputUtilityTable = before+"item_utility/"+filename+".txt";

		String inputBeforeDBPHM = before+"item_PHM/"+filename+".txt";

		String outputBeforePHUI = "database/PHUIs/beforeDB/"+filename+".txt";
		String outputBeforeSPHUI = "database/PHUIs/sensitivePHUI/"+sensitiveThreshold+"_"+filename+".txt";

		ReadDB readDB = new ReadDB();
		readDB.setEuFile(inputUtilityTable);
		readDB.setBeforeDB(inputBeforeDB);
		readDB.setBeforeDBPHUIs(outputBeforePHUI);

		List<Itemset> itemsets=readDB.getItemsets();
		Map<Integer,Integer> Ex_Utility = readDB.getExUtility();
		List<TransactionTP> Database=readDB.getDatabase( Ex_Utility);
		int DBUtility_old = readDB.getDBUtility(Database);
		List<Integer> senIDsCopy = new ArrayList<>();
		senIDsCopy.addAll(senIDs);

		DatabaseProcess DBprocess = new DatabaseProcess();
		DBprocess.genLink(Database, itemsets, senIDs);

		List<Itemset> itemsetsCopy=DataCopy.ItemsetsCopy(itemsets);
        ProUtility3 protect = new ProUtility3(Database,itemsets,senIDsCopy,min_utility,Ex_Utility);
        List<TransactionTP> newDB=protect.runAlgorithm();
		GetAfterDB afterDB = new GetAfterDB();
		afterDB.getDB(newDB,inputAfterDBPHM,outputAfterDB);

		// ========================= After PHM =======================
		//===== Optional parameters (new, 2017)==//
		// Minimum number of items that patterns should contain
		int minimumLength = 1;
		// Maximum number of items that patterns should contain
		int maximumLength = Integer.MAX_VALUE;
		//===========================//
		System.out.println("============== After PHM ===============");
		AlgoPHM algoAfterPHM = new AlgoPHM();
		// set the pattern length constraints
		algoAfterPHM.setMinimumLength(minimumLength);
		algoAfterPHM.setMaximumLength(maximumLength);

		// Run the algorithm
		algoAfterPHM.runAlgorithm(inputAfterDBPHM, outputAfterPHUI, min_utility,
				minPeriodicity, maxPeriodicity, minAveragePeriodicity,
				maxAveragePeriodicity);

		// Print statistics about the execution of the algorithm
		algoAfterPHM.printStats();
		System.out.println("============== After PHM ===============");
		// ========================= After PHM =======================


		// ======================== Experiment ===================================
		System.out.println("=================== Experiment ==================");
		Set<String> sks = getSensitive(outputBeforeSPHUI);

		double dus = TestAns.getDUS(inputBeforeDBPHM, inputAfterDBPHM);
		double ius = TestAns.getIUS(outputBeforePHUI, outputAfterPHUI, sks);
		double dss = TestAns.getDSS(inputBeforeDB, outputAfterDB);
		String compare = TestAns.compare(outputBeforePHUI, outputAfterPHUI, outputBeforeSPHUI);
		System.out.println("DUS: " + dus);
		System.out.println("IUS: " + ius);
		System.out.println("DSS: " + dss);
		System.out.println("HF and MC: " + compare);

	}
	public static Set<String> getSensitive(String sensitiveFile){

		BufferedReader sks = null;
		Set<String> sensitiveItemset = new HashSet<>();

		try {
			sks = new BufferedReader(new FileReader(new File(sensitiveFile)));
			String line = null;

			while (null != (line = sks.readLine())) {
				String[] kv = line.split(":");
				String items = kv[0];
				sensitiveItemset.add(items);
			}
			sks.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sensitiveItemset;
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        