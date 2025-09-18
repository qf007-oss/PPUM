import java.io.*;
import java.util.*;

public class PPPUM_MaxMP {
    protected Set<String> sensitive = new HashSet<>();

    protected List<String> database = new ArrayList<String>();
    protected Set<Integer> updatedTrans = new HashSet<Integer>();

    int numberOfTrans = 0;

    Map<String, SensitiveItemsetList> mapSensitiveItemSetToSensitiveItemSetList = new HashMap<String, SensitiveItemsetList>();

    protected List<SensitiveItemsetList> listsOfSensitive = new ArrayList<>();

    protected Map<String, Integer> utilityTable = new HashMap<String, Integer>();

    Set<String> allSensitiveItems = new HashSet<>();

    protected Map<String, ArrayList<Integer>> mapSensitiveItemToTidList = new HashMap<>();

    protected int mut;
    protected int minP;
    protected int maxP;
    protected int minAP;
    protected int maxAP;
    protected double minSupp;

    public PPPUM_MaxMP() {
    }

    public PPPUM_MaxMP(int mut, int minP, int maxP, int minAP, int maxAP) {
        this.mut = mut;
        this.minP = minP;
        this.maxP = maxP;
        this.minAP = minAP;
        this.maxAP = maxAP;
    }


    protected double dus = 0;
    protected double ius = 0;
    protected double dss = 0;
    protected int modifiedTransNum = 0;

    protected int modifiedTimes = 0;
    protected long time;
    protected double missingCost = 0;

    protected long databaseUtility = 0;

    public Set<String> getSensitive() {
        return sensitive;
    }

    public void setSensitive(Set<String> sensitive) {
        this.sensitive = sensitive;
    }

    public List<String> getDatabase() {
        return database;
    }

    public void setDatabase(List<String> database) {
        this.database = database;
    }

    public int getMut() {
        return mut;
    }

    public void setMut(int mut) {
        this.mut = mut;
    }

    public int getMaxP() {
        return maxP;
    }

    public void setMaxP(int maxP) {
        this.maxP = maxP;
    }

    public int getMinP() {
        return minP;
    }

    public void setMinP(int minP) {
        this.minP = minP;
    }

    public void setMinSupp(double minSupp) {
        this.minSupp = minSupp;
    }

    public double getMinSupp() {
        return minSupp;
    }

    public int getMaxAP() {
        return maxAP;
    }

    public void setMaxAP(int maxAP) {
        this.maxAP = maxAP;
    }

    public int getMinAP() {
        return minAP;
    }

    public void setMinAP(int minAP) {
        this.minAP = minAP;
    }

    public long getDatabaseUtility() {
        return databaseUtility;
    }

    public void setDatabaseUtility(long  databaseUtility) {
        this.databaseUtility = databaseUtility;
    }

    public Integer getUtility(String key) {
        return utilityTable.get(key);
    }

    public Map<String, Integer> getUtilityTable() {
        return utilityTable;
    }

    public void setUtilityTable(Map<String, Integer> utilityTable) {
        this.utilityTable = utilityTable;
    }

