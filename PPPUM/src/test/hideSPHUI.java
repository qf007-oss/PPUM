import java.io.*;
import java.net.URL;
import java.util.*;

// 没用到
public class hideSPHUI {
    private static final String NULL = null;
    static int tid = -1;
    static String changeItem = null;

    static int minutil;
    static int minPeriod = 1; // minimum period parameter
    static int maxPeriod = 6; // maximum period parameter
    static int minAveragePeriod = 1; // minimum average period
    static int maxAveragePeriod = 2; // maximum average period

    protected static int modifiedTimes = 0;

    static Map<String, Integer> mapItemToNum;

    static Set<String> removed = new HashSet<String>();

    protected static Set<Integer> updatedTrans = new HashSet<Integer>();

    protected static Map<String, Double> sensitive = new HashMap<String, Double>();

    static HashMap<String, Object> mapList = new HashMap<String, Object>();

    protected static List<String> database = new ArrayList<String>();

    static List<Integer> list = new ArrayList<Integer>();

    static List<Integer> list1 = new ArrayList<Integer>();

    static long startTimestamp = 0; // the time the algorithm started
    static long endTimestamp = 0; // the time the algorithm terminated

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        String inputDatabaseFile = fileToPath("chainstore.txt");

        readDatabaseFile(inputDatabaseFile);

        System.out.println("database size is :" + database.size());

        minutil = 1000;
        System.out.println("minutil:" + minutil);

        String PHUIFile = "temp_file/contextPHM.txt";

        Process(PHUIFile);

        String SPHUIFile = "temp_file/contextPHM.txt_si.txt";

        readSensitiveRI(SPHUIFile);

        //System.out.println(sensitive);
        System.out.println("sensitive size is :" + sensitive.size());
        File out_path = new File("src/pprpm");
        if (!out_path.exists())
            out_path.mkdirs(); // mkdirs可以创建多级目录，父目录不一定存在。
        String dst = "src/pprpm/NewTest.txt";
        startTimestamp = System.currentTimeMillis();

        buildiTable();

        for (String sk : sensitive.keySet()) {

            list = (List<Integer>) mapList.get(sk);

            double diff = sensitive.get(sk) - minutil;

            while (diff >= 0) {
                int del = 0;

                addressTIDandItem(sk);

                List<String> tran = getTransaction(database.get(tid));
                //System.out.println("changeItem: " + changeItem);
                String tr = database.get(tid);

                tran.remove(changeItem);

                for (int i = 0; i < list.size(); i++) {
                    if (tr.equals(database.get(list.get(i)))) {
                        // del表示需要从list中删除的事务的tid
                        del = list.get(i);
                        list.remove(i);
                    }
                }

                for (String key : mapList.keySet()) {
                    if (key.contains(changeItem) & key != sk) {

                        list1 = (List<Integer>) mapList.get(key);
                        for (int m = 0; m < list1.size(); m++) {
                            if (list1.get(m) == del) list1.remove(m);
                        }
                    }
                }

                database.set(tid, ListToString(tran));

                updatedTrans.add(tid);

                modifiedTimes++;
                diff = diff - 1;
            }

            removed.add(sk);
        }

        writeDB2File(database, dst);
        endTimestamp = System.currentTimeMillis();
        System.out.println(" Total time ~ " + (endTimestamp - startTimestamp)
                + " ms");


    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException{
        URL url = hideSPHUI.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
    }

    private static String ListToString(List<String> tran) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        String resultString = "";
        for (int i = 0; i < tran.size(); i++) {
            if (i < tran.size() - 1) {
                sb.append(tran.get(i));
                sb.append(" ");
            } else {
                sb.append(tran.get(i));
            }
        }
        resultString = sb.toString();
        return resultString;
    }

    @SuppressWarnings("unchecked")
    private static void addressTIDandItem(String sk) {

        // TODO Auto-generated method stub
        int lengthtarget;

        List<String> items = getKeys(sk);

        list = (List<Integer>) mapList.get(sk);
        if (list.size() == 1) {
            tid = list.get(0);
        } else {

            for (int i = 0; i < list.size() - 1; i++) {

                int tranlengthi = getTransaction(database.get(list.get(i))).size();

                for (int j = 1; j < list.size(); j++) {
                    int tranlengthj = getTransaction(database.get(list.get(j))).size();
                    if (tranlengthi < tranlengthj) {
                        tid = list.get(i);
                        lengthtarget = tranlengthi;
                    } else {
                        tid = list.get(j);
                        lengthtarget = tranlengthj;
                    }
                }

            }
        }

        List<Integer> itemss = new ArrayList<Integer>();

        for (String item : items) {
            Integer value = mapItemToNum.get(item);
            itemss.add(value);
        }
        int MinValueOfItem = Collections.min(itemss);

        for (String iteminmap : mapItemToNum.keySet()) {
            if (mapItemToNum.get(iteminmap) == MinValueOfItem
                    && items.contains(iteminmap))
                changeItem = iteminmap;
        }

    }

    private static void Process(String rIFile) throws FileNotFoundException {
        // TODO Auto-generated method stub
        BufferedReader br1 = new BufferedReader(new FileReader(new File(rIFile)));
        mapItemToNum = new HashMap<String, Integer>();
        String line = null;

        try {
            while (null != (line = br1.readLine())) {
                String split[] = line.split("support :");
                String items[] = split[0].split(" ");

                for (int i = 0; i < items.length; i++) {
                    // convert item to integer
                    String item = items[i];
                    Integer num = mapItemToNum.get(item);
                    num = (num == null) ? 1 : num + 1;
                    mapItemToNum.put(item, num);
                }

            }
            br1.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //System.out.println(mapItemToNum);
    }

    protected static void readSensitiveRI(String SRIFile) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(new File(SRIFile)));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                String[] k_v = line.split("support :");
                double sup = Double.parseDouble(k_v[1]);
                sensitive.put(k_v[0].trim(), sup);
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public static void readDatabaseFile(String dbFile) throws FileNotFoundException {
        // TODO Auto-generated method stub
        database.clear();
        BufferedReader br = new BufferedReader(new FileReader(new File(dbFile)));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                database.add(line);
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(database);
    }

    private static void buildiTable() throws FileNotFoundException {
        // TODO Auto-generated method stub

        for (String sk : sensitive.keySet()) {
            List<String> items = getKeys(sk);
            List<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < database.size(); j++) {
                List<String> itrans = getTransaction(database.get(j));
                if (itrans.containsAll(items)) {
                    list.add(j);
                    mapList.put(sk, list);
                }
            }
        }
    }

    protected static List<String> getKeys(String key) {
        List<String> keys = new ArrayList<String>();
        for (String item : key.split(" ")) {
            keys.add(item);
        }
        return keys;
    }

    protected static List<String> getTransaction(String trans) {
        List<String> t = new ArrayList<String>();
        for (String tran : trans.split(" ")) {
            t.add(tran);
        }
        return t;
    }

    protected static void writeDB2File(List<String> db, String path) {
        System.out.println("OUT PATH:" + path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), false));
            for (String t : db) {
                bw.append(t);
                bw.newLine();

            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
