import java.io.IOException;

public class TestPHM {
    public static void main(String[] args) throws IOException {
        String filename = "kosarak";
        int min_utility = 5000000; // 180000
        int minPeriodicity = 1;  // minimum periodicity parameter (a number of transactions)
        int maxPeriodicity = 4000;  // maximum periodicity parameter (a number of transactions)
        int minAveragePeriodicity = 1;  // minimum average periodicity (a number of transactions)
        int maxAveragePeriodicity = 1000;  // maximum average periodicity (a number of transactions)


        // minUtil-minPer-maxPer-minAvg-maxAvg
        //String input = path+"old_liquor.txt"; // 200000-1-2000-1-1000  PHUIs: 1318
        //TODO String input = path+"chainstore.txt"; // 10000-1-6000-1-3000  PHUIs: 182
        //TODO String input = path+"kosarak.txt"; // 5000000-1-2000-1-1000  PHUIs: 896
        //TODO String input = path+"foodmart.txt"; // 3000-1-5000-1-2000  PHUIs: 915
        //TODO String input = path+"retail.txt"; // 100000-1-5000-1-2000  PHUIs: 1748
        //TODO String input = path+"mushrooms.txt"; // 5000000-1-2000-1-1000  PHUIs: 220


        String after = "database/afterDB/";
        String before = "database/beforeDB/";

        String inputBeforeDBPHM = before+"item_PHM/"+filename+".txt";

        String outputBeforePHUI = "database/PHUIs/beforeDB/"+filename+".txt";



        //===== Optional parameters (new, 2017)==//
        // Minimum number of items that patterns should contain
        int minimumLength = 1;
        // Maximum number of items that patterns should contain
        int maximumLength = Integer.MAX_VALUE;
        //===========================//

        // ========================= Before PHM =======================
        System.out.println("============== Before PHM ===============");
        AlgoPHM algoPHM = new AlgoPHM();
        // set the pattern length constraints
        algoPHM.setMinimumLength(minimumLength);
        algoPHM.setMaximumLength(maximumLength);

        // Run the algorithm
        algoPHM.runAlgorithm(inputBeforeDBPHM, outputBeforePHUI, min_utility,
                minPeriodicity, maxPeriodicity, minAveragePeriodicity,
                maxAveragePeriodicity);

        // Print statistics about the execution of the algorithm
        algoPHM.printStats();
        System.out.println("============== end Before PHM ===============");
        // ========================= Before PHM =======================

    }
}
