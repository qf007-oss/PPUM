import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class SensitiveAnalysisMaxMP {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        boolean selectSPI = true;


        String filename = "chainstore";
        int min_utility = 10000; // 180000
        int minPeriodicity = 1;  // minimum periodicity parameter (a number of transactions)
        int maxPeriodicity = 6000;  // maximum periodicity parameter (a number of transactions)
        int minAveragePeriodicity = 1;  // minimum average periodicity (a number of transactions)
        int maxAveragePeriodicity = 3000;  // maximum average periodicity (a number of transactions)
        double sensitiveThreshold = 0.05;


        // minUtil-minPer-maxPer-minAvg-maxAvg
        //String input = path+"old_liquor.txt"; // 200000-1-2000-1-1000  PHUIs: 1318
        //TODO String input = path+"chainstore.txt"; // 10000-1-6000-1-3000  PHUIs: 182
        //TODO String input = path+"kosarak.txt"; // 5000000-1-2000-1-1000  PHUIs: 896, 8s
        //TODO String input = path+"foodmart.txt"; // 3000-1-5000-1-2000  PHUIs: 915
        //TODO String input = path+"retail.txt"; // 100000-1-5000-1-2000  PHUIs: 1748
        //TODO String input = path+"mushrooms.txt"; // 5000000-1-2000-1-1000  PHUIs: 220


        String after = "database/afterDB/";
        String before = "database/beforeDB/";

        String outputAfterDB = after+"item_count/"+filename+".txt";

        String inputAfterDBPHM = after+"item_PHM/"+filename+".txt";

        String outputAfterPHUI = "database/PHUIs/afterDB/"+min_utility+"_"+maxPeriodicity+"_"+filename+".txt";

        String inputBeforeDB = before+"item_count/"+filename+".txt";

        String inputUtilityTable = before+"item_utility/"+filename+".txt";

        String inputBeforeDBPHM = before+"item_PHM/"+filename+".txt";

        String outputBeforePHUI = "database/PHUIs/beforeDB/"+filename+".txt";

        String outputBeforeSPHUI = "database/PHUIs/sensitivePHUI/"+min_utility+"_"+maxPeriodicity+"_"+filename+".txt";


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

        // ============select sensitive period high utility itemset================
        if(selectSPI){
            Path file = Paths.get(outputBeforeSPHUI);
            if(Files.exists(file)){
                System.out.println("敏感项集文件已经存在，不再重复生成敏感项集");
            }else {
                System.out.println("敏感项集文件不存在，执行select代码，选择敏感项集");

                Select select = new Select(sensitiveThreshold); // 生成敏感周期高效用项集文件

                String inputSPHUI = select.generateSensitive(outputBeforePHUI,outputBeforeSPHUI);
                System.out.println("the number of SensitivePHUI are: " + select.getNum());
                System.out.println("The SensitivePHUI_TXT: " + inputSPHUI);
            }
        }
        // ===========================select end===================================


        // ============Hide sensitive period high utility itemset================


        PPPUM_MaxMP pppum = new PPPUM_MaxMP();

        pppum.setMut(min_utility);
        pppum.setMinP(minPeriodicity);
        pppum.setMaxP(maxPeriodicity);
        pppum.setMinAP(minAveragePeriodicity);
        pppum.setMaxAP(maxAveragePeriodicity);


        //pppum = new PPPUM(min_utility, minPeriodicity, maxPeriodicity, minAveragePeriodicity, maxAveragePeriodicity);

        pppum.readUT(inputUtilityTable);
        //pppum.readSensitiveHUI(inputSPHUI);
        pppum.readSensitiveHUI(outputBeforeSPHUI);
        pppum.readDatabaseFile(inputBeforeDB);

        long startTime = System.currentTimeMillis();
        pppum.MAU_MaxMP(outputAfterDB);

        long endTime = System.currentTimeMillis();

        Set<Integer> updatedTrans = pppum.updatedTrans;
        System.out.println("number of updatedTrans: "+updatedTrans.size());

        long totalTime = endTime - startTime;
        System.out.println("#HideTime: " + totalTime + " ms");
        // ===========================Hide end===================================

        pppum.transDBformat2HUIMiner(outputAfterDB, inputAfterDBPHM);

        // ========================= After PHM =======================
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
        Set<String> sensitive = pppum.getSensitive();

        double dus = TestAns.getDUS(inputBeforeDBPHM, inputAfterDBPHM);

        double ius = TestAns.getIUS(outputBeforePHUI, outputAfterPHUI, sensitive);

        double dss = TestAns.getDSS(inputBeforeDB, outputAfterDB);

        String compare = TestAns.compare(outputBeforePHUI, outputAfterPHUI, outputBeforeSPHUI);

        System.out.println("DUS: " + dus);
        System.out.println("IUS: " + ius);
        System.out.println("DSS: " + dss);
        System.out.println("HF and MC: " + compare);
        System.out.println("#HideTime: " + totalTime + " ms");
        System.out.println("number of updatedTrans: "+updatedTrans.size());
        // ======================== End Experiment =============================
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = TestMain_MaxMP.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
