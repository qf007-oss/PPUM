import java.util.*;

public class MAU_MaxMP extends PPPUM_MaxMP {

    int selectTid = -1;
    int utilityInSelectTid = 0;

    String changeItem = null;
    int seLTIDChangeItemIU = -1;
    int seLTIDChangeItemNumber = -1;


    public void hideSI(String afterDB) {

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
        // ===============test sensitive sort==========================
        for (SensitiveItemsetList list : listsOfSensitive) {
            System.out.println("===========================");
            System.out.println("sortedSensitive: " + list);
        }
        // =============== end test sensitive sort=====================

        for (SensitiveItemsetList list : listsOfSensitive) {
            String sensitive = list.sensitive;
            int count = list.count;
            System.out.println("敏感项集 : 敏感项目个数 " + sensitive + " : " + count);
        }

        for (SensitiveItemsetList senISList : listsOfSensitive) {
            String sensitive = senISList.sensitive;
            long diff = senISList.su - getMut();
            int dec = 0;
            int largestPeriod = senISList.largestPeriod;
            int supp = senISList.supp;

            while (diff >= 0 && largestPeriod <= getMaxP() && supp >= getMinSupp()) {
                List<String> items = getKeys(sensitive);

                addressTIDandItem(senISList);

                Map<String, Integer> trans = getTransaction(database.get(selectTid));

                System.out.println("HideTrans: " + trans);
                if (seLTIDChangeItemIU <= diff) {
                    checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem,
                            utilityInSelectTid, true);
                    trans.remove(changeItem);

                    diff = diff - utilityInSelectTid;
                } else {
                    if (diff == 0) { // U(X) = minUtil  也是高效用项集
                        dec = 1;
                    } else {
                        dec = (int) Math.ceil((double) diff / getUtility(changeItem)); // 向上取整
                    }
                    checkSubsetOfSensitive(trans, selectTid, sensitive, changeItem, dec
                            * getUtility(changeItem), false);
                    if (trans.get(changeItem) - dec == 0) {
                        trans.remove(changeItem);
                    } else {
                        trans.put(changeItem, trans.get(changeItem) - dec);
                    }
                    diff = -1;
                }

                database.set(selectTid, mapToTrans(trans));
                //System.out.println("After:" + database.get(tid));

                updatedTrans.add(selectTid);
                modifiedTimes++;

            }
        }
        System.out.println("updateDatabase: " + database);

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
                if (mp > maxPerItem) {
                    maxPerItem = mp;
                    changeItem = item;
                    seLTIDChangeItemIU = sI.iu;
                    seLTIDChangeItemNumber = sI.cnt;
                }
            }
        }
    }


    public static void main(String[] args) {
        new MAU_MaxMP();
    }
}