    public void readUT(String utFile) {
        BufferedReader br = FileTool.getReader(utFile);
        String line = null;
        utilityTable.clear();
        try {
            while ((line = br.readLine()) != null) {
                // [item utility]
                String[] ut = line.split(" ");
                utilityTable.put(ut[0].trim(), Integer.parseInt(ut[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readSensitiveHUI(String shuiFile) {
        BufferedReader br = FileTool.getReader(shuiFile);
        String line = null;
        //int i = 1;
        try {
            while ((line = br.readLine()) != null) {

                //System.out.println("看看我执行了吗" + i);
                //i++;

                String[] arr = line.split(":");
                String sItemSet = arr[0]; //敏感项集 "item item item"

                if("678".equals(sItemSet)){
                    System.out.println("我是678");
                    System.out.println("我是678");
                    System.out.println("我是678");
                }


                int utility = Integer.parseInt(arr[1]); //敏感项集的效用值
                int support = Integer.parseInt(arr[2]); //敏感项集的支持度
                int minPeriod = Integer.parseInt(arr[3]); //敏感项集的最小周期
                int maxPeriod = Integer.parseInt(arr[4]); //敏感项集的最大周期
                Double avgPeriod = Double.parseDouble(arr[5]); //敏感项集的平均周期

                SensitiveItemsetList seItList = new SensitiveItemsetList(sItemSet);

                List<String> sensitiveItems = getKeys(sItemSet);

                // ============test sensitiveItems===================== yes
                //System.out.println("sensitiveItems : "+sensitiveItems);
                // ============ end test sensitiveItems=====================

                allSensitiveItems.addAll(sensitiveItems);

                // ===========test allSensitiveItems===================== yes
                //System.out.println("allSensitiveItems: "+allSensitiveItems);
                // ===========end test allSensitiveItems=====================


                int size = sensitiveItems.size(); // 敏感项集中项目的个数
                seItList.setCount(size);

                seItList.su = utility;
                seItList.supp = support;
                seItList.largestPeriod = maxPeriod;
                listsOfSensitive.add(seItList);


                mapSensitiveItemSetToSensitiveItemSetList.put(sItemSet, seItList);

                sensitive.add(sItemSet);

                if("678".equals(sItemSet)){
                    System.out.println("我是678，我的SISL是："+seItList);
                }

            }

            br.close();
        } catch (Exception e) {

        }
    }


    public void readDatabaseFile(String dbFile) {
        database.clear();
        BufferedReader br = FileTool.getReader(dbFile);
        String line = null;

        try {
            long du = 0;
            int tid = 0;
            while ((line = br.readLine()) != null) {
                tid++;
                database.add(line);
                String[] items = line.split(" ");
                // System.out.println(line);

                for (String item : items) {
                    if (item.trim().equals(""))
                        continue;
                    String[] i_q = item.split(":");

                    if (allSensitiveItems.contains(i_q[0])) {
                        ArrayList<Integer> sItemTidList = mapSensitiveItemToTidList.get(i_q[0]);
                        if (null == sItemTidList) {
                            sItemTidList = new ArrayList<>();
                            mapSensitiveItemToTidList.put(i_q[0], sItemTidList);
                        }
                        sItemTidList.add(tid);
                    }


                    int q = Integer.parseInt(i_q[1]);//项目的数量
                    du += q * utilityTable.get(i_q[0].trim());
                }

                Map<String, Integer> trans = getTransaction(database.get(tid - 1));

                for (SensitiveItemsetList list : listsOfSensitive) {
                    String si = list.sensitive; // 敏感项集
                    List<String> sensitiveItems = getKeys(si);


                    if (trans.keySet().containsAll(sensitiveItems)) {
                        list.tidList.add(tid);

                        int sensitiveUtilityInTid = 0;
                        for (Map.Entry<String, Integer> entry : trans.entrySet()) {
                            String itemKey = entry.getKey();
                            if (sensitiveItems.contains(itemKey)) {
                                Integer itemNumber = entry.getValue();
                                int itemUtil = itemNumber * getUtility(itemKey);
                                sensitiveUtilityInTid += itemUtil;
                                SensitiveItems sensitiveIs = new SensitiveItems(tid, itemKey, itemUtil, itemNumber);
                                list.SIs.add(sensitiveIs);
                            }
                        }
                        list.mapTidToUtility.put(tid, sensitiveUtilityInTid);
                    }
                    mapSensitiveItemSetToSensitiveItemSetList.put(si, list);
                }

            }

            numberOfTrans = database.size(); // |D|

            for (Map.Entry<String, ArrayList<Integer>> entry : mapSensitiveItemToTidList.entrySet()) {
                String key = entry.getKey();
                ArrayList<Integer> value = entry.getValue();

                int maxItemPeriod = value.getFirst();
                int per = 0;
                for (int i = 1; i < value.size(); i++) {
                    per = value.get(i) - value.get(i - 1);
                    if (maxItemPeriod < per) {
                        maxItemPeriod = per;
                    }
                }
                per = numberOfTrans - value.getLast();
                if (maxItemPeriod < per) {
                    maxItemPeriod = per;
                }

                for (SensitiveItemsetList list : listsOfSensitive) {
                    List<SensitiveItems> sIs = list.SIs;
                    for (SensitiveItems sI : sIs) {
                        if (sI.item.equals(key)) {
                            sI.mp = maxItemPeriod;
                        }
                    }
                    mapSensitiveItemSetToSensitiveItemSetList.put(list.sensitive, list);
                }
            }

            // List<Integer> perList
            for (SensitiveItemsetList list : listsOfSensitive) {
                List<Integer> tidList = list.tidList;
                //System.out.println("tidList : " + tidList);
                List<Integer> perList = list.perList;
                perList.add(tidList.getFirst());
                int per = 0;
                for (int i = 1; i < tidList.size(); i++) {
                    per = tidList.get(i) - tidList.get(i - 1);
                    perList.add(per);
                }
                per = numberOfTrans - tidList.getLast();
                perList.add(per);
                mapSensitiveItemSetToSensitiveItemSetList.put(list.sensitive, list);
            }

            setDatabaseUtility(du);

            double supTre = (double) database.size() / getMaxAP() - 1;
            double roundedNumber = Math.round(supTre * 100) / 100.0;

            System.out.println("数据集中事务的个数：" + database.size());
            System.out.println("最小支持度阈值：|D| / maxAvg - 1 = " + roundedNumber);

            setMinSupp(roundedNumber);
            br.close();

            System.out.println("DU=" + du);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeDB2File(List<String> db, String path) {
        System.out.println("OUT PATH:" + path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    new File(path), false));
            for (String t : db) {
                bw.append(t);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String mapToTrans(Map<String, Integer> trans) {
        String str = "";
        for (String key : trans.keySet()) {
            String v = (trans.get(key) + "");
            int pos = v.indexOf(".");
            if (pos != -1) {
                v = v.substring(0, pos);
            }
            str = str + key + ":" + v + " ";
        }

        return str;
    }

    protected Map<String, Integer> getTransaction(String trans) {

        Map<String, Integer> t = new HashMap<String, Integer>();
        if (trans == null || trans.equals("")) {
            return t;
        }
        for (String attr : trans.split(" ")) {
            String[] kv = attr.split(":");
            t.put(kv[0], Integer.parseInt(kv[1]));
        }

        return t;
    }

    protected void checkSubsetOfSensitive(Map<String, Integer> trans, Integer selectTid, String sk,
                                          String item, Integer dec, boolean del) {
        for (SensitiveItemsetList list : listsOfSensitive) {
            String sensitive = list.sensitive;

            if (sensitive.equals(sk)) {
                list.su -= dec;

                if (!del) {
                    Map<Integer, Integer> mapTidToUtility = list.mapTidToUtility;
                    Integer util = mapTidToUtility.get(selectTid);
                    util = util - dec;
                    mapTidToUtility.put(selectTid, util);
                    list.mapTidToUtility = mapTidToUtility;

                    List<SensitiveItems> sIs = list.SIs;
                    for (SensitiveItems sI : sIs) {
                        if (sI.tid == selectTid && sI.item.equals(item)) {
                            sI.cnt = sI.cnt - dec / getUtility(item);
                            sI.iu = sI.iu - dec;
                        }
                    }
                    list.SIs = sIs;
                } else {
                    list.supp--;
                    list.tidList.remove(selectTid);

                    if(list.tidList!=null && !list.tidList.isEmpty()){
                        List<Integer> perList = list.perList;
                        perList.clear();
                        int per = list.tidList.getFirst();
                        perList.add(per);
                        int maxP = per;

                        for (int i = 1; i < list.tidList.size(); i++) {
                            per = list.tidList.get(i) - list.tidList.get(i - 1);
                            perList.add(per);
                            if (maxP < per) {
                                maxP = per;
                            }
                        }
                        per = numberOfTrans - list.tidList.getLast();
                        perList.add(per);
                        if (maxP < per) {
                            maxP = per;
                        }
                        list.largestPeriod = maxP;
                        list.perList = perList;
                    }else{
                        list.largestPeriod = -1;
                        list.perList = null;
                    }

                    Map<Integer, Integer> mapTidToUtility = list.mapTidToUtility;
                    mapTidToUtility.remove(selectTid);
                    list.mapTidToUtility = mapTidToUtility;

                    List<SensitiveItems> sIs = list.SIs;

                    sIs.removeIf(sI -> sI.tid == selectTid);
                    list.SIs = sIs;
                }
                continue;
            }

            List<String> keys = getKeys(sensitive);

            if (keys.contains(item)) {
                if (trans.keySet().containsAll(keys)) {

                    if (!del) {

                        list.su = list.su - dec;

                        Map<Integer, Integer> mapTidToUtility = list.mapTidToUtility;
                        Integer util = mapTidToUtility.get(selectTid);
                        util = util - dec;
                        mapTidToUtility.put(selectTid, util);
                        list.mapTidToUtility = mapTidToUtility;

                        List<SensitiveItems> sIs = list.SIs;
                        for (SensitiveItems sI : sIs) {
                            if (sI.tid == selectTid && sI.item.equals(item)) {
                                sI.cnt = sI.cnt - dec / getUtility(item);
                                sI.iu = sI.iu - dec;
                            }
                        }
                        list.SIs = sIs;

                    } else {
                        Map<Integer, Integer> mapTidToUtility = list.mapTidToUtility;
                        Integer util = mapTidToUtility.get(selectTid);
                        list.su = list.su - util;
                        mapTidToUtility.remove(selectTid);
                        list.mapTidToUtility = mapTidToUtility;

                        list.supp--;

                        list.tidList.remove(selectTid);

                        if(list.tidList!=null && !list.tidList.isEmpty()){
                            List<Integer> perList = list.perList;
                            perList.clear();

                            int per = list.tidList.getFirst();
                            perList.add(per);
                            int maxP = per;

                            for (int i = 1; i < list.tidList.size(); i++) {
                                per = list.tidList.get(i) - list.tidList.get(i - 1);
                                perList.add(per);
                                if (maxP < per) {
                                    maxP = per;
                                }
                            }
                            per = numberOfTrans - list.tidList.getLast();
                            perList.add(per);
                            if (maxP < per) {
                                maxP = per;
                            }
                            list.largestPeriod = maxP;
                            list.perList = perList;
                        }else{
                            list.largestPeriod = -1;
                            list.perList = null;
                        }
                        List<SensitiveItems> sIs = list.SIs;
                        sIs.removeIf(sI -> sI.tid == selectTid);
                        list.SIs = sIs;
                    }
                }
            }


        }
    }

    protected void printParameters() {
        System.out.println("****************************************");
        System.out.println("DU=" + databaseUtility);
        System.out.println("MUT=" + mut);
        System.out.println("****************************************");
    }

    public String transDBformat2HUIMiner(String src, String dst) {

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            FileReader fr = new FileReader(new File(src));
            fw = new FileWriter(new File(dst));
            BufferedReader br = new BufferedReader(fr);

            bw = new BufferedWriter(fw);
            String str = null;
            StringBuffer sb = new StringBuffer();

            while (null != (str = br.readLine())) {
                if (str.length() == 0) {
                    sb.append("-1"+":"+"-1"+ "\r");
                    continue;
                }

                String items = "";
                String counts = "";
                String[] item_count = str.split(" ");
                int tu = 0;

                for (int i = 0; i < item_count.length; ++i) {
                    String[] item_value = item_count[i].split(":");
                    items += item_value[0] + " ";
                    int itemUtil = getUtility(item_value[0])
                            * Integer.parseInt(item_value[1]);
                    tu += itemUtil;
                    counts += itemUtil + " ";
                }

                sb.append(items.substring(0, items.length() - 1) + ":"
                        + tu + ":"
                        + counts.substring(0, counts.length() - 1) + "\r");
            }

            bw.write(sb.toString());
            br.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dst;
    }

    int selectTid = -1;
    int utilityInSelectTid = 0;

    String changeItem = null;
    int seLTIDChangeItemIU = -1;
    int seLTIDChangeItemNumber = -1;

    public void MAU_MaxMP(String afterDB) {
        Set<String> removed = new HashSet<String>();

        Collections.sort(listsOfSensitive, new Comparator<SensitiveItemsetList>() {
            @Override
            public int compare(SensitiveItemsetList o1, SensitiveItemsetList o2) {
                return compareItems(o1.count, o2.count);
            }

            private int compareItems(int count1, int count2) {
                // int compare = count1 - count2; // 升序排序
                return count2 - count1; // 降序排序
            }
        });

        for (SensitiveItemsetList senISList : listsOfSensitive) {
            String sensitive = senISList.sensitive;
            long diff = senISList.su - getMut();
            int dec = 0;
            int largestPeriod = senISList.largestPeriod;
            int supp = senISList.supp;



            if("678".equals(sensitive)){
                System.out.println("sensitive = "+sensitive);

                System.out.println("diff: " + diff);
                System.out.println("largestPeriod: " + largestPeriod);
                System.out.println("supp: " + supp);
                System.out.println("dec: " + dec);

                System.out.println("maxPer: " + getMaxP());
                System.out.println("minSupport: " + getMinSupp());
            }

            while (diff >= 0 && largestPeriod <= getMaxP() && supp >= getMinSupp()) {

                addressTIDandItem(senISList);

                Map<String, Integer> trans = getTransaction(database.get(selectTid - 1)); // 敏感项集效用值最大的事务

                if (seLTIDChangeItemIU < diff) {
                    checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem,
                            utilityInSelectTid, true);
                    trans.remove(changeItem);

                    diff = diff - utilityInSelectTid;
                    supp--;
                    largestPeriod = senISList.largestPeriod;
                } else if (seLTIDChangeItemIU > diff){
                    if(diff==0){
                        dec=1;
                    }else{
                        dec = (int) Math.ceil((double) diff / getUtility(changeItem)); // 向上取整
                    }

                    if (trans.get(changeItem) - dec == 0) {
                        checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem,
                                utilityInSelectTid, true);
                        trans.remove(changeItem);
                        diff = diff - utilityInSelectTid;
                    } else {
                        checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem, dec
                                * getUtility(changeItem), false);
                        trans.put(changeItem, trans.get(changeItem) - dec);
                        diff = diff - dec * getUtility(changeItem);
                    }
                    //diff = -1;
                    supp=senISList.supp;
                    largestPeriod=senISList.largestPeriod;
                } else if (seLTIDChangeItemIU ==diff) {
                    checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem,
                            utilityInSelectTid, true);
                    trans.remove(changeItem);

                    diff = diff - utilityInSelectTid;

                    supp--;
                    largestPeriod=senISList.supp;
                }


                database.set(selectTid - 1, mapToTrans(trans));


                //System.out.println("After:" + database.get(tid));

                if("678".equals(sensitive)){
                    System.out.println("trans.remove(changeItem): " + trans);
                    System.out.println("update diff: " + diff);
                }


                updatedTrans.add(selectTid);
                modifiedTimes++;

            }
            //System.out.println("removed sensitive: " + sensitive);
            removed.add(sensitive);
        }
        //System.out.println("updateDatabase: " + database);

        writeDB2File(database, afterDB);
    }

    private void addressTIDandItem(SensitiveItemsetList senISList) {

        int maxUtil = -1;
        for (Map.Entry<Integer, Integer> entry : senISList.mapTidToUtility.entrySet()) {
            Integer keyTid = entry.getKey();
            Integer valueUtil = entry.getValue();
            if (valueUtil > maxUtil) {
                maxUtil = valueUtil;
                selectTid = keyTid;
                utilityInSelectTid = maxUtil;
            }
        }

        List<SensitiveItems> sIs = senISList.SIs;

        int maxPerItem = -1;
        for (SensitiveItems sI : sIs) {
            int mp = sI.mp;
            String item = sI.item;
            if (sI.tid == selectTid) {
                //System.out.println("敏感项集: " + senISList.sensitive + " selectTid: " + selectTid + " SensitiveItems: " + sI);
                if (mp > maxPerItem) {
                    maxPerItem = mp;
                    changeItem = item;
                    seLTIDChangeItemIU = sI.iu;
                    seLTIDChangeItemNumber = sI.cnt;
                }
            }
        }

    }

    protected List<String> getKeys(String key) {
        List<String> keys = new ArrayList<String>();
        for (String item : key.split(" ")) {
            keys.add(item);
        }
        return keys;
    }

}
