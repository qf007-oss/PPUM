import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO OK
public class ReadDB {
    Map<Integer, Integer> Ex_Utility = null;
    int DU = 0;

    String euFile;
    String beforeDB;
    String beforeDBPHUIs;

    public String getEuFile() {
        return euFile;
    }

    public void setEuFile(String euFile) {
        this.euFile = euFile;
    }

    public String getBeforeDB() {
        return beforeDB;
    }

    public void setBeforeDB(String beforeDB) {
        this.beforeDB = beforeDB;
    }

    public String getBeforeDBPHUIs() {
        return beforeDBPHUIs;
    }

    public void setBeforeDBPHUIs(String beforeDBPHUIs) {
        this.beforeDBPHUIs = beforeDBPHUIs;
    }

    public int getDU() {
        return DU;
    }

    public Map<Integer, Integer> getExUtility() throws IOException {
        Ex_Utility = new HashMap<Integer, Integer>();

        File fi = new File(euFile);
        BufferedReader reader = new BufferedReader(new FileReader(fi));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] linSplited = line.split(" ");
            int[] temp = new int[linSplited.length];
            for (int i = 0; i < linSplited.length; i++) {
                temp[i] = Integer.parseInt(linSplited[i]);
            }
            Ex_Utility.put(temp[0], temp[1]);
        }
        reader.close();
        return Ex_Utility;
    }

    public List<TransactionTP> getDatabase(Map<Integer, Integer> Ex_Utility) throws IOException {
        List<TransactionTP> dataBase = new ArrayList<>();
        File fi = new File(beforeDB);
        BufferedReader reader = new BufferedReader(new FileReader(fi));
        String line = null;
        int TID = 0;
        while ((line = reader.readLine()) != null) {
            int TU = 0;
            TID++;
            List<ItemUtility> transaction = new ArrayList<>();
            String[] split = line.split(" ");
            List<Integer> Items = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                String[] item_count = split[i].split(":");
                int item = Integer.parseInt(item_count[0]);
                int InUtility = Integer.parseInt(item_count[1]);
                int itemUtility = InUtility*Ex_Utility.get(item);
                DU += itemUtility;
                TU += itemUtility;
                Items.add(item);
                transaction.add(new ItemUtility(item, InUtility, itemUtility));
            }
            dataBase.add(new TransactionTP(transaction, TID, Items, TU));
        }
        System.out.println("���ݿ�Ч��ֵΪ��" + DU);
        reader.close();
        return dataBase;
    }

    public List<Itemset> getItemsets() throws IOException {
        List<Itemset> itemset = new ArrayList<Itemset>();
        int itemsetsCount = 0;
        String thisLine;

        File fi = new File(beforeDBPHUIs);
        BufferedReader myInput = new BufferedReader(new FileReader(fi));
        int itemID = 0;
        while ((thisLine = myInput.readLine()) != null) {
            // 490 3 6:6639921:6347:1:1535:155.95
            String line[] = thisLine.split(":");
            String[] items = line[0].split(" ");
            itemsetsCount++;
            List<Integer> temp = new ArrayList<Integer>();
            int utility = Integer.parseInt(line[1]);
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                temp.add(item);
            }
            itemset.add(new Itemset(temp, utility, itemID));
            itemID++;//���ʶ����0��ʼ
        }
        myInput.close();
        System.out.println("��Ч�������: " + itemsetsCount);
        return itemset;
    }

    public int getDBUtility(List<TransactionTP> Database) {
        int DBUtility = 0;
        for (TransactionTP transaction : Database) {
            for (ItemUtility item : transaction.itemsUtilities) {
                DBUtility += item.in_utility * Ex_Utility.get(item.item);
            }
        }
        return DBUtility;
    }

}
