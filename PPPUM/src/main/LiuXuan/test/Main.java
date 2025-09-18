import java.io.*;
import java.util.*;

// liuxuan's algorithm
public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "mushrooms";
        int min_utility = 5000000; // 180000
        int minPeriodicity = 1;  // minimum periodicity parameter (a number of transactions)
        int maxPeriodicity = 2000;  // maximum periodicity parameter (a number of transactions)
        int minAveragePeriodicity = 1;  // minimum average periodicity (a number of transactions)
        int maxAveragePeriodicity = 1000;  // maximum average periodicity (a number of transactions)
        double sensitiveThreshold = 0.05;

        // minUtil-minPer-maxPer-minAvg-maxAvg
        //String input = path+"old_liquor.txt"; // 200000-1-2000-1-1000  PHUIs: 1318
        //TODO String input = path+"chainstore.txt"; // 10000-1-6000-1-3000  PHUIs: 182
        //TODO String input = path+"kosarak.txt"; // 5000000-1-2000-1-1000  PHUIs: 896, 8s
        //TODO String input = path+"foodmart.txt"; // 3000-1-5000-1-2000  PHUIs: 915
        //TODO String input = path+"retail.txt"; // 100000-1-5000-1-2000  PHUIs: 1748
        //TODO String input = path+"mushrooms.txt"; // 5000000-1-2000-1-1000  PHUIs: 220

        MaxUtility MAUtility = new MaxUtility(min_utility, minPeriodicity, maxPeriodicity, minAveragePeriodicity, maxAveragePeriodicity, sensitiveThreshold);
        MinUtility MIUtility = new MinUtility(min_utility, minPeriodicity, maxPeriodicity, minAveragePeriodicity, maxAveragePeriodicity, sensitiveThreshold);
        MinCount MICount = new MinCount(min_utility, minPeriodicity, maxPeriodicity, minAveragePeriodicity, maxAveragePeriodicity, sensitiveThreshold);


        List<Integer> senList = new ArrayList<>();

        String beforePHUIs = "database/PHUIs/beforeDB/"+filename+".txt";
        String sensitive = "database/PHUIs/sensitivePHUI/"+sensitiveThreshold+"_"+filename+".txt";

        senList = getSenIDs(beforePHUIs, sensitive);

        System.out.println("=============MaxUtility==============");
        MAUtility.test(senList,filename);
        System.out.println("=============end MaxUtility==============");

        System.out.println("=============MinUtility==============");
        MIUtility.test(senList,filename);
        System.out.println("=============end MinUtility==============");

        System.out.println("=============MinCount==============");
        MICount.test(senList, filename);
        System.out.println("=============end MinCount==============");
    }
    private static List<Integer> getSenIDs(String before, String sensitive) {
        List<Integer> SenIDs = new ArrayList<>();

        BufferedReader brb = null;
        BufferedReader sks = null;
        Set<String> sensitiveItemset = new HashSet<>();
        int id = 0;

        try {
            brb = new BufferedReader(new FileReader(new File(before)));
            sks = new BufferedReader(new FileReader(new File(sensitive)));
            String line = null;

            while (null != (line = sks.readLine())) {
                String[] kv = line.split(":");
                String items = kv[0];
                sensitiveItemset.add(items);
            }
            sks.close();

            while (null != (line = brb.readLine())) {
                String[] kv = line.split(":");
                String items = kv[0];
                if(sensitiveItemset.contains(items)){
                    SenIDs.add(id);
                }
                id++;
            }
            brb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SenIDs;
    }
}
